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
import static com.zaubersoftware.gnip4j.http.ErrorCodes.*;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;

/**
 * Tests input validations
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class ValidationTest {

    /** test */
    @Test
    public final void facadeNullClient() {
        try {
            new HttpGnipFacade(null);
            fail();
        } catch(IllegalArgumentException e) {
            assertEquals(ERROR_NULL_HTTPCLIENT, e.getMessage());
        }
    }
    
    /** test */
    @Test
    public final void streamNullClient() {
        try {
            new HttpGnipStream(null, "x", 12L, new InmutableGnipAuthentication("foo", "bar"));
            fail();
        } catch(IllegalArgumentException e) {
            assertEquals(ERROR_NULL_HTTPCLIENT, e.getMessage());
        }
    }
    

    /** test */
    @Test
    public final void streamEmptyDomain() {
        try {
            new HttpGnipStream(new DefaultHttpClient(), " \t", 12L, new InmutableGnipAuthentication("foo", "bar"));
            fail();
        } catch(IllegalArgumentException e) {
            assertEquals(ERROR_EMPTY_DOMAIN, e.getMessage());
        }
    }
    

    /** test */
    @Test
    public final void streamNullAuth() {
        try {
            new HttpGnipStream(new DefaultHttpClient(), "f", 12L, null);
            fail();
        } catch(IllegalArgumentException e) {
            assertEquals(ERROR_NULL_AUTH, e.getMessage());
        }
    }
}
