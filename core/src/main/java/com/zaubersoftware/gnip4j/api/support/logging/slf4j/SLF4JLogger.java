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
package com.zaubersoftware.gnip4j.api.support.logging.slf4j;

import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * SLF4J implementation
 *
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public final class SLF4JLogger implements Logger {
    private final org.slf4j.Logger target;

    /** constructor */
    public SLF4JLogger(final org.slf4j.Logger target) {
        if(target == null) {
            throw new IllegalArgumentException("target is null");
        }
        this.target = target;
    }

    @Override
    public boolean isTraceEnabled() {
        return target.isTraceEnabled();
    }

    @Override
    public void trace(final String msg) {
        target.trace(msg);
    }

    @Override
    public void trace(final String format, final Object... argArray) {
        target.trace(format, argArray);
    }

    @Override
    public void trace(final String msg, final Throwable t) {
        target.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return target.isDebugEnabled();
    }

    @Override
    public void debug(final String msg) {
        target.debug(msg);
    }

    @Override
    public void debug(final String format, final Object... argArray) {
        target.debug(format, argArray);
    }

    @Override
    public void debug(final String msg, final Throwable t) {
        target.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return target.isInfoEnabled();
    }

    @Override
    public void info(final String msg) {
        target.info(msg);
    }

    @Override
    public void info(final String format, final Object... argArray) {
        target.info(format, argArray);
    }

    @Override
    public void info(final String msg, final Throwable t) {
        target.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return target.isWarnEnabled();
    }

    @Override
    public void warn(final String msg) {
        target.warn(msg);
    }

    @Override
    public void warn(final String format, final Object... argArray) {
        target.warn(format, argArray);
    }

    @Override
    public void warn(final String msg, final Throwable t) {
        target.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return target.isErrorEnabled();
    }

    @Override
    public void error(final String msg) {
        target.error(msg);

    }

    @Override
    public void error(final String format, final Object... argArray) {
        target.error(format, argArray);
    }

    @Override
    public void error(final String msg, final Throwable t) {
        target.error(msg, t);
    }
}
