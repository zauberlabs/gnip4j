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
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.zaubersoftware.gnip4j.api.model.ruleValidation.RulesValidation;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;

import com.zaubersoftware.gnip4j.api.EDCStreamBuilder;
import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.PowertrackStreamBuilder;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.impl.formats.ActivityUnmarshaller;
import com.zaubersoftware.gnip4j.api.impl.formats.ByLineFeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.FeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.JSONActivityUnmarshaller;
import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.Unmarshaller;
import com.zaubersoftware.gnip4j.api.impl.formats.XMLActivityStreamFeedProcessor;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.support.jmx.JMXProvider;

/**
 * Http implementation for the {@link GnipFacade}
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class DefaultGnipFacade implements GnipFacade {
    private static final UriStrategy DEFAULT_BASE_URI_STRATEGY = new DefaultUriStrategy();
    private static final UriStrategy POWERTRACKV2_URI_STRATEGY = new PowerTrackV2UriStrategy();

    private final RemoteResourceProvider facade;
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

    /** returns a PowerTrack V1 Facade */
    public static DefaultGnipFacade createPowertrackV1(final GnipAuthentication authentication) {
        return createPowertrackV1(new JRERemoteResourceProvider(authentication));
    }
    
    /** returns a PowerTrack V1 Facade */
    public static DefaultGnipFacade createPowertrackV1(final RemoteResourceProvider facade) {
        return new DefaultGnipFacade(facade, DEFAULT_BASE_URI_STRATEGY);
    }
    
    /** returns a PowerTrack V2 Facade */
    public static DefaultGnipFacade createPowertrackV2(final GnipAuthentication authentication) {
        return createPowertrackV2(new JRERemoteResourceProvider(authentication));
    }
    
    /** returns a PowerTrack V2 Facade */
    public static DefaultGnipFacade createPowertrackV2(final RemoteResourceProvider facade) {
        return new DefaultGnipFacade(facade, POWERTRACKV2_URI_STRATEGY);
    }
    
    public static DefaultGnipFacade createPowertrackV2Compliance(
            final GnipAuthentication authentication, final int partition) {
        return new DefaultGnipFacade(new JRERemoteResourceProvider(authentication), 
                                     new ComplianceV2UriStrategy(partition));
    }
    
    
    /** Creates the HttpGnipFacade. */
    public DefaultGnipFacade(final RemoteResourceProvider facade) {
        this(facade, DEFAULT_BASE_URI_STRATEGY);
    }
    
    @Override
    public PowertrackStreamBuilder createPowertrackStream(final Class clazz) {
        return new PowertrackStreamBuilder() {
            @Override
            protected GnipStream buildStream() {
                final String streamName = String.format(Locale.ENGLISH, "powertrack-%s-%s", this.account, this.type);
                
                final FeedProcessor processor =  new ByLineFeedProcessor<Activity>(
                        streamName, 
                        executorService, 
                        this.observer, 
                        unmarshaller == null ? new JSONActivityUnmarshaller() : unmarshaller);
                final GnipStream stream = createStream(this.account, type, 
                        this.observer, this.executorService, 
                        unmarshaller == null ? new ActivityUnmarshaller(streamName) : unmarshaller, 
                        processor);
                processor.setStream(stream);
                return stream;
            }
        };
    }

    @Override
    public final EDCStreamBuilder createEnterpriceDataCollectorStream() {
        return new EDCStreamBuilder() {
            @Override
            protected GnipStream buildStream() {
                FeedProcessor p;
                final String streamName = String.format(Locale.ENGLISH, "edc-%s-%s", this.account, this.dataCollector);
                
                if(activity) {
                    p = new XMLActivityStreamFeedProcessor(this.account, this.executorService, this.observer, 
                                                           new ActivityUnmarshaller(streamName));
                } else {
                    if(Format.JSON.equals(this.format)) {
                        p = new ByLineFeedProcessor(streamName, this.executorService, this.observer, 
                                unmarshaller);
                    } else if(Format.ATOM.equals(this.format)) {
                        p = new XMLActivityStreamFeedProcessor(streamName, this.executorService, 
                                this.observer, unmarshaller);
                    } else {
                        throw new IllegalArgumentException("Unknown format " + format);
                    }
                }
                final GnipStream stream = createStream(this.account, dataCollector.toString(), 
                        this.observer, this.executorService, this.unmarshaller, 
                        p);
                p.setStream(stream);
                return stream;
            }
        };
    }
    
    
    public final GnipStream createStream(final String account, final String streamName,
            final StreamNotification observer, final ExecutorService executor, final Unmarshaller unmarshaller,
            final FeedProcessor feedProcessor) {
        final DefaultGnipStream stream = createStream(account, streamName, executor);
        stream.open(observer, unmarshaller, feedProcessor);
        GnipStream ret = stream;
        if(useJMX) {
            ret = new GnipStream() {
                @Override
                public String getStreamName() {
                    if(baseUriStrategy instanceof ComplianceV2UriStrategy) {
                        return stream.getStreamName() + ((ComplianceV2UriStrategy)baseUriStrategy).getPartition();
                    } else {
                        return stream.getStreamName();
                    }
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
                public boolean await(final long time, final TimeUnit unit) throws InterruptedException {
                    return stream.await(time, unit);
                }
                
                @Override
                public StreamStats getStreamStats() {
                    return stream.getStreamStats();
                }
            };
            JMXProvider.getProvider().registerBean(ret, stream.getStreamStats());
        }
        return ret;
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

    @Override
    public final void addRule(final String account, final String streamName, final Rule rule) {
        final Rules rules = new Rules();
        rules.getRules().add(rule);
        addRules(account, streamName, rules);
    }
    
    @Override
    public final void addRules(final String account, final String streamName, final Rules rules) {
        facade.postResource(baseUriStrategy.createRulesUri(account, streamName), rules);
    }
    
    @Override
    public final void deleteRule(final String account, final String streamName, final Rule rule) {
        final Rules rules = new Rules();
        rules.getRules().add(rule);
        deleteRules(account, streamName, rules);
    }
    
    @Override
    public final void deleteRules(final String account, final String streamName, final Rules rules) {
    	if (baseUriStrategy.getHttpMethodForRulesDelete().equals(UriStrategy.HTTP_POST)){
    		facade.postResource(baseUriStrategy.createRulesDeleteUri(account, streamName), rules);
    	} else {
    		facade.deleteResource(baseUriStrategy.createRulesDeleteUri(account, streamName), rules);
    	}
    }

    @Override // TODO test
    public RulesValidation validateRules(String account, String streamName, Rules rules) {
        RulesValidation validation = null;
        try {
            URI rulesValidationUri = baseUriStrategy.createRulesValidationUri(account, streamName);
            String responseString = facade.postResource(rulesValidationUri, rules);
            final JsonParser parser = JsonActivityFeedProcessor.getObjectMapper().getJsonFactory().createJsonParser(responseString);
            validation = parser.readValueAs(RulesValidation.class);
        } catch (IOException e) {
            throw new GnipException("Could not validate rules", e);
        }
        return validation;
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
