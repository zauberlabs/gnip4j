/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.server;

import java.io.InputStream;

/**
 * A factory of {@link GnipServer}
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public interface GnipServerFactory {

    /**
     * Creates a new {@link GnipServer}
     *
     * @param port The port to which the socket will be binded.
     * @param activities An input stream of activities. The stream should contain a JSON activity per line.
     * This stream will be read and kept in memory in order to serve the activities to the clients. Once
     * all the activities have been served, the server will start serving the first activity again.
     * @return A new {@link GnipServer} that needs to be started.
     */
    GnipServer createServer(int port, InputStream activities);


}
