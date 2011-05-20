/**
 * Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>
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
package com.zaubersoftware.gnip4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.AbstractGnipStream;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Implementation acording   
 * http://docs.gnip.com/w/page/23724581/Gnip-Full-Documentation#streaminghttp
 * 
 * <verbatim>
 *                                               Json
 *   +----------+   HTTP   +------------------+ (String) +--------------+        +-----------------+
 *   | data     | -------> | GnipHttpConsumer |-----+--> | JsonConsumer |----+   | ActivityConsumer|
 *   | colector |          +------------------+     |    +--------------+    |   +-----------------+
 *   + ---------+                                   |         .....          +-->     ....
 *                                                  |    +--------------+    |   +-----------------+
 *                                                  +--> | JsonConsumer |----+   | ActivityConsumer|
 *                                                       +--------------+        +-----------------+
 * </verbatim>
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public abstract class AbstractHttpGnipStream extends AbstractGnipStream {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** stream name for debugging propourse */
    private final String streamName;
    private final URI streamURI;
    private final DefaultHttpClient client;
    private final GnipAuthentication auth;
    private final ExecutorService activityService = Executors.newScheduledThreadPool(10);
    private GnipHttpConsumer httpConsumer;
    private Thread httpThread;
    
    /**
     * Creates the HttpGnipStream.
     *
     * @param client
     * @param domain
     * @param dataCollectorId
     * @param auth
     */
    public AbstractHttpGnipStream(
            @NotNull final DefaultHttpClient client, 
            @NotNull final String domain,
            @NotNull final long dataCollectorId, 
            @NotNull final GnipAuthentication auth) {
        if(client == null) {
            throw new IllegalArgumentException("The HTTP client cannot be null");
        }
        if(domain == null) {
            throw new IllegalArgumentException("The domain cannot be empty");
        }
        if(auth == null) {
            throw new IllegalArgumentException("The Gnip authentication cannot null");
        }
        
        final StringBuilder sb = new StringBuilder("https://");
        sb.append(domain);
        sb.append(".gnip.com/data_collectors/");
        sb.append(dataCollectorId);
        sb.append("/track.json");
        this.streamURI = URI.create(sb.toString());
        
        this.client = client;
        this.auth = auth;
        this.streamName = String.format("%s-%d", domain, dataCollectorId);
    }

    @Override
    protected final String getStreamName() {
        return streamName;
    }
    
    @Override
    public final void open(final StreamNotification notification) {
        if(notification == null) {
            throw new IllegalArgumentException(getStreamName() + " does not support null observers");
        } 
        
        if (httpConsumer != null) {
            throw new IllegalStateException("The stream is open");
        }
        
        this.httpConsumer = new GnipHttpConsumer(handshake());
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
            } catch (InterruptedException e) {
                try {
                    // Avoid high CPU usage
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
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
                waitForTermination(httpThread);
            }
            
            activityService.shutdown();
        } else {
            logger.info("Already shutting Down " + streamName);
        }
    }
    
    /** @return the {@link DefaultHttpClient} */
    protected final DefaultHttpClient getHttpClient() {
        return client;
    }
    
    /**
     * Template method that obtains the {@link HttpResponse} for the given {@link HttpUriRequest}
     * 
     * @param request
     * @return
     */
    protected abstract HttpResponse getResponse(@NotNull HttpUriRequest request) throws IOException;
    
    /**
     * Performs the handshake to establish a new stream connection with Gnip.
     * 
     * @return
     * @throws AuthenticationGnipException
     * @throws TransportGnipException
     */
    private HttpResponse handshake()
        throws AuthenticationGnipException, TransportGnipException {
        logger.debug("Handshaking with Gnip to establish a new stream connection");
        logger.trace("\t-- Setting Gnip credentials");
        client.getCredentialsProvider().setCredentials(
                new AuthScope(streamURI.getHost(), AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword()));
        
        final HttpGet get = new HttpGet(streamURI);
        try {
            logger.trace("\t-- Executing get request to URI {}", streamURI);
            final HttpResponse response  = getResponse(get);
            
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            logger.trace("\t-- Response status code {}", statusCode);
            if (statusCode == 401) {
                throw new AuthenticationGnipException(statusLine.getReasonPhrase());
            } else if (statusCode == 200) {
                logger.debug("The handshake was successfully done");
                return response;
            } else {
                throw new TransportGnipException(
                    String.format("Connection to %s: Unexpected status code: %s %s",
                            streamURI, statusCode, statusLine.getReasonPhrase()));
            }
        } catch (final ClientProtocolException e) {
            throw new TransportGnipException("Protocol error", e);
        } catch (final IOException e) {
            throw new TransportGnipException("Error", e);
        }
    }
    
    /** 
     * Consumes the HTTP input stream from the stream one {@link Activity} per line 
     */
    private class GnipHttpConsumer implements Runnable {
        private static final long MAX_RE_CONNECTION_WAIT_TIME = 5 * 60 * 1000; // 5 minutes
        private static final long INITIAL_RE_CONNECTION_WAIT_TIME = 250;    
        
        private final ObjectMapper mapper = new ObjectMapper();
        private final AtomicInteger reConnectionAttempt = new AtomicInteger();
        private long reConnectionWaitTime = INITIAL_RE_CONNECTION_WAIT_TIME;
        private HttpResponse response;
        private InputStream is = null;
        
        /**
         * Creates the GnipHttpConsumer.
         *
         * @param response
         */
        public GnipHttpConsumer(final HttpResponse response) {
            if(response == null) {
                throw new IllegalArgumentException("response is null");
            }
            this.response = response;
            
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
            mapper.setDeserializationConfig(
                    mapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
            mapper.setSerializationConfig(
                    mapper.getSerializationConfig().withAnnotationIntrospector(introspector));
        }
        
        @Override
        public void run() {
            HttpEntity entity = null;

            while (!shuttingDown.get()) {
                try {
                    entity = response.getEntity();
                    if (entity != null) {
                        // TODO Wrapp InputStream to count bytes and transfer rates 
                        is = entity.getContent();
//                      is = new TeeInputStream(is, new OutputStream() {
//                      private final byte []bytes = new byte[4096 * 4];
//                      private int i  = 0;
//                      @Override
//                      public void write(final int b) throws IOException {
//                          if(i < bytes.length && b != '\n') {
//                              bytes[i++] = (byte) b;
//                          } else {
//                              System.out.println(i);
//                              System.out.println("--------> " + new String(bytes, 0, i, "UTF-8"));
//                              i = 0;
//                          }
//                      }
//                  });

                        final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
                        logger.debug("Starting to consume activity stream...");
                        while(!Thread.interrupted()) {
                            final Activity activity = parser.readValueAs(Activity.class);
                            if (activity == null) {
                                logger.warn("Activity parsed from stream is null. Should not happen!");
                                continue;
                            }
                            if (activity.getBody() == null) {
                                logger.warn("Activity with id {} and link {} has a null body",
                                        activity.getId(), activity.getLink());
                            }
                            logger.trace("Notifying activity {}", activity.getBody());
                            activityService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    getNotification().notify(activity, AbstractHttpGnipStream.this);
                                }
                            });
                        }
                        logger.debug("The activity stream is no longer being consumed.");
                    }            
                } catch(final IOException e) {
                    if(!shuttingDown.get()) {
                        logger.warn("There was a problem with the channel.", e);
                        activityService.execute(new Runnable() {
                            @Override
                            public void run() {
                                getNotification().notifyConnectionError(
                                        new TransportGnipException("There was a problem with the channel", e));
                            }
                        });
                    }
                } catch (final Exception e) {
                    logger.warn("Unexpected exception while consuming activity stream", e);
                } finally {
                    try {
                        if(is != null) {
                            try {
                                is.close();
                                is = null;
                            } catch (final IOException e) {
                                // ignore
                            }
                        }
                    } finally {
                        try {
                            if(entity != null) {
                                try {
                                    EntityUtils.consume(entity);
                                } catch (final IOException e) {
                                    // ignore
                                }
                            }
                        } finally {
                            //If the stream is not being shutdown, it must be reconnected
                            if (!shuttingDown.get()) {
                                reconnect();
                            } else {
                                close();
                            }
                        }
                    }
                }
            }
        }
        
        /**
         * Cleanly close the input stream
         */
        void closeInputStream() {
            if(is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        
        /**
         * Re-connects the stream
         */
        private void reconnect() {
            logger.debug("Reconnecting ...");
            try {
                final int attempt = reConnectionAttempt.incrementAndGet();
                logger.debug("Waiting for {} ms till next re-connection", reConnectionWaitTime);
                reConnectionWaitTime = (long) (reConnectionWaitTime * 2);
                reConnectionWaitTime = (reConnectionWaitTime > MAX_RE_CONNECTION_WAIT_TIME) 
                    ? MAX_RE_CONNECTION_WAIT_TIME : reConnectionWaitTime;
                try {
                    Thread.sleep(reConnectionWaitTime);
                } catch (InterruptedException e) {
                    throw new GnipException(e);
                }
                activityService.execute(new Runnable() {
                    @Override
                    public void run() {
                        getNotification().notifyReConnection(attempt, reConnectionWaitTime);
                    }
                });
                logger.debug("Re-connecting stream with Gnip.");
                response = handshake();
                logger.debug("The re-connection has been successfully established");
                
                reConnectionAttempt.set(0);
                reConnectionWaitTime = INITIAL_RE_CONNECTION_WAIT_TIME;
                
            } catch (final GnipException e) {
                logger.error("The re-connection could not be established", e);
                activityService.execute(new Runnable() {
                    @Override
                    public void run() {
                        getNotification().notifyReConnectionError(e);
                    }
                });
            }
        }
    }
    private AtomicBoolean shuttingDown = new AtomicBoolean(false);
    
}
