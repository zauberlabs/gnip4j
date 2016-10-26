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

import com.zaubersoftware.gnip4j.api.model.Rule;
import com.zaubersoftware.gnip4j.api.model.Rules;
import com.zaubersoftware.gnip4j.api.model.ruleValidation.RulesValidation;

/**
 * Facade to the Gnip Streaming API  
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public interface GnipFacade {

    /** Let you create an Enterprices Data Collector Stream */
    EDCStreamBuilder createEnterpriceDataCollectorStream();
    
    /** Let you create a Powertrack Stream 
     * @param <T>*/
    <T> PowertrackStreamBuilder<T> createPowertrackStream(Class<T> claszz);
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Gnip provides a REST interface to the rules configured for each data collector.
     * They can be modified either through this interface, or through the UI. These
     * views are synchronized.
     *
     * @param account The account name for the power track API. (For example: acme)
     * @param streamName the streamName
     * @return The Rules object for all the rules configured on this tracker.
     */
    Rules getRules(String account, String streamName);
    
    /**
     * Add a single rule from the Gnip stream
     * 
     * @param account The account name for the power track API. (For example: acme)
     * @param streamName the streamName
     * @param rule The Rule object to add to the tracker.
     */
    void addRule(String account, String streamName, Rule rule);
    
    /**
     * Add a collection of rules to the Gnip stream
     * 
     * @param account The account name for the power track API. (For example: acme)
     * @param streamName the streamName
     * @param rules The Rules object to add to the tracker
     */
    void addRules(String account, String streamName, Rules rules);
    
    /**
     * Delete a single rule from the Gnip stream
     * 
     * @param account The account name for the power track API. (For example: acme)
     * @param streamName the streamName
     * @param rule The Rule object to add to the tracker.
     */
    void deleteRule(String account, String streamName, Rule rule);
    
    /**
     * Delete a collection of rules from the Gnip stream
     * 
     * @param account The account name for the power track API. (For example: acme)
     * @param streamName the streamName
     * @param rules The Rules object to add to the tracker.
     */
    void deleteRules(String account, String streamName, Rules rules);

    /**
     * Validate a collection of rules wrt Gnip's Rule Validation end point.
     *
     * @param account The account name for the power track API.
     * @param streamName the name of the stream.
     * @param rules The rules to validate.
     * @return A response indicating the validity of each input rule.
     */
    RulesValidation validateRules(String account, String streamName, Rules rules);
}
