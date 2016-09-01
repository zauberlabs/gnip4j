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
package com.zaubersoftware.gnip4j.api.support.jmx.sun;

/**
 * MBean implementation
 *  
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public final class StreamStats implements StreamStatsMBean {
    private final com.zaubersoftware.gnip4j.api.stats.StreamStats target;

    /** target */
    public StreamStats(final com.zaubersoftware.gnip4j.api.stats.StreamStats target) {
        this.target = target;
    }
    
    @Override
    public long getTransferedActivities() {
        return target.getTransferedActivities();
    }

    @Override
    public long getLastTransferedActivityDate() {
        return target.getLastTransferedActivityDate();
    }

    @Override
    public long getTransferedBytes() {
        return target.getTransferedBytes();
    }

    @Override
    public long getLastTransferedByteDate() {
        return target.getLastTransferedByteDate();
    }

    @Override
    public int getNumberOfSuccessfulReconnections() {
        return target.getNumberOfSuccessfulReconnections();
    }

    @Override
    public long getLastSuccessfulReconnectionsDate() {
        return target.getLastSuccessfulReconnectionsDate();
    }

    @Override
    public int getNumberOfReconnectionsAttempt() {
        return target.getNumberOfReconnectionsAttempt();
    }

    @Override
    public long getLastReconnectionsAttemptDate() {
        return target.getLastReconnectionsAttemptDate();
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return target.equals(obj);
    }

    @Override
    public String toString() {
        return target.toString();
    }
};
