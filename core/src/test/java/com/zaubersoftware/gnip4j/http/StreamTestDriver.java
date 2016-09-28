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
package com.zaubersoftware.gnip4j.http;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

/**
 * Test Driver that tests the flows using a real connections 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public final class StreamTestDriver {

    /**
     * Entry point for the test driver
     * 
     * @throws Exception
     */
    @Test
    public void testname() throws Exception {
        final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        final String account = System.getProperty("gnip.account");
        final String streamName = System.getProperty("gnip.stream");
        
        if(username == null) {
            throw new IllegalArgumentException("Missing gnip.username");
        }
        if(password == null) {
            throw new IllegalArgumentException("Missing gnip.password");
        }
        if(account == null) {
            throw new IllegalArgumentException("Missing gnip.account");
        }
        if(streamName == null) {
            throw new IllegalArgumentException("Missing gnip.stream");
        }
        
        try {
            final GnipFacade gnip = new DefaultGnipFacade(new JRERemoteResourceProvider(new ImmutableGnipAuthentication(username, password)));
            
            System.out.println("-- Creating stream");
            final AtomicInteger counter = new AtomicInteger();
            final StreamNotificationAdapter<Activity> n = new StreamNotificationAdapter<Activity>() {
                @Override
                public void notify(final Activity activity, final GnipStream stream) {
                    final int i = counter.getAndIncrement();
                    if (i >= 10) {
                        System.out.println("-- Closing stream.");
                        stream.close();
                    }
                    System.out.println(i + "-" + activity.getBody() + " " + activity.getGnip().getMatchingRules());
                }
            };
            
            final GnipStream stream = gnip.createPowertrackStream(Activity.class)
                                        .withAccount(account)
                                        .withType(streamName)
                                        .withObserver(n)
                                        .build();
            System.out.println("-- Awaiting for stream to terminate");
            stream.await();
            System.out.println("-- Shutting down");

        }   catch(final Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();
        }
    }
}
