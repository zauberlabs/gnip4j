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
package com.zaubersoftware.gnip4j.api;

import java.util.concurrent.ExecutorService;

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;

/**
 * Facade to the Gnip Streaming API  
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public interface GnipFacade {

    /** @see #createStream(String, long, StreamNotification, ExecutorService) */
    GnipStream createStream(String domain, long dataCollectorId,
            StreamNotification observer);
    
    /**
     * Gnip provides access to the data with a streaming HTTP implementation
     * that allows you to consume data in near real-time over a single
     * persistent HTTP connection/request.
     * 
     * @param domain The domain name for the power track API. (For example: acme)
     * @param dataCollectorId collector id (id for the data collector)
     * @param observer {@link StreamNotification} used to notify the {@link Activity}s.
     * @param executorService {@link ExecutorService} used to process {@link Activity}
     * @return a reference to the stream so it can be closed 
     */
    GnipStream createStream(String domain, long dataCollectorId,
                            StreamNotification observer, 
                            ExecutorService executorService);
    
    /**
     * Gnip provides a REST interface to the rules configured for each data collector.
     * They can be modified either through this interface, or through the UI. These
     * views are synchronized.
     *
     * @param domain The domain name for the power track API. (For example: acme)
     * @param dataCollectorId collector id (id for the data collector)
     * @return The Rules object for all the rules configured on this tracker.
     */
    Rules getRules(String domain, long dataCollectorId);
    
    /**
     * Gnip provides a REST interface to the rules configured for each data collector.
     * They can be modified either through this interface, or through the UI. These
     * views are synchronized.
     * 
     * Rules should be added one at a time, according to Gnip's documentation on best
     * practices, so this method only lets you change one Rule. It can, of course, be
     * called many times.
     * 
     * @param domain The domain name for the power track API. (For example: acme)
     * @param dataCollectorId collector id (id for the data collector)
     * @param rule The Rule object to add to the tracker.
     */
    void addRule(String domain, long dataCollectorId, Rule rule);
}
