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

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
/**
 * Http implementation for the {@link GnipFacade}  
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class DefaultGnipFacade implements GnipFacade {
    private final RemoteResourceProvider facade;
    private int streamDefaultWorkers = 10;
    
    /** Creates the HttpGnipFacade. */
    public DefaultGnipFacade(final RemoteResourceProvider facade) {
        if(facade == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        this.facade = facade; 
    }

    
    @Override
    public final GnipStream createStream(
            final String domain,
            final long dataCollectorId,
            final StreamNotification observer) {
        final ExecutorService executor = Executors.newFixedThreadPool(streamDefaultWorkers);
        final GnipStream target = createStream(domain, dataCollectorId, observer, executor);
        
        return new GnipStream() {
            @Override
            public void close() {
                try {
                    target.close();
                } finally {
                    executor.shutdown();
                }
            }
            
            @Override
            public void await() throws InterruptedException {
                target.await();
            }
        }; 
    }
    
    @Override
    public final GnipStream createStream(final String domain, final long dataCollectorId,
            final StreamNotification observer, final ExecutorService executor) {
        final DefaultGnipStream stream = new DefaultGnipStream(facade, domain, dataCollectorId, 
                executor);
        stream.open(observer);
        return stream;
    }

    
    public final int getStreamDefaultWorkers() {
        return streamDefaultWorkers;
    }
    
    /** @see #getStreamDefaultWorkers() */
    public final void setStreamDefaultWorkers(final int streamDefaultWorkers) {
        if(streamDefaultWorkers < 1) {
            throw new IllegalArgumentException("Must be >= 1");
        }
        this.streamDefaultWorkers = streamDefaultWorkers;
    }
}
