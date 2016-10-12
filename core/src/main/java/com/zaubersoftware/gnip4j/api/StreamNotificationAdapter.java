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
package com.zaubersoftware.gnip4j.api;

import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;

/**
 * An adapter for {@link StreamNotification} that implements all the method
 * except for {@link StreamNotification#notify(Object, GnipStream)} by doing 
 * nothing.
 * 
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public abstract class StreamNotificationAdapter<T> implements StreamNotification<T> {

    @Override
    public void notifyConnectionError(final TransportGnipException e) {
        // Do nothing
    }

    @Override
    public void notifyReConnectionError(final GnipException e) {
        // Do nothing
    }

    @Override
    public void notifyReConnectionAttempt(final int attempt, final long waitTime) {
        // Do nothing
    }
    
    @Override
    public void notifyReConnected(final int attempts, final long elapsedDisconnectedTime){
    	// Do nothing
    }

}
