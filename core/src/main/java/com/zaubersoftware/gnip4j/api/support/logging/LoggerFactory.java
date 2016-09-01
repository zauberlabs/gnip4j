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
package com.zaubersoftware.gnip4j.api.support.logging;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.ILoggerFactory;

import com.zaubersoftware.gnip4j.api.support.logging.nil.NilLoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.slf4j.SLF4JTargetLoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Decides what SPI implementation is used for logging.
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
        } catch (final IOException e) {
            throw new RuntimeException(e);
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
