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
package com.zaubersoftware.gnip4j.server.netty;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpMethod.*;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.*;

import java.util.Collection;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public class HttpGnipServerHandler extends SimpleChannelUpstreamHandler {

    private final Collection<String> activities;

    /**
     * Creates the HttpGnipServerHandler.
     *
     * @param activities
     */
    public HttpGnipServerHandler(final Collection<String> activities) {
        if (activities == null || activities.isEmpty()) {
            throw new IllegalArgumentException("The collection of activities cannot be null or empty");
        }
        this.activities = activities;
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        final HttpRequest request = (HttpRequest) e.getMessage();

        if (request.getMethod() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.getUri();
        if (uri.equals("/fake-stream")) {
            final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            response.setHeader(TRANSFER_ENCODING, "chunked");
            response.setChunked(true);
            final Channel ch = e.getChannel();
            ch.write(response);
            ch.write(new GnipChunkedInput(activities));
        } else if (uri.equals("/")) {
            final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            response.setStatus(MOVED_PERMANENTLY);
            response.setHeader(LOCATION, "/fake-stream");

            final Channel ch = e.getChannel();
            ChannelFuture writeFuture;

            // Write the initial line and the header.
            writeFuture = ch.write(response);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        } else {
            sendError(ctx, NOT_FOUND);
            return;
        }
    }

    @Override
    public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        System.out.println("HOLAAAAAAAAA!");
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
        final Channel ch = e.getChannel();
        final Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        cause.printStackTrace();
        if (ch.isConnected()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(final ChannelHandlerContext ctx, final HttpResponseStatus status) {
        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8));

        // Close the connection as soon as the error message is sent.
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }
}