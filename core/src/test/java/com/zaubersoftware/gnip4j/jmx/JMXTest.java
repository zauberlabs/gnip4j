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
package com.zaubersoftware.gnip4j.jmx;


import java.io.InputStream;
import java.net.URI;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Test JMX
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public final class JMXTest {
    private final StreamNotificationAdapter<Activity> observer = new StreamNotificationAdapter<Activity>() {
        @Override
        public void notify(final Activity activity, final GnipStream stream) {
            
        }
    };
    
    /** test the register and unregister features of JMX */
    @Test
    public void testRegisterAndUnregister() throws Exception {
        final RemoteResourceProvider resources = new RemoteResourceProvider() {
            
            @Override
            public InputStream getResource(final URI uri) throws AuthenticationGnipException,
                    TransportGnipException {
                return getClass().getClassLoader().getResourceAsStream(
                        "com/zaubersoftware/gnip4j/payload/payload-example.js");
            }
            
            @Override
            public <T> T postResource(final URI uri, final Object resource, final Class<T> clazz) {
                return null;
            }

			@Override
			public void deleteResource(final URI uri, final Object resource)
					throws AuthenticationGnipException, TransportGnipException {
				// TODO Auto-generated method stub
				
			}
        };
        final GnipFacade f = new DefaultGnipFacade(resources);
        final GnipStream stream = f.createPowertrackStream(Activity.class)
            .withAccount("acme")
            .withType("twitter")
            .withObserver(observer)
            .build();
        stream.close();
        f.createPowertrackStream(Activity.class)
            .withAccount("acme")
            .withType("twitter")
            .withObserver(observer)
            .build();
        stream.close();
        
    }
}
