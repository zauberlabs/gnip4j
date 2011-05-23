/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.api.support.logging.nil;

import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;
import com.zaubersoftware.gnip4j.api.support.logging.spi.LoggerFactory;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class NilLoggerFactory implements LoggerFactory {
    private static Logger logger = new Logger() {

        @Override
        public void warn(final String msg, final Throwable t) {
            // void
        }

        @Override
        public void warn(final String format, final Object... argArray) {
            // void

        }

        @Override
        public void warn(final String msg) {
            // void

        }

        @Override
        public void trace(final String msg, final Throwable t) {
            // void

        }

        @Override
        public void trace(final String format, final Object... argArray) {
            // void

        }

        @Override
        public void trace(final String msg) {
            // void

        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void info(final String msg, final Throwable t) {
            // void

        }

        @Override
        public void info(final String format, final Object... argArray) {
            // void

        }

        @Override
        public void info(final String msg) {
            // void

        }

        @Override
        public void error(final String msg, final Throwable t) {
            // void

        }

        @Override
        public void error(final String format, final Object... argArray) {
            // void

        }

        @Override
        public void error(final String msg) {
            // void

        }

        @Override
        public void debug(final String msg, final Throwable t) {
            // void

        }

        @Override
        public void debug(final String format, final Object... argArray) {
            // void

        }

        @Override
        public void debug(final String msg) {
            // void

        }
    };
    @Override
    public final Logger getLogger(final Class<?> l) {
        return logger;
    }

}
