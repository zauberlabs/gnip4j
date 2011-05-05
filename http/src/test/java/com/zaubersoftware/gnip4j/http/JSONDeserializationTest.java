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

import java.io.StringReader;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.Before;
import org.junit.Test;

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
//        final InputStream is = getClass().getClassLoader().getResourceAsStream(
//        "/gnip4j-http/src/test/resources/com/zaubersoftware/gnip4j/payload/twitter-payload-example.js");
//        
        final StringReader is = new StringReader("{\"id\":\"tag:search.twitter.com,2005:66216309981061120\",\"inReplyTo\":{\"link\":\"http://twitter.com/heirtoblair/statuses/66216007370424320\"},\"body\":\"@heirtoblair Seriously? That program kicks my ass. All for a coke? You go girl!\",\"verb\":\"post\",\"link\":\"http://twitter.com/lotsOspermies/statuses/66216309981061120\",\"generator\":{\"link\":\"http://twitter.com\",\"displayName\":\"web\"},\"postedTime\":\"2011-05-05T19:02:53.000Z\",\"provider\":{\"link\":\"http://www.twitter.com\",\"displayName\":\"Twitter\",\"objectType\":\"service\"},\"object\":{\"summary\":\"@heirtoblair Seriously? That program kicks my ass. All for a coke? You go girl!\",\"id\":\"object:search.twitter.com,2005:66216309981061120\",\"link\":\"http://twitter.com/lotsOspermies/statuses/66216309981061120\",\"postedTime\":\"2011-05-05T19:02:53.000Z\",\"objectType\":\"note\"},\"actor\":{\"summary\":\"My vag will never be the same after spawning 3 boys! I'm a wife, PPD ass kickah, blogger, snarky biznitch & a wanna be SAHM... \",\"friendsCount\":331,\"location\":{\"displayName\":\"Florida\",\"objectType\":\"place\"},\"link\":\"http://www.twitter.com/lotsOspermies\",\"postedTime\":\"2010-06-17T14:34:03.000Z\",\"image\":\"http://a1.twimg.com/profile_images/1336079729/me_normal.jpg\",\"links\":[{\"rel\":\"me\",\"href\":\"http://www.spermiestyle.com/\"}],\"listedCount\":40,\"id\":\"id:twitter.com:156653884\",\"languages\":[\"en\"],\"followersCount\":557,\"utcOffset\":\"-18000\",\"preferredUsername\":\"lotsOspermies\",\"displayName\":\"Pamela Gold \",\"statusesCount\":4554,\"objectType\":\"person\"},\"twitter_entities\":{\"urls\":[],\"user_mentions\":[{\"indices\":[0,12],\"screen_name\":\"heirtoblair\",\"id_str\":\"19663219\",\"name\":\"Heir to Blair Blog\",\"id\":19663219}],\"hashtags\":[]},\"objectType\":\"activity\",\"gnip\":{\"language\":{\"value\":\"en\"},\"matching_rules\":[{\"value\":\"coke\",\"tag\":null}]}}\n");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        mapper.setDeserializationConfig(
                mapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
        mapper.setSerializationConfig(
                mapper.getSerializationConfig().withAnnotationIntrospector(introspector));
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
