/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.support.logging;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class LoggerFactoryTest {

    @Test
    public void testname() throws Exception {
        LoggerFactory.getLogger(getClass()).info("ghlas");
    }
}
