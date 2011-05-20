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

import static org.junit.Assert.*;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.model.Activity;


/**
 * Test for {@link AbstractGnipStream}.
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class AbstractGnipStreamTest {

    /** test illegal null observer  */
    @Test(expected = IllegalArgumentException.class, timeout = 1000)
    public final void nullObserver() {
        new NullGnipStream().open(null);
    }
    
    /** test doble close */
    @Test(timeout = 5000)
    public final void testname() throws InterruptedException {
        final GnipStream stream = new NullGnipStream();
        try {
            stream.openAndAwait(new StreamNotificationAdapter() {
                @Override
                public void notify(final Activity activity, final GnipStream stream) {
                    stream.close();
                }
            });
        } finally {
            stream.close();
        }
    }
}

/**
 * Null Stream.
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
class NullGnipStream extends AbstractGnipStream {
    
    @Override
    public void open(final StreamNotification notification) {
        if(notification == null) {
            throw new IllegalArgumentException();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                notification.notify(new Activity(), NullGnipStream.this);
            }
        }).start();
    }
    
    @Override
    protected String getStreamName() {
        return "null";
    }
}  
