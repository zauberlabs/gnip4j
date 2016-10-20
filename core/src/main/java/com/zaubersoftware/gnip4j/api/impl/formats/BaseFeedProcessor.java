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
package com.zaubersoftware.gnip4j.api.impl.formats;

import java.util.concurrent.ExecutorService;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;


/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Juan F. Codagnone
 * @since Dec 13, 2012
 */
public abstract class BaseFeedProcessor<T> implements FeedProcessor {
    protected final String streamName;
    private final ExecutorService activityService;
    private final StreamNotification<T> notification;
    private GnipStream stream;
    
    
    /** Creates the BaseFeedProcessor. */
    public BaseFeedProcessor(final String streamName, final ExecutorService activityService,
            final StreamNotification<T> notification) {
        if(streamName == null) {
            throw new IllegalArgumentException("streamName is null");
        }
        if(activityService == null) {
            throw new IllegalArgumentException("activityService is null");
        }
        if(notification == null) {
            throw new IllegalArgumentException("notification is null");
        }
        

        this.streamName = streamName;
        this.activityService = activityService;
        this.notification = notification;
    }
    
    /** handle an activity */
    protected final void handle(final T activity) {
        activityService.execute(() -> notification.notify(activity, stream));
    }
    
    @Override
    public final void setStream(final GnipStream stream) {
        this.stream = stream;
    }
    
    public final GnipStream getStream() {
        return stream;
    }
}
