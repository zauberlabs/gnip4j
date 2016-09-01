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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import com.zaubersoftware.gnip4j.server.GnipChunkedInputFactory;

/**
 * A {@link GnipChunkedInputFactory} that knows how to create an {@link NettyChunkedInputFactory} 
 *
 * @author Ignacio De Maio
 * @since 10/01/2012
 */
public class NettyChunkedInputFactory implements GnipChunkedInputFactory{

    private final Collection<String> activities;

    /**
     * 
     * Creates the NettyHandlerAggregator.
     *
     * @param activities An input stream of activities. The stream should contain a JSON activity per line.
     * This stream will be read and kept in memory in order to serve the activities to the clients. Once
     * all the activities have been served, the server will start serving the first activity again.
     */
    public NettyChunkedInputFactory(final InputStream activities) {
        
        if (activities == null) {
            throw new IllegalArgumentException("The activities stream cannot be null");
        }
        
        this.activities = parseActivities(activities);
    }
    
    @Override
    public GnipChunkedInput getChunkedInput() {
        return new GnipChunkedInput(new NextChunkUnlimited(activities));
    }
    
    public Collection<String> getActivities() {
        return activities;
    }

    protected static final Collection<String> parseActivities(final InputStream activities) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(activities, "UTF-8"));
            final Collection<String> result = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line.trim() + "\r\n");
            }
            return result;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


}
