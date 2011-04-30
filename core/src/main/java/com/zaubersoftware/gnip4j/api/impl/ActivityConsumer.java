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

import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.Validate;

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
public class ActivityConsumer extends AbstractJobScheduler<Activity> {
    private final ExecutorService executors;
    private GnipStream stream;

    /**
     * Creates the ActivityHandlerJobScheduler.
     *
     * @param queue
     * @param httpGnipStream 
     */
    public ActivityConsumer(final JobQueue<Activity> queue, final ExecutorService executors, final GnipStream stream) {
        super(queue);
        Validate.notNull(executors);
        
        this.executors = executors;
        this.stream = stream;
    }

    @Override
    protected final void doJob(final Activity job) throws InterruptedException {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(job.getVerb());
                stream.close();
            }
        });
    }

    @Override
    protected void doShutdown() {
        executors.shutdownNow();
    }

}
