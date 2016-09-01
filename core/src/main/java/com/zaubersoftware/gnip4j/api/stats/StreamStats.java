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
package com.zaubersoftware.gnip4j.api.stats;


/**
 * Stream stats
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public interface StreamStats {
    /** @return number of activities transfered */
    long getTransferedActivities();
    
    /** @return the date of the last transfered activity */
    long getLastTransferedActivityDate();
    
    /** @return number of bytes transfered throu the channel (after gzip) */
    long getTransferedBytes();
    
    /** @return the date of the last transfered byte */
    long getLastTransferedByteDate();
    
    /** @return of reconnections of the channel */
    int getNumberOfSuccessfulReconnections();
    
    /** @return the date of the last successful reconnect */
    long getLastSuccessfulReconnectionsDate();
    
    /** @return the number of connections attemps */
    int getNumberOfReconnectionsAttempt();
    
    /** @return the date of the last reconnection attempt  */
    long getLastReconnectionsAttemptDate();
    
}
