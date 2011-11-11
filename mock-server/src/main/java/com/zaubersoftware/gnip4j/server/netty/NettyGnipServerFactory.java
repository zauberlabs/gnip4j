/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.server.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import com.zaubersoftware.gnip4j.server.GnipServer;
import com.zaubersoftware.gnip4j.server.GnipServerFactory;

/**
 * A {@link GnipServerFactory} that creates {@link NettyGnipServer} instances
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class NettyGnipServerFactory implements GnipServerFactory {

    @Override
    public GnipServer createServer(final int port, final InputStream activities) {
        if (activities == null) {
            throw new IllegalArgumentException("The activities stream cannot be null");
        }
        return new NettyGnipServer(port, getActivities(activities));
    }

    private static final Collection<String> getActivities(final InputStream activities) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(activities));
            final Collection<String> result = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return result;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
