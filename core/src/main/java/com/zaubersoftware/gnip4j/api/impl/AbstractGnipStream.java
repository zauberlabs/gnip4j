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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.zauber.commons.dao.Closure;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public abstract class AbstractGnipStream implements GnipStream {
    private final Lock lock = new ReentrantLock();
    private final Condition emptyCondition  = lock.newCondition(); 
    protected final AtomicBoolean streamClosed = new AtomicBoolean(false);
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public final void addObserver(final Closure<Activity> closure) {
        // TODO: Auto-generated method stub

    }

    @Override
    public final void close() {
        doClose();
        lock.lock();
        try {
            streamClosed.set(true);
            emptyCondition.signalAll();
        } catch(final Throwable t) {
            logger.error("decrementing active jobs. should not happen ", t);
        } finally {
            lock.unlock();
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
}
