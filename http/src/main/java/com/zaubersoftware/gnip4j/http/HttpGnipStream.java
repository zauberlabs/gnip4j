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

import static ar.com.zauber.leviathan.common.async.ThreadUtils.waitForTermination;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.UnhandledException;
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
import ar.com.zauber.leviathan.common.CharsetStrategy;
import ar.com.zauber.leviathan.impl.httpclient.HTTPClientURIFetcher;
import ar.com.zauber.leviathan.impl.httpclient.charset.DefaultHttpCharsetStrategy;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
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
public class HttpGnipStream extends AbstractGnipStream {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /** stream name for debugging propourse */
    private final String streamName;
    private final URI streamURI;
    
    private final Thread httpThread;
    private final ExecutorService activityService = Executors.newScheduledThreadPool(10);
    private static final CharsetStrategy charsetStrategy = new DefaultHttpCharsetStrategy();   
    
    /** Creates the HttpGnipStream. */
    public HttpGnipStream(final DefaultHttpClient client, 
                          final String domain,
                          final long dataCollectorId, 
                          final GnipAuthentication auth) {
        Validate.notNull(client, "http client is null");
        Validate.notBlank(domain, "domain is empty");
        Validate.notNull(auth, "auth is null");
        
        final StringBuilder sb = new StringBuilder("https://");
        sb.append(domain);
        sb.append(".gnip.com/data_collectors/");
        sb.append(dataCollectorId);
        sb.append("/track.json");
        streamURI = URI.create(sb.toString());
        
        streamName = String.format("%s-%d", domain, dataCollectorId);
        httpThread = new Thread(new GnipHttpConsumer(handshake(client, auth, streamURI)), 
                                streamName + "-consumer-http");
        
        httpThread.start();
    }

    /** get the connection */
    private  static HttpResponse handshake(final DefaultHttpClient client, 
                                           final GnipAuthentication auth,
                                           final URI streamUri)
        throws AuthenticationGnipException, TransportGnipException {
        client.getCredentialsProvider().setCredentials(
                new AuthScope(streamUri.getHost(), AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword()));
        
        final HttpGet get = new HttpGet(streamUri);
        try {
            final HttpResponse response  = client.execute(get);
            
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            if(statusCode == 401) {
                throw new AuthenticationGnipException(statusLine.getReasonPhrase());
            } else if(statusCode == 200) {
                return response;
            } else {
                throw new TransportGnipException(
                    String.format("Connection to %s: Unexpected status code: %s %s",
                            streamUri, statusCode, statusLine.getReasonPhrase()));
            }
        } catch (ClientProtocolException e) {
            throw new TransportGnipException("Protocol error", e);
        } catch (IOException e) {
            throw new TransportGnipException("Error", e);
        }
        
    }
    
    /** consumes the http input stream from the stream one tweet per line */
    private class GnipHttpConsumer implements Runnable {
        private final HttpResponse response;
        final ObjectMapper mapper = new ObjectMapper();
        
        {
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
            mapper.getDeserializationConfig().withAnnotationIntrospector(introspector);
            mapper.getSerializationConfig().withAnnotationIntrospector(introspector);
        }
        
        public GnipHttpConsumer(final HttpResponse response) {
            Validate.notNull(response);
            this.response = response;
        }
        
        @Override
        public void run() {
            HttpEntity entity = null;
            InputStream is = null;
            
            try {
                entity = response.getEntity();
                if (entity != null) {
                    final Charset charset = charsetStrategy.getCharset(HTTPClientURIFetcher.getMetaResponse(
                                    streamURI, response, entity), null);
                    // TODO Wrapp InputStream to count bytes and transfer rates 
                    is = entity.getContent();
                    final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
                    while(!Thread.interrupted()) {
                        final Activity activity = parser.readValueAs(Activity.class);
                        activityService.execute(new Runnable() {
                            @Override
                            public void run() {
                                getClosure().execute(activity);
                            }
                        });
                    }
                }            
            } catch(IOException e) {
                // TODO handle reconnection
                throw new UnhandledException(e);
            } finally {
                try {
                    if(is != null) {
                        try {
                            is.close();
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
            }
        }
    }
    private AtomicBoolean shuttingDown = new AtomicBoolean(false);
    
    @Override
    protected void doClose() {
        if(shuttingDown.getAndSet(true) == false) {
            logger.info("Shutting Down " + streamName);
            // no aceptamos más trabajos.
            if(httpThread != null) {
                httpThread.interrupt();
                waitForTermination(httpThread);
            }
            
            activityService.shutdown();
        } else {
            logger.info("Already shutting Down " + streamName);
        }
    }
}
