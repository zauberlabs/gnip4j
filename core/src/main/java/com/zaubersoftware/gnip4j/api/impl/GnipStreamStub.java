/**
 * Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;

/**
 * Stub implementation of {@link GnipStream} 
 * 
 * @author Guido Marucci Blas
 * @since May 23, 2011
 */
public final class GnipStreamStub implements GnipStream {

    private final Logger logger = LoggerFactory.getLogger(GnipStreamStub.class);
    
    @Override
    public void addObserver(final StreamNotification notification) {
        logger.debug("A new StreamNotification has been added");
    }

    @Override
    public void open() {
        logger.debug("The Stream has been opened");
    }

    @Override
    public void openAndAwait() throws InterruptedException {
        logger.debug("The Stream has been opened. Now waiting for termination");
    }

    @Override
    public void close() {
        logger.debug("The stream has been closed");
    }

    @Override
    public void await() throws InterruptedException {
        logger.debug("Awaiting for stream termination");
    }

}
