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

import java.io.InputStream;

import com.zaubersoftware.gnip4j.server.GnipServerFactory;

/**
 * A {@link GnipServerFactory} that creates {@link NettyGnipServer} instances
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class LimitedNettyChunkedInputFactory extends NettyChunkedInputFactory{

    private final int numberOfChunks;
    
    /**
     * 
     * Creates the NettyHandlerAggregator.
     * @param limitedActivities
     * @param numberOfChunks
     */
    public LimitedNettyChunkedInputFactory(final InputStream limitedActivities,
            final int numberOfChunks) {
        super(limitedActivities);
        this.numberOfChunks = numberOfChunks;
    }
    
    @Override
    public GnipChunkedInput getChunkedInput() {
       return new GnipChunkedInput(new NextChunkLimited(getActivities(), numberOfChunks));
    }

}
