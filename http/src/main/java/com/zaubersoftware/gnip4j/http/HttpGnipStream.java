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
package com.zaubersoftware.gnip4j.http;

import static ar.com.zauber.leviathan.common.async.ThreadUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.UnhandledException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import ar.com.zauber.commons.validate.Validate;
import ar.com.zauber.leviathan.common.async.AbstractJobScheduler;
import ar.com.zauber.leviathan.common.async.JobQueue;
import ar.com.zauber.leviathan.common.async.impl.BlockingQueueJobQueue;

import com.zaubersoftware.gnip4j.api.impl.AbstractGnipStream;
import com.zaubersoftware.gnip4j.api.impl.ActivityConsumer;
import com.zaubersoftware.gnip4j.api.impl.JsonConsumer;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Implementation acording   
 * http://docs.gnip.com/w/page/23724581/Gnip-Full-Documentation#streaminghttp
 * 
 * <verbatim>
 *                                               Json
 *   +----------+   HTTP   +------------------+ (String) +--------------+        +-----------------+
 *   | data     | -------> | GnipHttpConsumer |-----+--> | JsonConsumer |----+   | ActivityConsumer|
 *   | colector |          +------------------+     |    +--------------+    |   +-----------------+
 *   + ---------+                                   |         .....          +-->     ....
 *                                                  |    +--------------+    |   +-----------------+
 *                                                  +--> | JsonConsumer |----+   | ActivityConsumer|
 *                                                       +--------------+        +-----------------+
 * </verbatim>
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class HttpGnipStream extends AbstractGnipStream {
    private final JobQueue<String> jsonQueue = new BlockingQueueJobQueue<String>(new LinkedBlockingQueue<String>());
    private final JobQueue<Activity> activityQueue = new BlockingQueueJobQueue<Activity>(new LinkedBlockingQueue<Activity>());
    private final Thread httpThread;
    private final Thread jsonThread;
    private final Thread processThread;
    private final AbstractJobScheduler<String> stringScheduler = new JsonConsumer(jsonQueue, activityQueue);
    private final AbstractJobScheduler<Activity> activityScheduler = new ActivityConsumer(activityQueue, Executors.newScheduledThreadPool(10), this);
    
    private final HttpResponse response;
    
    /**
     * Creates the HttpGnipStream.
     *
     * @param auth
     */
    public HttpGnipStream(final HttpResponse response) {
        Validate.notNull(response, "response is null");

        this.response = response;

        httpThread = new Thread(new GnipHttpConsumer(), "consumer-http");
        jsonThread = new Thread(stringScheduler, "consumer-json");
        processThread = new Thread(activityScheduler, "consumer-activity");
        
        httpThread.start();
        jsonThread.start();
        processThread.start();
    }

    private class GnipHttpConsumer implements Runnable {

        @Override
        public void run() {
            HttpEntity entity = null;
            BufferedReader is = null;
            
            try {
                entity = response.getEntity();
                if (entity != null) {
                    is = new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8"));
                    String json;
                    while((json = is.readLine()) != null) {
                        jsonQueue.add(json);
                    }
                }            
            } catch(InterruptedException e) {
                throw new UnhandledException(e);
            } catch(IOException e) {
                throw new UnhandledException(e);
            } finally {
                try {
                    if(is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                } finally {
                    try {
                        if(entity != null) {
                            try {
                                EntityUtils.consume(entity);
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                    } finally {
//                        close();
                    }
                }
            }
        }
    }
 
    @Override
    protected void doClose() {
        // no aceptamos más trabajos.
        
        
        httpThread.interrupt();
        jsonThread.interrupt();
        processThread.interrupt();
        
        waitForTermination(httpThread);
        jsonQueue.shutdown();
        waitForTermination(jsonThread);
        activityQueue.shutdown();
        waitForTermination(processThread);
    }
}
