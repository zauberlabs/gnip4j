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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;

import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.stats.ModifiableStreamStats;

/**
 * Custom feed processor   
 * 
 * @author Juan F. Codagnone
 * @since Oct 3, 2013
 * @param <T> param
 */
public class ByLineFeedProcessor<T> extends BaseFeedProcessor<T> {
    private final Unmarshaller<T> unmarshaller;

    /** Creates the ByLineFeedProcessor. */
    public ByLineFeedProcessor(final String streamName, final ExecutorService activityService,
            final StreamNotification<T> notification, final Unmarshaller<T> unmarshaller) {
        super(streamName, activityService, notification);
        this.unmarshaller = unmarshaller;
    }
    
    @Override
    public final void process(final InputStream is, final ModifiableStreamStats stats) throws IOException, ParseException {
        // hack
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String s = null;

        while ((s = reader.readLine()) != null) {
            if(!s.isEmpty()) {
                handle(unmarshaller.unmarshall(s));
                if (stats != null){
                	stats.incrementTransferedActivities();
                }
            }
            if(Thread.interrupted()) {
                break;
            }
        }
    }
}
