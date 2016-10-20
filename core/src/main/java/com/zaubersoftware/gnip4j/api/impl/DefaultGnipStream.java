/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.impl;
import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.formats.FeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.Unmarshaller;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.stats.DefaultStreamStats;
import com.zaubersoftware.gnip4j.api.stats.ModifiableStreamStats;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.stats.StreamStatsInputStream;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Implementation acording
 * http://docs.gnip.com/w/page/23724581/Gnip-Full-Documentation#streaminghttp
 *
 * {@literal
 *                                               Json
 *   +----------+   HTTP   +------------------+ (String) +--------------+        +-----------------+
 *   | data     | -------> | GnipHttpConsumer |-----+--> | JsonConsumer |----+   | ActivityConsumer|
 *   | colector |          +------------------+     |    +--------------+    |   +-----------------+
 *   + ---------+                                   |         .....          +-->     ....
 *                                                  |    +--------------+    |   +-----------------+
 *                                                  +--> | JsonConsumer |----+   | ActivityConsumer|
 *                                                       +--------------+        +-----------------+
 * }
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class DefaultGnipStream extends AbstractGnipStream {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** stream name for debugging propourse */
    private final String streamName;
    private final URI streamURI;
    private final RemoteResourceProvider client;
    private final ExecutorService activityService;
    private GnipHttpConsumer httpConsumer;
    private Thread httpThread;
    private final ModifiableStreamStats stats = new DefaultStreamStats();

    private StreamNotification notification = new StreamNotificationAdapter<Activity>() {
        @Override
        public void notify(final Activity activity, final GnipStream stream) {
            logger.warn("No notification is registed for stream {}", getStreamName());
        }
    };

    private boolean captureStats = true;

    /** Creates the HttpGnipStream. */
    public DefaultGnipStream(
            final RemoteResourceProvider client,
            final String account,
            final String streamName,
            final ExecutorService activityService,
            final UriStrategy baseUriStrategy) {
        if (client == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        if (account == null || account.trim().length() == 0) {
            throw new IllegalArgumentException(ERROR_EMPTY_ACCOUNT);
        }
        if (streamName == null || streamName.trim().length() == 0) {
            throw new IllegalArgumentException(ERROR_EMPTY_STREAM_NAME);
        }
        if (activityService == null) {
            throw new IllegalArgumentException(ERROR_NULL_ACTIVITY_SERVICE);
        }
        if (baseUriStrategy == null) {
            throw new IllegalArgumentException(ERROR_NULL_BASE_URI_STRATEGY);
        }

        this.streamURI = baseUriStrategy.createStreamUri(account, streamName);
        this.client = client;
        this.streamName = streamName;
        this.activityService = activityService;
    }

    @Override
    public final String getStreamName() {
        return streamName;
    }

    /** open the stream */
      public final <X> void open(final StreamNotification<X> notification, final Unmarshaller<X> unmarshaller,
              final FeedProcessor processor) {
        if(notification == null) {
            throw new IllegalArgumentException(getStreamName() + " does not support null observers");
        }  else {
            this.notification  = notification;
        }

        if (httpConsumer != null) {
            throw new IllegalStateException("The stream is open");
        }

        this.httpConsumer = new GnipHttpConsumer(getStreamInputStream(), processor);
        this.httpThread = new Thread(httpConsumer, streamName + "-consumer-http");
        httpThread.start();
    }

    /**
     * Waits for a thread to terminate
     *
     * @param thread
     * @return
     */
    public static boolean waitForTermination(final Thread thread) {
        boolean wait = true;

        while(wait && thread.isAlive()) {
            try {
                thread.join();
                wait = false;
            } catch (final InterruptedException e) {
                try {
                    // Avoid high CPU usage
                    Thread.sleep(200);
                } catch (final InterruptedException e1) {
                    // Nothing to be done
                }
            }
        }
        return wait;
    }

    @Override
    protected final void doClose() {
        if(!shuttingDown.getAndSet(true)) {
            logger.info("Shutting Down " + streamName);

            httpConsumer.closeInputStream();

            //jobs are no longer accepted
            if(httpThread != null) {
                httpThread.interrupt();
                if(!Thread.currentThread().equals(httpThread)) { // avoid deadlock in single thread env
                    waitForTermination(httpThread);
                }
            }

            activityService.shutdown();
        } else {
            logger.info("Already shutting Down " + streamName);
        }
    }


    /** @return the stream {@link InputStream} */
    private InputStream getStreamInputStream() {
        InputStream ret = client.getResource(streamURI);
        if(captureStats) {
            ret = new StreamStatsInputStream(stats, ret);
        }
        return ret;
    }
    /**
     * Consumes the HTTP input stream from the stream one {@link Activity} per line
     */
    private class GnipHttpConsumer implements Runnable {
        private static final long MAX_RE_CONNECTION_WAIT_TIME = 5 * 60 * 1000; // 5 minutes
        private static final long INITIAL_RE_CONNECTION_WAIT_TIME = 250;

        private final AtomicInteger reConnectionAttempt = new AtomicInteger();
        private long reConnectionWaitTime = INITIAL_RE_CONNECTION_WAIT_TIME;
        private AtomicLong disconnectedSinceTime = null;

        private InputStream is;
        private final FeedProcessor processor;
        
        /** Creates the GnipHttpConsumer. */
        public GnipHttpConsumer(final InputStream response, final FeedProcessor proccesor) {
            if(response == null) {
                throw new IllegalArgumentException("response is null");
            }
            if(proccesor == null) {
                throw new IllegalArgumentException("processor is null");
            }
            this.is = response;
            this.processor = proccesor;
        }


        @Override
        public void run() {
            try {
                while (!shuttingDown.get() && !Thread.interrupted()) {
                    try {
                        if(is == null) {
                        	if (disconnectedSinceTime == null){
                        		disconnectedSinceTime = new AtomicLong(System.currentTimeMillis());
                        	}
                            reconnect();
                        }
                        if(is != null) {
                            processor.process(is,stats);
                            logger.debug("{}: The activity stream is no longer being consumed.", streamName);
                        }
                    } catch(final IOException e) {
                        final String msg = "I/O error in channel " + streamName + ": " + e.getLocalizedMessage();
                        if(logger.isWarnEnabled()) {
                            logger.warn(msg, e);
                        }
                        if(!shuttingDown.get()) {
                            activityService.execute(() -> notification.notifyConnectionError(new TransportGnipException(msg, e)));
                        }
                    } catch (final Throwable e) {
                        if(logger.isWarnEnabled()) {
                            logger.warn("Unexpected exception while consuming activity stream "
                                + streamName + ": " + e.getMessage(), e);
                        }
                    } finally {
                        closeInputStream();
                    }
                }
            } finally {
                close();
            }
        }

        /** Cleanly close the input stream */
        void closeInputStream() {
            if(is != null) {
                try {
                    is.close();
                } catch(final IOException e) {
                    throw new TransportGnipException(e);
                }
                is = null;
            }
        }

        /** Re-connects the stream */
        private void reconnect() {
            stats.incrementNumberOfReconnectionsAttempt();
            logger.debug("{}: Reconnecting...", streamName);
            try {
                final int attempt = reConnectionAttempt.incrementAndGet();
                reConnectionWaitTime = (reConnectionWaitTime * 2);
                reConnectionWaitTime = (reConnectionWaitTime > MAX_RE_CONNECTION_WAIT_TIME)
                    ? MAX_RE_CONNECTION_WAIT_TIME : reConnectionWaitTime;
                activityService.execute(() -> notification.notifyReConnectionAttempt(attempt, reConnectionWaitTime));
                logger.debug("{}: Waiting for {} ms till next re-connection", streamName, reConnectionWaitTime);
                try {
                    Thread.sleep(reConnectionWaitTime);
                } catch (final InterruptedException e) {
                    throw new GnipException(streamName + ": waiting for reconnection", e);
                }
                logger.debug("{}: Re-connecting stream with Gnip: {}", streamName, streamURI);
                is = getStreamInputStream();
                logger.debug("{}: The re-connection has been successfully established", streamName);
                notification.notifyReConnected(reConnectionAttempt.get(), System.currentTimeMillis() - disconnectedSinceTime.get());
                disconnectedSinceTime = null;
                reConnectionAttempt.set(0);
                reConnectionWaitTime = INITIAL_RE_CONNECTION_WAIT_TIME;
                stats.incrementNumberOfSuccessfulReconnections();

            } catch (final Throwable e) {
                logger.error(streamName + ": The re-connection could not be established", e);
                activityService.execute(() -> notification.notifyReConnectionError(e instanceof GnipException
                        ? ((GnipException)e) : new GnipException(e)));
            }
        }
    }
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);


    public final boolean isCaptureStats() {
        return captureStats;
    }

    public final void setCaptureStats(final boolean captureStats) {
        this.captureStats = captureStats;
    }

    @Override
    public final StreamStats getStreamStats() {
        return stats;
    }
}
