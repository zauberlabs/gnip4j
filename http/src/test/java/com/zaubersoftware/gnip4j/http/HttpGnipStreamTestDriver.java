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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;


/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public final class HttpGnipStreamTestDriver {

    @Test
    public void testReConnection() throws Exception {
        final InputStream instream = new ActivityNetworkExceptionInputStream(
        getClass().getClassLoader().getResourceAsStream(
        "com/zaubersoftware/gnip4j/payload/payload-example.js"), 2);
        final GnipAuthentication auth = new InmutableGnipAuthentication("test", "test");
        final DefaultHttpClient client = new DefaultHttpClient();
        final StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "");
        final HttpEntity entity = new InputStreamEntity(instream, 10000);
        final HttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        
        final MockHttpGnipStream stream = new MockHttpGnipStream(client, "test", 1, auth, response);
        stream.addObserver(new StreamNotification() {
            @Override
            public void notifyReConnectionError(final GnipException e) {
                System.out.println(String.format("ReConnectionError: %s", e.getMessage()));
            }
            
            @Override
            public void notifyReConnection(final int attempt, final long waitTime) {
                System.out.println(String.format("Connection attempt %d wait time %d", attempt, waitTime));
            }
            
            @Override
            public void notifyConnectionError(final TransportGnipException e) {
                System.out.println(String.format("ConnectionError: %s", e.getMessage()));
            }
            
            @Override
            public void notify(final Activity activity, final GnipStream stream) {
                System.out.println(activity.getBody());
            }
        });
        stream.openAndAwait();
    }
    
    
    public static final class ActivityNetworkExceptionInputStream extends FilterInputStream {

        private final int amountOfActivitiesTillException;
        private int activitiesCount = 0;
        
        /**
         * Creates the ActivityExceptionInputStream.
         *
         * @param in
         */
        protected ActivityNetworkExceptionInputStream(
                final InputStream in, 
                final int amountOfActivitiesTillException) {
            super(in);
            this.amountOfActivitiesTillException = amountOfActivitiesTillException;
        }

        @Override
        public int read() throws IOException {
            final int ret = super.read();
            if (ret == '\n') {
                if (++activitiesCount == amountOfActivitiesTillException) {
                    throw new IOException();
                }
            }
            return ret;
        }
    }
}
