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
package com.zaubersoftware.gnip4j.api.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;

import junit.framework.Assert;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class ImmutableGnipAuthenticationTest {

    /** test getters */
    @Test
    public final void getters() {
        final String user = "foo";
        final String pass = "bar";
        
        final GnipAuthentication a  = new ImmutableGnipAuthentication(user, pass);
        assertEquals(user, a.getUsername());
        assertEquals(pass, a.getPassword());
    }
    
    /** test null argument */
    @Test(expected = IllegalArgumentException.class)
    public final void testNullUser() {
        new ImmutableGnipAuthentication(null, "xx");
        Assert.fail();
    }
    
    /** test null argument */
    @Test(expected = IllegalArgumentException.class)
    public final void testNullPass() {
        new ImmutableGnipAuthentication("xx", null);
        Assert.fail();
    }
}
