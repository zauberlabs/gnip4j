/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.ERROR_EMPTY_ACCOUNT;
import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.ERROR_EMPTY_STREAM_NAME;
import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.ERROR_NULL_ACTIVITY_SERVICE;
import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.ERROR_NULL_BASE_URI_STRATEGY;
import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.ERROR_NULL_HTTPCLIENT;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Geo;
import com.zaubersoftware.gnip4j.api.model.GeoDeserializer;
import com.zaubersoftware.gnip4j.api.stats.DefaultStreamStats;
import com.zaubersoftware.gnip4j.api.stats.ModifiableStreamStats;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.stats.StreamStatsInputStream;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Implementation acording http://docs.gnip.com/w/page/23724581/Gnip-Full-Documentation#streaminghttp
 * 
 * <verbatim> Json +----------+ HTTP +------------------+ (String) +--------------+ +-----------------+ | data |
 * -------> | GnipHttpConsumer |-----+--> | JsonConsumer |----+ | ActivityConsumer| | colector | +------------------+ |
 * +--------------+ | +-----------------+ + ---------+ | ..... +--> .... | +--------------+ | +-----------------+ +--> |
 * JsonConsumer |----+ | ActivityConsumer| +--------------+ +-----------------+ </verbatim>
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class DefaultGnipStream extends AbstractGnipStream {

    public static final ObjectMapper getObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        SimpleModule gnipActivityModule = new SimpleModule("gnip.activity", new Version(1, 0, 0, null));
        gnipActivityModule.addDeserializer(Geo.class, new GeoDeserializer(Geo.class));
        mapper.registerModule(gnipActivityModule);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JodaModule());       
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        final TimeZone tz = TimeZone.getTimeZone("GMT");
        mapper.setDateFormat(df); 
        return mapper;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** stream name for debugging propourse */
    private final String streamName;
    private final URI streamURI;
    private final RemoteResourceProvider client;
    private final ExecutorService activityService;
    private GnipHttpConsumer httpConsumer;
    private Thread httpThread;
    private final ModifiableStreamStats stats = new DefaultStreamStats();

    private StreamNotification notification = new StreamNotificationAdapter() {
        @Override
        public void notify(final Activity activity, final GnipStream stream) {
            logger.warn("No notification is registed for stream {}", getStreamName());
        }
    };

    private boolean captureStats = true;

    /** Creates the HttpGnipStream. */
    public DefaultGnipStream(final RemoteResourceProvider client, final String account, final String streamName,
            final ExecutorService activityService, final UriStrategy baseUriStrategy) {
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
    public final void open(final StreamNotification notification) {
        if (notification == null) {
            throw new IllegalArgumentException(getStreamName() + " does not support null observers");
        } else {
            this.notification = notification;
        }

        if (httpConsumer != null) {
            throw new IllegalStateException("The stream is open");
        }

        this.httpConsumer = new GnipHttpConsumer(getStreamInputStream());
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

        while (wait && thread.isAlive()) {
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
        if (!shuttingDown.getAndSet(true)) {
            logger.info("Shutting Down " + streamName);

            httpConsumer.closeInputStream();

            // jobs are no longer accepted
            if (httpThread != null) {
                httpThread.interrupt();
                if (!Thread.currentThread().equals(httpThread)) { // avoid deadlock in single thread env
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
        if (captureStats) {
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

        private InputStream is;

        /**
         * Creates the GnipHttpConsumer.
         * 
         * @param response
         */
        public GnipHttpConsumer(final InputStream response) {
            if (response == null) {
                throw new IllegalArgumentException("response is null");
            }
            this.is = response;
        }

        @Override
        public void run() {
            try {
                while (!shuttingDown.get() && !Thread.interrupted()) {
                    try {
                        if (is == null) {
                            reconnect();
                        }
                        if (is != null) {
                            final JsonParser parser = getObjectMapper().getJsonFactory().createJsonParser(is);

                            logger.debug("Starting to consume activity stream {} ...", streamName);
                            while (!Thread.interrupted()) {
                                final Activity activity = parser.readValueAs(Activity.class);
                                if (activity == null) {
                                    logger.warn("Activity parsed from stream {} is null. Should not happen!",
                                            streamName);
                                    continue;
                                }
                                if (activity.getBody() == null) {
                                    logger.warn("{}: Activity with id {} and link {} has a null body", new Object[] {
                                            streamName, activity.getId(), activity.getLink() });
                                }
                                logger.trace("{}: Notifying activity {}", streamName, activity.getBody());
                                activityService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        notification.notify(activity, DefaultGnipStream.this);
                                    }
                                });
                            }
                            logger.debug("{}: The activity stream is no longer being consumed.", streamName);
                        }
                    } catch (final IOException e) {
                        final String msg = "I/O error in channel " + streamName + ": " + e.getLocalizedMessage();
                        if (logger.isWarnEnabled()) {
                            logger.warn(msg, e);
                        }
                        if (!shuttingDown.get()) {
                            activityService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    notification.notifyConnectionError(new TransportGnipException(msg, e));
                                }
                            });
                        }
                    } catch (final Throwable e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn(
                                    "Unexpected exception while consuming activity stream " + streamName + ": "
                                            + e.getMessage(), e);
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
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
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
                reConnectionWaitTime = (reConnectionWaitTime > MAX_RE_CONNECTION_WAIT_TIME) ? MAX_RE_CONNECTION_WAIT_TIME
                        : reConnectionWaitTime;
                activityService.execute(new Runnable() {
                    @Override
                    public void run() {
                        notification.notifyReConnectionAttempt(attempt, reConnectionWaitTime);
                    }
                });
                logger.debug("{}: Waiting for {} ms till next re-connection", streamName, reConnectionWaitTime);
                try {
                    Thread.sleep(reConnectionWaitTime);
                } catch (final InterruptedException e) {
                    throw new GnipException(streamName + ": waiting for reconnection", e);
                }
                logger.debug("{}: Re-connecting stream with Gnip: {}", streamName, streamURI);
                is = getStreamInputStream();
                logger.debug("{}: The re-connection has been successfully established", streamName);

                reConnectionAttempt.set(0);
                reConnectionWaitTime = INITIAL_RE_CONNECTION_WAIT_TIME;
                stats.incrementNumberOfSuccessfulReconnections();

            } catch (final Throwable e) {
                logger.error(streamName + ": The re-connection could not be established", e);
                activityService.execute(new Runnable() {
                    @Override
                    public void run() {
                        notification.notifyReConnectionError(e instanceof GnipException ? ((GnipException) e)
                                : new GnipException(e));
                    }
                });
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
