/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.support.logging.spi;


/**
 * {@link LoggerFactory} implementation
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public interface LoggerFactory {

    /** @return a logger */
    Logger getLogger(Class<?> logger);
}
