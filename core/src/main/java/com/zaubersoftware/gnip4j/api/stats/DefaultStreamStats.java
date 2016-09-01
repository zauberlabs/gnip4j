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
 * POJO implementation for {@link StreamStats}.
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public class DefaultStreamStats implements ModifiableStreamStats {
    private long transferedBytes;
    private long transferedBytesDate;
    
    private long transferedActivities;
    private long transferedActivitiesDate;
    
    private int numberOfSucessfulReconnections;
    private long sucessfulReconnectionsDate;
    
    private int numberOfReconnections;
    private long reconnectionDate;
    
    @Override
    public final long getTransferedBytes() {
        return transferedBytes;
    }

    @Override
    public final long getTransferedActivities() {
        return transferedActivities;
    }

    public final int getNumberOfSucessfulReconnections() {
        return numberOfSucessfulReconnections;
    }
    
    @Override
    public final int getNumberOfReconnectionsAttempt() {
        return numberOfReconnections;
    }
    
    @Override
    public final void incrementNumberOfSuccessfulReconnections() {
        numberOfSucessfulReconnections++;
        sucessfulReconnectionsDate = System.currentTimeMillis();
    }
    
    @Override
    public final void incrementTransferedActivities() {
        transferedActivities++;
        transferedActivitiesDate = System.currentTimeMillis();
    }
    
    @Override
    public final void incrementTransferedBytes() {
        transferedBytes++;
        transferedBytesDate = System.currentTimeMillis();
    }

    @Override
    public final int getNumberOfSuccessfulReconnections() {
        return numberOfSucessfulReconnections;
    }

    @Override
    public final void incrementNumberOfReconnectionsAttempt() {
        numberOfReconnections++;
        reconnectionDate = System.currentTimeMillis();
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("transferedBytes = ");
        sb.append(transferedBytes);
        sb.append("\ntransferedActivities = ");
        sb.append(transferedActivities);
        sb.append("\nnumberOfSucessfulReconnections = ");
        sb.append(numberOfSucessfulReconnections);
        sb.append("\nnumberOfReconnections = ");
        sb.append(numberOfReconnections);
        return sb.toString();
    }

    @Override
    public final long getLastTransferedActivityDate() {
        return transferedActivitiesDate;
    }

    @Override
    public final long getLastTransferedByteDate() {
        return transferedBytesDate;
    }

    @Override
    public final long getLastSuccessfulReconnectionsDate() {
        return sucessfulReconnectionsDate;
    }

    @Override
    public final long getLastReconnectionsAttemptDate() {
        return reconnectionDate;
    }
}
