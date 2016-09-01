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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Abstract skeleton implementation of the {@link GnipStream} interface.
 * This is the base class of all the {@link GnipStream} implementations.
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public abstract class AbstractGnipStream implements GnipStream {
    private final Lock lock = new ReentrantLock();
    private final Condition emptyCondition  = lock.newCondition(); 
    private final AtomicBoolean streamClosed = new AtomicBoolean(false);
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public final void close() {
        if(!streamClosed.getAndSet(true)) {
            try {
                doClose();
            } finally {
                lock.lock();
                try { 
                    emptyCondition.signalAll();
                } catch(final Throwable t) {
                    logger.error("decrementing active jobs. should not happen ", t);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**  template method for close */
    protected void doClose() {
        
    }

    @Override
    public final void await() throws InterruptedException {
        lock.lock();
        try {
            while(!streamClosed.get()) {
                emptyCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final boolean await(final long time, final TimeUnit unit) throws InterruptedException {
        boolean ret = false;
        lock.lock();
        try {
            emptyCondition.await(time, unit);
            ret = streamClosed.get();
        } finally {
            lock.unlock();
        }
        return ret;
    }
    
    /** @return the stream name. Used for tracing propourses */
    @Override
    public abstract String getStreamName(); 
}
