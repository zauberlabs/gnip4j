/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
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
