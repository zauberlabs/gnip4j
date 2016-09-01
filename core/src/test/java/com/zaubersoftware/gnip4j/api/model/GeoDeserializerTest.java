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
package com.zaubersoftware.gnip4j.api.model;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Juan F. Codagnone
 * @since Jul 24, 2015
 */
public class GeoDeserializerTest {
    private static final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();

    @Test
    public void testLocation() throws JsonParseException, JsonMappingException, IOException {
        String x = "{\"twitter_place_type\":\"city\",\"geo\":{\"coordinates\":[[[-58.5317922,-34.674453],[-58.5317922,-34.534177],[-58.353494,-34.534177],[-58.353494,-34.674453]]],\"type\":\"Polygon\"},\"link\":\"https://api.twitter.com/1.1/geo/id/018f1cde6bad9747.json\",\"twitter_country_code\":\"AR\",\"country_code\":\"Argentina\",\"name\":\"Ciudad Autónoma de Buenos Aires\",\"displayName\":\"Ciudad Autónoma de Buenos Aires, Argentina\",\"objectType\":\"place\"}";
        
        final Activity.Location l = JsonActivityFeedProcessor.getObjectMapper().readValue(x, Activity.Location.class);
        assertEquals("place", l.getObjectType());
        assertEquals("Ciudad Autónoma de Buenos Aires, Argentina", l.getDisplayName());
        assertEquals("Ciudad Autónoma de Buenos Aires", l.getName());
        assertEquals("Argentina", l.getCountryCode());
        assertEquals("AR", l.getTwitterCountryCode());
        assertEquals("https://api.twitter.com/1.1/geo/id/018f1cde6bad9747.json", l.getLink());
        assertEquals("city", l.getTwitterPlaceType());
        
        final Geo geo = l.getGeo();
        assertEquals("Polygon", geo.getType());
        
        final Polygon expected = new Polygon(new LinearRing(
            new Point(-58.5317922, -34.674453), 
            new Point(-58.5317922, -34.534177), 
            new Point(-58.353494, -34.534177),
            new Point(-58.353494, -34.674453)
        ));
        
        assertEquals(expected, geo.getCoordinates());
        x = mapper.writeValueAsString(geo);
        assertEquals(geo, mapper.readValue(x, Geo.class));
    }

    @Test
    public void testPoint() throws JsonParseException, JsonMappingException, IOException {
        String x = "{\"coordinates\":[-34.635439,-58.427005],\"type\":\"Point\"}";
        
        final Geo geo = mapper.readValue(x, Geo.class);
        
        final Geo expected = new Geo("Point", new Point(-34.635439,-58.427005));
        assertEquals(expected, geo);
        
        x = mapper.writeValueAsString(geo);
        assertEquals(expected, mapper.readValue(x, Geo.class));
    }
}
