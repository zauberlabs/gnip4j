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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.http.HttpGnipFacade;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class TestDriver {

    @Test
    public void testname() throws Exception {
        final String username = System.getProperty("gnip.username");
        final String password = System.getProperty("gnip.password");
        final String domain = System.getProperty("gnip.domain");
        
        if(username == null) {
            throw new IllegalArgumentException("Missing gnip.username");
        }
        if(password == null) {
            throw new IllegalArgumentException("Missing gnip.password");
        }
        if(domain == null) {
            throw new IllegalArgumentException("Missing gnip.domain");
        }
        
        try {
            final GnipFacade gnip = new HttpGnipFacade();
            
            final GnipAuthentication auth = new InmutableGnipAuthentication(username, password);
            System.out.println("-- Creating stream");
            final GnipStream stream = gnip.createStream(domain, 1, auth);
            final AtomicInteger counter = new AtomicInteger();
            stream.addObserver(new StreamNotification() {
                @Override
                public void notify(final Activity activity, final GnipStream stream) {
                    final int i = counter.getAndIncrement();
                    if (i >= 10) {
                        System.out.println("-- Closing stream.");
                        stream.close();
                    }
                    System.out.println(i + "-" + activity.getBody() + " " + activity.getGnip().getMatchingRules());
                }

                @Override
                public void notifyConnectionError(TransportGnipException e) {
                    // TODO: Auto-generated method stub
                    
                }

                @Override
                public void notifyReConnectionError(GnipException e) {
                    // TODO: Auto-generated method stub
                    
                }

                @Override
                public void notifyReConnection(int attempt, long waitTime) {
                    // TODO: Auto-generated method stub
                    
                }
            });
            System.out.println("-- Awaiting for strem to terminate");
            stream.await();
            System.out.println("-- Shutting down");

        }   catch(Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();
        }
    }
}
