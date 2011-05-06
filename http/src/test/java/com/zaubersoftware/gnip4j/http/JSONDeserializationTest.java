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

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Before;
import org.junit.Test;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.MatchingRules;


/**
 * Tests the {@link Activity} JSON Deserialization.
 * 
 * @author Guido Marucci Blas
 * @since May 5, 2011
 */
public final class JSONDeserializationTest {

    private Activity activity;
    
    @Before
    public void setUp() throws Exception {
        
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
        "com/zaubersoftware/gnip4j/payload/payload-example.js");
        final ObjectMapper mapper = new ObjectMapper();
        final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        mapper.setDeserializationConfig(
                mapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
        mapper.setSerializationConfig(
                mapper.getSerializationConfig().withAnnotationIntrospector(introspector));
        //TODO This configuration should be removed once the JSON is fully supported
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
        activity = parser.readValueAs(Activity.class);
    }

    @Test
    public void testGetGnip() throws Exception {
        assertNotNull(activity.getGnip());
    }
    
    @Test
    public void testGetLanguage() throws Exception {
        assertNotNull(activity.getGnip().getLanguage());
        assertEquals("en", activity.getGnip().getLanguage().getValue());
    }
    
    @Test
    public void testGetMatchingRules() throws Exception {
        final List<MatchingRules> matchingRules = activity.getGnip().getMatchingRules();
        assertNotNull(matchingRules);
        assertEquals(1, matchingRules.size());
        assertEquals("coke", matchingRules.get(0).getValue());
        assertEquals(null, matchingRules.get(0).getTag());
    }
}
