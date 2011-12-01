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
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.DefaultGnipStream;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.MatchingRules;


/**
 * Tests the {@link Activity} JSON Deserialization.
 * 
 * @author Guido Marucci Blas
 * @since May 5, 2011
 */
public final class JSONDeserializationTest {
    private ObjectMapper mapper;
    private JAXBContext ctx; 

    /** setup test */
    @Before
    public void setUp() throws Exception {
        
        mapper = DefaultGnipStream.getObjectMapper();
        ctx = JAXBContext.newInstance(Activity.class.getPackage().getName());
    }

    /** test a complete unmarshal from the json */
    @Test
    public void testGetGnip() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
        "com/zaubersoftware/gnip4j/payload/payload-example.js");
        try  {
            final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
            final Activity activity = parser.readValueAs(Activity.class);
            
            assertNotNull(activity.getGnip());
            assertNotNull(activity.getGnip().getLanguage());
            assertEquals("en", activity.getGnip().getLanguage().getValue());
            final List<MatchingRules> matchingRules = activity.getGnip().getMatchingRules();
            assertNotNull(matchingRules);
            assertEquals(1, matchingRules.size());
            assertEquals("coke", matchingRules.get(0).getValue());
            assertEquals(null, matchingRules.get(0).getTag());
        } finally {
            is.close();
        }
    }

    /** test a complete unmarshal from the json */
    @Test
    public void testGeoCoordinates() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
        "com/zaubersoftware/gnip4j/payload/payload-example-geo.json");
        try  {
            final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
            final Activity activity = parser.readValueAs(Activity.class);

            assertNotNull(activity.getGeo());
            assertNotNull(activity.getGeo().getCoordinates());
            assertEquals(-34.58501869, activity.getGeo().getCoordinates()[0], 0.001);
            assertEquals(-58.43946468, activity.getGeo().getCoordinates()[1], 0.001);
        } finally {
            is.close();
        }
    }

    /** regression test for a NPE exception */
    @Test
    public void testNPE() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
            "com/zaubersoftware/gnip4j/payload/payload-twitter-entities.js");
        final InputStream expectedIs = getClass().getClassLoader().getResourceAsStream(
            "com/zaubersoftware/gnip4j/payload/payload-twitter-entities.xml");
        
        try {
            final String json = IOUtils.toString(is);
            final JsonParser parser = mapper.getJsonFactory().createJsonParser(json);
            final Activity activity = parser.readValueAs(Activity.class);
            final StringWriter w = new StringWriter();
            mapper.getJsonFactory().createJsonGenerator(w).writeObject(activity);
            
            final Marshaller o = ctx.createMarshaller();
            o.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter ww = new StringWriter();
            o.marshal(activity, ww);
            assertEquals(removeTimeZoneFields(IOUtils.toString(expectedIs)), removeTimeZoneFields(ww.toString()));
        } finally {
            is.close();
        }
    }

    private String removeTimeZoneFields(String input) {
        return input.replaceAll("postedTime=\"[\\d-\\+T:\\.]*\"", "");
    }

}
