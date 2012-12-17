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

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
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

    private static final UriStrategy DEFAULT_BASE_URI_STRATEGY = new DefaultUriStrategy();

    private final RemoteResourceProvider facade;
    private int streamDefaultWorkers = Runtime.getRuntime().availableProcessors();
    private boolean useJMX = true;
    private final UriStrategy baseUriStrategy;

    /** Creates the HttpGnipFacade. */
    public DefaultGnipFacade(final RemoteResourceProvider facade, final UriStrategy baseUriStrategy) {
        if(facade == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        if (baseUriStrategy == null) {
            throw new IllegalArgumentException(ERROR_NULL_BASE_URI_STRATEGY);
        }
        this.facade = facade;
        this.baseUriStrategy = baseUriStrategy;
    }

    /** Creates the HttpGnipFacade. */
    public DefaultGnipFacade(final RemoteResourceProvider facade) {
        this(facade, DEFAULT_BASE_URI_STRATEGY);
    }

    @Override
    public final GnipStream createStream(
            final String account,
            final String streamName,
            final StreamNotification observer) {
        final ExecutorService executor = Executors.newFixedThreadPool(streamDefaultWorkers);
        final GnipStream target = createStream(account, streamName, observer, executor);
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
    public final GnipStream createStream(final String account, final String streamName,
            final StreamNotification observer, final ExecutorService executor) {
        final DefaultGnipStream stream = createStream(account, streamName, executor);
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

    @Override
    public final Rules getRules(final String account, final String streamName) {
        try {
            final InputStream gnipRestResponseStream = facade.getResource(baseUriStrategy
                    .createRulesUri(account, streamName));
            final JsonParser parser =  JsonActivityFeedProcessor.getObjectMapper()
                    .getJsonFactory().createJsonParser(gnipRestResponseStream);
            final Rules rules = parser.readValueAs(Rules.class);
            gnipRestResponseStream.close();
            return rules;
        } catch (final JsonProcessingException e) {
            throw new GnipException("Unexpected response from Gnip REST API", e);
        } catch (final IOException e) {
            throw new GnipException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.zaubersoftware.gnip4j.api.GnipFacade#addRule(java.lang.String, java.lang.String, com.zaubersoftware.gnip4j.api.model.Rule)
     */
    @Override
    public final void addRule(final String account, final String streamName, final Rule rule) {
        Rules rules = new Rules();
        rules.getRules().add(rule);
        addRules(account, streamName, rules);
    }
    
    /* (non-Javadoc)
     * @see com.zaubersoftware.gnip4j.api.GnipFacade#addRules(java.lang.String, java.lang.String, com.zaubersoftware.gnip4j.api.model.Rules)
     */
    @Override
    public final void addRules(final String account, final String streamName, final Rules rules) {
        facade.postResource(baseUriStrategy.createRulesUri(account, streamName), rules);
    }
    
    /* (non-Javadoc)
     * @see com.zaubersoftware.gnip4j.api.GnipFacade#deleteRule(java.lang.String, java.lang.String, com.zaubersoftware.gnip4j.api.model.Rule)
     */
    @Override
    public final void deleteRule(final String account, final String streamName, final Rule rule) {
        Rules rules = new Rules();
        rules.getRules().add(rule);
        deleteRules(account, streamName, rules);
    }
    
    /* (non-Javadoc)
     * @see com.zaubersoftware.gnip4j.api.GnipFacade#deleteRules(java.lang.String, java.lang.String, com.zaubersoftware.gnip4j.api.model.Rules)
     */
    @Override
    public final void deleteRules(final String account, final String streamName, final Rules rules) {
        facade.deleteResource(baseUriStrategy.createRulesUri(account, streamName), rules);
    }

    public final boolean isUseJMX() {
        return useJMX;
    }


    public final void setUseJMX(final boolean useJMX) {
        this.useJMX = useJMX;
    }

    /** Creates a new {@link DefaultGnipStream} */
    private DefaultGnipStream createStream(final String account, final String streamName,
            final ExecutorService executor) {
            return new DefaultGnipStream(facade, account, streamName, executor, baseUriStrategy);
    }
}
