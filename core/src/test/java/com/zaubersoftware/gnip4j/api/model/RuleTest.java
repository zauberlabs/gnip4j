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
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Tests {@link Rule}
 * 
 * @author Juan F. Codagnone
 * @since Sep 29, 2015
 */
public class RuleTest {

    @Test
    public void testOnlyValueToString() {
        final Rule r = new Rule();
        r.setValue("bar");
        
        assertEquals("{\"value\": \"bar\"}", r.toString());
    }
    
    @Test
    public void testOnlyQuotedValueToString() {
        final Rule r = new Rule();
        r.setValue("b\"ar");
        
        assertEquals("{\"value\": \"b\\\"ar\"}", r.toString());
    }
    
    @Test
    public void testOnlyTagToString() {
        final Rule r = new Rule();
        r.setTag("tag");
        
        assertEquals("{\"tag\": \"tag\"}", r.toString());
    }
    
    @Test
    public void testOnlyQuotedTagToString() {
        final Rule r = new Rule();
        r.setTag("t\"ag");
        
        assertEquals("{\"tag\": \"t\\\"ag\"}", r.toString());
    }
    
    @Test
    public void testOnlyId() {
        final Rule r = new Rule();
        r.setId(1L);
        
        assertEquals("{\"id\": 1}", r.toString());
    }
    
    @Test
    public void testFull() {
        final Rule r = new Rule("foo", "bar", 42L);
        
        assertEquals("{\"tag\": \"bar\", \"value\": \"foo\", \"id\": 42}", r.toString());
    }
    
    
    @Test
    public void testEmptyRule() {
        final Rule r = new Rule();
        
        assertEquals("{}", r.toString());
    }
    
    @Test
    public void testEquals() {
        assertEquals(new Rule(null, null), new Rule(null, null));
        assertEquals(new Rule("a", null),  new Rule("a", null));
        assertEquals(new Rule(null, "a"),  new Rule(null, "a"));
        assertEquals(new Rule("a", "b"),   new Rule("a", "b"));
        assertEquals(new Rule("a", "b", 1L),   new Rule("a", "b", 1L));
    }
    

    @Test
    public void testUnmarshall() throws IOException {
        final ObjectMapper m = new ObjectMapper();
        
        try(final InputStream is = getClass().getClassLoader().getResourceAsStream("com/zaubersoftware/gnip4j/payload/rules.js")) {
            final Rules rules = m.reader(Rules.class).readValue(is);
            assertEquals(1, rules.getRules().size());
            final Rule rule = rules.getRules().get(0);
            assertEquals("foo", rule.getValue());
            assertEquals("f88489e8dc1279583956da7582b94a0c507383fd", rule.getTag());
            assertEquals((Long)794146392408298500L, rule.getId());
        }
    }
    
    
    
}
