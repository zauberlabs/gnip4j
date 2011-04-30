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

import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.zauber.commons.validate.Validate;
import ar.com.zauber.leviathan.common.async.AbstractJobScheduler;
import ar.com.zauber.leviathan.common.async.JobQueue;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class JsonConsumer extends AbstractJobScheduler<String> {
    private JobQueue<Activity> activityQueue;

    /**
     * Creates the JsonToDomainJobScheduler.
     *
     * @param queue
     */
    public JsonConsumer(final JobQueue<String> jsonQueue, final JobQueue<Activity> activityQueue) {
        super(jsonQueue);
        Validate.notNull(activityQueue);
        
        this.activityQueue = activityQueue;
    }

    @Override
    protected final void doJob(final String json) throws InterruptedException {
        final Activity activity = new Activity();
        activity.setVerb(json);
        activityQueue.add(activity);
    }

    @Override
    protected void doShutdown() {
        // nothing to do
    }

}
