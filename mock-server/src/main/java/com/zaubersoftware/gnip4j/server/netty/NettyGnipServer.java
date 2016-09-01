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
package com.zaubersoftware.gnip4j.server.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
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

        private final NettyChunkedInputFactory chunkedInputFactory;
        /**
         * Creates the GnipPipelineFactory.
         * @param nettyGnipServer 
         * @param nettyGnipServer 
         *
         * @param activities
         * @param timesToStart 
         */
        public GnipPipelineFactory(final NettyChunkedInputFactory chunkedInputFactory) {
            this.chunkedInputFactory = chunkedInputFactory;
        }

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            final ChannelPipeline pipeline = pipeline();

            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
            pipeline.addLast("handler", new HttpGnipServerHandler(chunkedInputFactory.getChunkedInput()));
            
            return pipeline;
        }

    }

    static final ChannelGroup allChannels = new DefaultChannelGroup("time-server");
    private final int port;
    private final ServerBootstrap bootstrap;
    public static Channel boundedChannel;
    private final NioServerSocketChannelFactory channelFactory;

    /**
     * Creates the NettyGnipServer.
     *
     * @param port
     * @param activities The collection of activities to be served to the client in a loop.
     * @param timesToStart 
     */
    NettyGnipServer(final int port, final NettyChunkedInputFactory handlerFactory) {
        
        this.port = port;

        // Configure the server.
        channelFactory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());
        bootstrap = new ServerBootstrap(channelFactory);

        // Set up the event pipeline factory.
        final GnipPipelineFactory pipelineFactory = new GnipPipelineFactory(handlerFactory);
        bootstrap.setPipelineFactory(pipelineFactory);
    }

    @Override
    public void start() {
        boundedChannel = bootstrap.bind(new InetSocketAddress(port));
        allChannels.add(boundedChannel);
    }

    @Override
    public void shutdown() {
        final ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();
        channelFactory.releaseExternalResources();
        //bootstrap.releaseExternalResources();
    }
}
