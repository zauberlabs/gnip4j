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
package com.zaubersoftware.gnip4j.api.impl.formats;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.module.SimpleModule;

import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Geo;
import com.zaubersoftware.gnip4j.api.model.GeoDeserializer;
import com.zaubersoftware.gnip4j.api.model.GeoSerializer;
import com.zaubersoftware.gnip4j.api.stats.ModifiableStreamStats;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;


/** process the powertracke feed */
public class JsonActivityFeedProcessor extends BaseFeedProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final ObjectMapper getObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        
        final SimpleModule gnipActivityModule = new SimpleModule("gnip.activity", new Version(1, 0, 0, null));
        gnipActivityModule.addDeserializer(Geo.class, new GeoDeserializer(Geo.class));
        gnipActivityModule.addSerializer(Geo.class, new GeoSerializer());
        mapper.registerModule(gnipActivityModule);
        
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
        
        return mapper;
    }
    
    /** constructor */
    public JsonActivityFeedProcessor(final String streamName, final ExecutorService activityService,
            final StreamNotification notification) {
        super(streamName, activityService, notification);
    }
    
    @Override
    public final void process(final InputStream is, final ModifiableStreamStats stats) throws IOException {
        final JsonParser parser =  JsonActivityFeedProcessor
                .getObjectMapper().getJsonFactory().createJsonParser(is);

        logger.info("Starting to consume activity stream {} ...", streamName);
        while(!Thread.interrupted()) {
            final Activity activity = parser.readValueAs(Activity.class);
            handle(activity);
            if (activity != null && stats != null){
            	stats.incrementTransferedActivities();
            }
        }
    }
}