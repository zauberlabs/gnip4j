/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;


/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Juan F. Codagnone
 * @since Dec 13, 2012
 */
public abstract class BaseFeedProcessor implements FeedProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected final String streamName;
    private final ExecutorService activityService;
    private final StreamNotification notification;
    private final GnipStream stream;
    
    
    /** Creates the BaseFeedProcessor. */
    public BaseFeedProcessor(final String streamName, final ExecutorService activityService,
            final StreamNotification notification, final GnipStream stream) {
        if(streamName == null) {
            throw new IllegalArgumentException("streamName is null");
        }
        if(activityService == null) {
            throw new IllegalArgumentException("activityService is null");
        }
        if(notification == null) {
            throw new IllegalArgumentException("notification is null");
        }
        if(stream == null) {
            throw new IllegalArgumentException("stream is null");
        }
        

        this.streamName = streamName;
        this.activityService = activityService;
        this.notification = notification;
        this.stream = stream;
    }
    
    /** handle an activity */
    protected final void handle(final Activity activity) {
        if (activity == null) {
            logger.warn("Activity parsed from stream {} is null. Should not happen!",
                    streamName);
            return;
        }
        if (activity.getBody() == null && activity.getObject() == null) {
            logger.warn("{}: Activity with id {} and link {} has a null body",
                    new Object[]{streamName, activity.getId(), activity.getLink()});
        }
        logger.trace("{}: Notifying activity {}", streamName, activity.getBody());
        activityService.execute(new Runnable() {
            @Override
            public void run() {
                notification.notify(activity, stream);
            }
        });
    }
}
