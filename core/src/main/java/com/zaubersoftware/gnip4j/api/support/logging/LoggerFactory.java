/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.support.logging;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import com.zaubersoftware.gnip4j.api.support.logging.nil.NilLoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.slf4j.SLF4JTargetLoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public final class LoggerFactory {
    private static com.zaubersoftware.gnip4j.api.support.logging.spi.LoggerFactory targetLoggerFactory;
    
    /** constructor */
    private LoggerFactory() {
        // utility class
    }
    
    
    static {
        targetLoggerFactory = getLoggerImplementation();
    }
    

    /** @return an implemetation for logging  */
    private static com.zaubersoftware.gnip4j.api.support.logging.spi.LoggerFactory getLoggerImplementation() {
        try {
            boolean hasSlf4 = false;
            final Enumeration<URL> resources = LoggerFactory.class
                    .getClassLoader().getResources("org/slf4j/Logger.class");
            while(resources.hasMoreElements()) {
                resources.nextElement();
                hasSlf4 = true;
            }
            
            if(hasSlf4) {
                targetLoggerFactory = new SLF4JTargetLoggerFactory();
            } else {
                targetLoggerFactory = new NilLoggerFactory();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return targetLoggerFactory;
    }
    
    /**
     * Return a logger named corresponding to the class passed as parameter, using
     * the statically bound {@link ILoggerFactory} instance.
     * 
     * @param clazz the returned logger will be named after clazz
     * @return logger
     */
    public static Logger getLogger(final Class<?> clazz) {
        return targetLoggerFactory.getLogger(clazz);
    }

}
