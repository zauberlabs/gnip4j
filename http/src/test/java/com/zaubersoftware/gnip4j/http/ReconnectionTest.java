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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.zaubersoftware.gnip4j.MockHttpGnipStream;
import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;


/**
 * Re connection algorithm test 
 * 
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public final class ReconnectionTest {
    /** test */
    @Test(timeout = 20000)
    public void testReConnection() throws Exception {
        // ignore framework warnings
        Logger root = Logger.getRootLogger();
        root.setLevel(Level.OFF);
        
        final ActivityNetworkExceptionInputStream instream = new ActivityNetworkExceptionInputStream(
            "payload-example-2.js");
        final GnipAuthentication auth = new InmutableGnipAuthentication("test", "test");
        final StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "");
        final HttpEntity entity = new InputStreamEntity(instream, 10000);
        final HttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        
        final AtomicBoolean error = new AtomicBoolean(false);
        final AtomicInteger count = new AtomicInteger(0);
        final MockHttpGnipStream stream = new MockHttpGnipStream("test", 1, auth, response);
        final AtomicBoolean reConnected = new AtomicBoolean(false);
        
        final StringBuilder out = new StringBuilder();
        
        final StreamNotification n = new StreamNotification() {
            @Override
            public void notifyReConnectionError(final GnipException e) {
                out.append(String.format("ReConnectionError: %s\n", e.getMessage()));
            }
            
            @Override
            public void notifyReConnection(final int attempt, final long waitTime) {
                out.append(String.format("Connection attempt %d wait time %d\n", attempt, waitTime));
                if (attempt > 2) {
                    response.setEntity(new InputStreamEntity(new ActivityNetworkExceptionInputStream(
                            "payload-example-2.js"), 10000));
                    stream.setResponse(response);
                    instream.setThrowException(false);
                    reConnected.set(true);
                    count.set(0);
                }
            }
            
            @Override
            public void notifyConnectionError(final TransportGnipException e) {
                out.append(String.format("ConnectionError: %s\n", e.getMessage()));
                if (!error.get()) {
                    stream.setResponse(new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 505, "TEST!"));
                    error.set(true);
                }
            }
            
            @Override
            public void notify(final Activity activity, final GnipStream stream) {
                out.append(activity.getBody() + "\n");
                if (count.incrementAndGet() > 2 && !reConnected.get()) {
                    instream.setThrowException(true);
                } else if (count.incrementAndGet() > 4 && reConnected.get()) {
                    stream.close();
                }
            }
        };
        stream.openAndAwait(n);
        final String s = out.toString();
        final String expected = IOUtils.toString(getClass().getClassLoader()
                .getResourceAsStream("reconnectlog.txt"));
        if (!expected.equals(s)
                && !expected.replace("Connection attempt 4 wait time 250",
                        "Connection attempt 4 wait time 4000").equals(s)) {
            Assert.fail(s);
        }
    }
}
