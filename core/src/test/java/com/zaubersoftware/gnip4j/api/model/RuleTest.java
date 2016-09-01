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
    public void testEmptyRule() {
        final Rule r = new Rule();
        
        assertEquals("{}", r.toString());
    }
}
