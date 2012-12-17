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
package com.zaubersoftware.gnip4j.http;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
import com.zaubersoftware.gnip4j.api.model.Activities;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Geo;
import com.zaubersoftware.gnip4j.api.model.MatchingRules;
import com.zaubersoftware.gnip4j.api.model.Point;


/**
 * Tests the {@link Activity} JSON Deserialization.
 * 
 * @author Guido Marucci Blas
 * @since May 5, 2011
 */
public final class JSONDeserializationTest {
    private ObjectMapper mapper;

    /** setup test */
    @Before
    public void setUp() throws Exception {
        mapper = JsonActivityFeedProcessor.getObjectMapper();
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
            assertEquals(-34.58501869, ((Point)activity.getGeo().getCoordinates()).getLatitude(), 0.001);
            assertEquals(-58.43946468, ((Point)activity.getGeo().getCoordinates()).getLongitude(), 0.001);
        } finally {
            is.close();
        }
    }


    @Test
    public void testDeserializeWithPolygonAndPoint() throws JsonParseException, IOException{
        final InputStream is = getClass().getClassLoader().getResourceAsStream("com/zaubersoftware/gnip4j/payload/deserialize/geolocated-tweets.json");

        try  {
            final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
            final Activities activities = parser.readValueAs(Activities.class);
            
            
            Geo geo1 = activities.getActivities().get(0).getGeo();
            Geo geo2 = activities.getActivities().get(1).getGeo();
            Geo geo3 = activities.getActivities().get(0).getLocation().getGeo();
            Geo geo4 = activities.getActivities().get(1).getLocation().getGeo();
            
            assertNull(geo1);
            assertEquals("lat: 35.11222481 lon: -78.99696934", geo2.getCoordinates().toString());
            assertEquals("[[ lat: -0.5093057 lon: 51.286606 ][ lat: 0.334433 lon: 51.286606 ][ lat: 0.334433 lon: 51.691672 ][ lat: -0.5093057 lon: 51.691672 ]]", geo3.getCoordinates().toString());
            
            assertEquals("[[ lat: -79.058407 lon: 35.106225 ][ lat: -78.944666 lon: 35.106225 ][ lat: -78.944666 lon: 35.177993 ][ lat: -79.058407 lon: 35.177993 ]]", geo4.getCoordinates().toString());
            
        } finally {
            is.close();
        }
    }
    
    /*USE THIS TEST TO TEST ENCODING OF JsonParser
     * Run this test with -Dfile.encoding=UTF-8 and then with another encoding, and compare the file results
     * */
    public void utfDesearilzationTest() throws JsonParseException, IOException{
        InputStream in = getClass().getClassLoader().getResourceAsStream("com/zaubersoftware/gnip4j/payload/deserialize/utf8_tweets.json");
        
        try  {
            final JsonParser parser = mapper.getJsonFactory().createJsonParser(in);
            final Activities activities = parser.readValueAs(Activities.class);
            
            
            String body0 = activities.getActivities().get(0).getBody();
            String body1 = activities.getActivities().get(1).getBody();
            String body2 = activities.getActivities().get(2).getBody();

            FileOutputStream fileOutputStream = new FileOutputStream(new File("tweets"));
            
            fileOutputStream.write(body0.getBytes("UTF-8"));
            fileOutputStream.write(body1.getBytes("UTF-8"));
            fileOutputStream.write(body2.getBytes("UTF-8"));
            
            
        } finally {
            //text.close();
        }
    }
    
    /**
     * tests if the data "model" is serializable
     */
    @Test
    public void testSerializable() throws IOException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
                "com/zaubersoftware/gnip4j/payload/payload-example.js");
        final JsonParser parser = mapper.getJsonFactory().createJsonParser(is);
        final Activity activity = parser.readValueAs(Activity.class);
        final ObjectOutputStream os = new ObjectOutputStream(new ByteArrayOutputStream());
        os.writeObject(activity);
    }
      
}
