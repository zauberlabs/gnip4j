/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.server.netty;

import static org.jboss.netty.buffer.ChannelBuffers.*;

import java.util.Collection;
import java.util.Iterator;

import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.stream.ChunkedInput;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class GnipChunkedInput implements ChunkedInput {

    private final Collection<String> activities;
    private Iterator<String> iterator;

    /**
     * Creates the GnipChunkedInput.
     */
    public GnipChunkedInput(final Collection<String> activities) {
        if (activities == null || activities.isEmpty()) {
            throw new IllegalArgumentException("The collection of activities cannot be null or empty");
        }
        this.activities = activities;
        this.iterator = activities.iterator();
    }

    @Override
    public boolean hasNextChunk() throws Exception {
        return true;
    }

    @Override
    public Object nextChunk() throws Exception {
        if (iterator.hasNext() == false) {
            iterator = activities.iterator();
        }
        final String activity = iterator.next();
        final DefaultHttpChunk chunk = new DefaultHttpChunk(wrappedBuffer(activity.getBytes("UTF-8")));
        return chunk;
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        return false;
    }

    @Override
    public void close() throws Exception {

    }

}
