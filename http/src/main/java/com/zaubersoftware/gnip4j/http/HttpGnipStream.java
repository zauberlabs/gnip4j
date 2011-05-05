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

import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.zauber.commons.validate.Validate;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
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
public final class HttpGnipStream extends AbstractGnipStream {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** stream name for debugging propourse */
    private final String streamName;
    private final URI streamURI;
    private final DefaultHttpClient client;
    private final GnipAuthentication auth;
    private final Thread httpThread;
    private final GnipHttpConsumer httpConsumer;
    private final ExecutorService activityService = Executors.newScheduledThreadPool(10);
    
    /**
     * Creates the HttpGnipStream.
     *
     * @param client
     * @param domain
     * @param dataCollectorId
     * @param auth
     */
    public HttpGnipStream(
            @NotNull final DefaultHttpClient client, 
            @NotNull final String domain,
            @NotNull final long dataCollectorId, 
            @NotNull final GnipAuthentication auth) {
        Validate.notNull(client, "The HTTP client cannot be null");
        Validate.notBlank(domain, "The domain cannot be empty");
        Validate.notNull(auth, "The Gnip authentication cannot null");
        
        final StringBuilder sb = new StringBuilder("https://");
        sb.append(domain);
        sb.append(".gnip.com/data_collectors/");
        sb.append(dataCollectorId);
        sb.append("/track.json");
        this.streamURI = URI.create(sb.toString());
        
        this.client = client;
        this.auth = auth;
        this.streamName = String.format("%s-%d", domain, dataCollectorId);
        this.httpConsumer = new GnipHttpConsumer(handshake(client, auth, streamURI, logger));
        this.httpThread = new Thread(httpConsumer, streamName + "-consumer-http");
        
        httpThread.start();
    }

    /**
     * Performs the handshake to establish a new stream connection with Gnip.
     * 
     * @param client
     * @param auth
     * @param streamUri
     * @return
     * @throws AuthenticationGnipException
     * @throws TransportGnipException
     */
    private static HttpResponse handshake(
            @NotNull final DefaultHttpClient client, 
            @NotNull final GnipAuthentication auth,
            @NotNull final URI streamUri,
            @NotNull final Logger logger)
        throws AuthenticationGnipException, TransportGnipException {
        logger.debug("Handshaking with Gnip to establish a new stream connection");
        logger.trace("\t-- Setting Gnip credentials");
        client.getCredentialsProvider().setCredentials(
                new AuthScope(streamUri.getHost(), AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword()));
        
        final HttpGet get = new HttpGet(streamUri);
        try {
            logger.trace("\t-- Executing get request to URI {}", streamUri);
            final HttpResponse response  = client.execute(get);
            
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
                            streamUri, statusCode, statusLine.getReasonPhrase()));
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
        private final Logger logger = LoggerFactory.getLogger(GnipHttpConsumer.class);
        private final ObjectMapper mapper = new ObjectMapper();
        private HttpResponse response;
        private InputStream is = null;
        
        /**
         * Creates the GnipHttpConsumer.
         *
         * @param response
         */
        public GnipHttpConsumer(final HttpResponse response) {
            Validate.notNull(response);
            this.response = response;
            
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
            mapper.getDeserializationConfig().withAnnotationIntrospector(introspector);
            mapper.getSerializationConfig().withAnnotationIntrospector(introspector);
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
                        final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
                        logger.debug("Starting to consume activity stream...");
                        while(!Thread.interrupted()) {
                            final Activity activity = parser.readValueAs(Activity.class);
                            activityService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    getClosure().execute(activity);
                                }
                            });
                        }
                        logger.debug("The activity stream is no longer being consumed.");
                    }            
                } catch(final IOException e) {
                    if(!shuttingDown.get()) {
                        logger.warn("There was a problem with the channel.", e);
                        //TODO add observer notification
                    }
                } finally {
                    try {
                        if(is != null) {
                            try {
                                is.close();
                                is = null;
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                    } finally {
                        try {
                            if(entity != null) {
                                try {
                                    EntityUtils.consume(entity);
                                } catch (IOException e) {
                                    // ignore
                                }
                            }
                        } finally {
                            close();
                        }
                    }
                    //If the stream is not being shutdown, it must be reconnected
                    if (!shuttingDown.get()) {
                        reconnect();
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
            try {
                logger.debug("Re-connecting stream with Gnip.");
                response = handshake(client, auth, streamURI, logger);
                logger.debug("The re-connection has been successfully established");
            } catch (final GnipException e) {
                logger.error("The re-connection could not be established", e);
                //TODO add re-connection error observer
            }
        }
    }
    private AtomicBoolean shuttingDown = new AtomicBoolean(false);
    
    @Override
    protected void doClose() {
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
    
    /** espera que termine un thread */
    public static boolean waitForTermination(final Thread thread) {
        boolean wait = true;
        
        // esperamos que el scheduler consuma todos los trabajos
        while(wait && thread.isAlive()) {
            try {
                thread.join();
                wait = false;
            } catch (InterruptedException e) {
                try {
                    // si justo tenemos un bug, evitamos comermos  todo el cpu
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    // nada que  hacer
                }
            }
        }
        return wait;
    }
}
