/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package com.zaubersoftware.gnip4j.server.netty;

import static org.jboss.netty.channel.Channels.*;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import com.zaubersoftware.gnip4j.server.GnipServer;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class NettyGnipServer implements GnipServer {

    private static final class GnipPipelineFactory implements ChannelPipelineFactory {

        private final Collection<String> activities;

        /**
         * Creates the GnipPipelineFactory.
         *
         * @param activities
         */
        public GnipPipelineFactory(final Collection<String> activities) {
            this.activities = activities;
        }

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            final ChannelPipeline pipeline = pipeline();

            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

            pipeline.addLast("handler", new HttpGnipServerHandler(activities));

            return pipeline;
        }

    }

    private final int port;
    private final ServerBootstrap bootstrap;

    /**
     * Creates the NettyGnipServer.
     *
     * @param port
     * @param activities The collection of activities to be served to the client in a loop.
     */
    NettyGnipServer(final int port, final Collection<String> activities) {
        if (activities == null || activities.isEmpty()) {
            throw new IllegalArgumentException("The collection of activities cannot be null or empty");
        }
        this.port = port;

        // Configure the server.
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new GnipPipelineFactory(activities));
    }

    @Override
    public void start() {
        bootstrap.bind(new InetSocketAddress(port));
    }

}
