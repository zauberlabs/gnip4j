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
package com.zaubersoftware.gnip4j.api.exception;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;


/**
 * Test {@link TransportGnipException} 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class TransportGnipExceptionTest {

    /** test */
    @Test
    public final void testNetwork() {
        assertFalse(new TransportGnipException("x").isNetworkError());
        assertFalse(new TransportGnipException(new IllegalArgumentException()).isNetworkError());
        assertTrue(new TransportGnipException("ioio", new IOException("no network")).isNetworkError());
    }
    
    /** test */
    @Test
    public final void testException() {
        assertNull(new GnipException().getMessage());
    }

    
    /** test */
    @Test
    public final void testAuthenticationGnipException() {
        assertNull(new AuthenticationGnipException().getMessage());
        assertEquals("x", new AuthenticationGnipException("x").getMessage());
        final IOException e = new IOException();
        assertEquals(e, new AuthenticationGnipException(e).getCause());
        assertEquals(e, new AuthenticationGnipException("f", e).getCause());
    }

}
