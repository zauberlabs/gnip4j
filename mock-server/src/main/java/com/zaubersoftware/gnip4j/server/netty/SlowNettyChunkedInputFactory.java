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
public final class SlowNettyChunkedInputFactory extends NettyChunkedInputFactory{

    private final int timeBetweenChunks;
    //private final int startedTimes = 1;
    //private final Collection<String> limitedActivities;
    
    /**
     * 
     * Creates the NettyHandlerAggregator.
     * @param limitedActivities
     */
    public SlowNettyChunkedInputFactory(final InputStream limitedActivities,
            final int timeBetweenChunks) {
        super(limitedActivities);
        this.timeBetweenChunks = timeBetweenChunks;
    }
    
    @Override
    public GnipChunkedInput getChunkedInput() {
        return new GnipChunkedInput(new NextChunkSlow(getActivities(), timeBetweenChunks));
    }

}
