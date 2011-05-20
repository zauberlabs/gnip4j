/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.impl;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class InmutableGnipAuthenticationTest {

    /** test getters */
    @Test
    public final void getters() {
        final String user = "foo";
        final String pass = "bar";
        
        final GnipAuthentication a  = new InmutableGnipAuthentication(user, pass);
        assertEquals(user, a.getUsername());
        assertEquals(pass, a.getPassword());
    }
    
    /** test null argument */
    @Test(expected = IllegalArgumentException.class)
    public final void testNullUser() {
        new InmutableGnipAuthentication(null, "xx");
        Assert.fail();
    }
    
    /** test null argument */
    @Test(expected = IllegalArgumentException.class)
    public final void testNullPass() {
        new InmutableGnipAuthentication("xx", null);
        Assert.fail();
    }
}
