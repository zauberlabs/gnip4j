/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.support.logging.slf4j;

import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;
import com.zaubersoftware.gnip4j.api.support.logging.spi.LoggerFactory;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class SLF4JTargetLoggerFactory implements LoggerFactory {

    @Override
    public final Logger getLogger(final Class<?> logger) {
        return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(logger));
    }

}
