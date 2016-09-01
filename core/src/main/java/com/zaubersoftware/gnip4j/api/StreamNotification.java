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
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Notification from the stream
 * 
 * @author Juan F. Codagnone
 * @since May 4, 2011
 */
public interface StreamNotification<T> {

    /**
     * Notifies the arrival of a new {@link Activity}
     * 
     * @param activity The new arrived activity.
     * @param stream The stream used to suck the activities.
     */
    void notify(T  activity, GnipStream stream);
    
    /**
     * Notifies a connection error.
     * 
     * @param e The exception that cause the error.
     */
    void notifyConnectionError(TransportGnipException e);
    
    /**
     * Notifies an error that occurs while trying to re-establish a connection with Gnip
     * 
     * @param e The exception that cause ther error.
     */
    void notifyReConnectionError(GnipException e);
    
    /**
     * Notifies a re-connection attempt with Gnip. This is called before the waiting time.
     * 
     * @param attempt The amount of re-connection attempts including this notification. Once a reconnection
     * has been established the counter will be reset.
     * @param waitTime The amount of time to be waited before trying to reconnect.
     */
    void notifyReConnectionAttempt(int attempt, long waitTime);

    /**
     * Notifies that we have successfully reconnected to gnip stream.
     * 
     * @param attempts The number of re-connection attempts made before success.
     * @param elapsedDisconnectedTime The elapsed ms between disconnect and successful reconnect.
     */
	void notifyReConnected(int attempts, long elapsedDisconnectedTime);
}
