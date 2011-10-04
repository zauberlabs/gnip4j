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
package com.zaubersoftware.gnip4j.api.impl;

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.model.ObjectFactory;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.support.jmx.JMXProvider;
/**
 * Http implementation for the {@link GnipFacade}  
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class DefaultGnipFacade implements GnipFacade {
    private final RemoteResourceProvider facade;
    private int streamDefaultWorkers = Runtime.getRuntime().availableProcessors();
    private boolean useJMX = true;
    
    /** Creates the HttpGnipFacade. */
    public DefaultGnipFacade(final RemoteResourceProvider facade) {
        if(facade == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        this.facade = facade; 
    }

    
    @Override
    public final GnipStream createStream(
            final String domain,
            final long dataCollectorId,
            final StreamNotification observer) {
        final ExecutorService executor = Executors.newFixedThreadPool(streamDefaultWorkers);
        final GnipStream target = createStream(domain, dataCollectorId, observer, executor);
        return new GnipStream() {
            @Override
            public void close() {
                try {
                    target.close();
                } finally {
                    executor.shutdown();
                }
            }
            
            @Override
            public void await() throws InterruptedException {
                target.await();
            }
            
            @Override
            public final String getStreamName() {
                return target.getStreamName();
            }
            
            @Override
            public StreamStats getStreamStats() {
                return target.getStreamStats();
            }
        }; 
    }
    
    @Override
    public final GnipStream createStream(final String domain, final long dataCollectorId,
            final StreamNotification observer, final ExecutorService executor) {
        final DefaultGnipStream stream = new DefaultGnipStream(facade, domain, dataCollectorId, 
                executor);
        stream.open(observer);
        GnipStream ret = stream;
        if(useJMX) {
            ret = new GnipStream() {
                @Override
                public String getStreamName() {
                    return stream.getStreamName();
                }
                
                @Override
                public void close() {
                    try {
                        stream.close();
                    } finally {
                        JMXProvider.getProvider().unregister(stream);
                    }
                }
                
                @Override
                public void await() throws InterruptedException {
                    stream.await();
                }
                
                @Override
                public StreamStats getStreamStats() {
                    return stream.getStreamStats();
                }
            };
            JMXProvider.getProvider().registerBean(stream, stream.getStreamStats());
        }
        return ret;
    }

    
    public final int getStreamDefaultWorkers() {
        return streamDefaultWorkers;
    }
    
    /** @see #getStreamDefaultWorkers() */
    public final void setStreamDefaultWorkers(final int streamDefaultWorkers) {
        if(streamDefaultWorkers < 1) {
            throw new IllegalArgumentException("Must be >= 1");
        }
        this.streamDefaultWorkers = streamDefaultWorkers;
    }
    
    public final Rules getRules(String domain, long dataCollectorId) {
    	try {
			InputStream gnipRestResponseStream = facade.getResource(new URI(String.format(
					"https://%s.gnip.com/data_collectors/%d/rules.json",
					domain,
					dataCollectorId)));
			final JsonParser parser =  DefaultGnipStream.getObjectMapper()
					.getJsonFactory().createJsonParser(gnipRestResponseStream);
			Rules rules = parser.readValueAs(Rules.class);
			gnipRestResponseStream.close();
			return rules;
		} catch (URISyntaxException e) {
			throw new GnipException("The domain or collector ID were invalid", e);
		} catch (JsonProcessingException e) {
			throw new GnipException("Unexpected response from Gnip REST API", e);
		} catch (IOException e) {
			throw new GnipException(e);
		}
    }
    
    public final void addRule(String domain, long dataCollectorId, Rule rule) {
    	Rules rules = new ObjectFactory().createRules();
    	rules.getRules().add(rule);
    	
    	try {
    		facade.postResource(
    				new URI(String.format(
    						"https://%s.gnip.com/data_collectors/%d/rules.json",
    						domain,
    						dataCollectorId)),
    				rules);
    	} catch (URISyntaxException e) {
    		throw new GnipException("The domain or collector ID were invalid", e);
    	}
    }

    public final boolean isUseJMX() {
        return useJMX;
    }


    public final void setUseJMX(final boolean useJMX) {
        this.useJMX = useJMX;
    }
    
}
