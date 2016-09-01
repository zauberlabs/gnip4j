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

import java.util.concurrent.TimeUnit;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.stats.DefaultStreamStats;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Stub implementation of {@link GnipStream} 
 * 
 * @author Guido Marucci Blas
 * @since May 23, 2011
 */
public final class GnipStreamStub implements GnipStream {
    private final Logger logger = LoggerFactory.getLogger(GnipStreamStub.class);
    private final String name;

    /**
     * Creates the GnipStreamStub.
     *
     */
    public GnipStreamStub(final String name) {
        if(name == null) {
            throw new IllegalArgumentException("null name");
        }
        
        this.name = name;
    }
    
    @Override
    public void close() {
        logger.debug("The stream has been closed");
    }

    @Override
    public void await() throws InterruptedException {
        logger.debug("Awaiting for stream termination");
    }

    @Override
    public String getStreamName() {
        return name;
    }

    private final StreamStats stats = new DefaultStreamStats();
    
    @Override
    public StreamStats getStreamStats() {
        return stats;
    }

    @Override
    public boolean await(final long time, final TimeUnit unit) throws InterruptedException {
        logger.debug("Awaiting for stream termination");
        return true;
    }

}
