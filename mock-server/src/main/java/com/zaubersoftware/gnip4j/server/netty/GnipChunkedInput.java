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

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.stream.ChunkedInput;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class GnipChunkedInput implements ChunkedInput {

    private final NextChunkStrategy nextChunkStrategy;

    /**
     * Creates the GnipChunkedInput.
     */
    public GnipChunkedInput(final NextChunkStrategy nextChunkStrategy) {
        this.nextChunkStrategy = nextChunkStrategy;
    }

    @Override
    public boolean hasNextChunk() throws Exception {
        return true;
    }

    @Override
    public Object nextChunk() throws Exception {
        return nextChunkStrategy.nextChunk();
    }

    @Override
    public boolean isEndOfInput() throws Exception {
        return false;
    }

    @Override
    public void close() throws Exception {

    }

    public void setChannel(final Channel channel) {
        nextChunkStrategy.setChannel(channel);
    }
}
