/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.server;

import java.io.InputStream;

import com.zaubersoftware.gnip4j.server.netty.NettyGnipServerFactory;

/**
 * Main entry point to start the {@link GnipServer}
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class Main {

    private static final int DEFAUL_SERVER_PORT = 8080;
    private static final InputStream ACTIVITIES = Main.class.getClassLoader().getResourceAsStream(
            "com/zaubersoftware/gnip4j/server/activity/activities.json");

    public static void main(final String[] args) {
        final GnipServerFactory gnipServerFactory = new NettyGnipServerFactory();
        final GnipServer gnipServer = gnipServerFactory.createServer(DEFAUL_SERVER_PORT, ACTIVITIES);

        gnipServer.start();
        System.out.println("Gnip server started at port " + DEFAUL_SERVER_PORT);
    }

}
