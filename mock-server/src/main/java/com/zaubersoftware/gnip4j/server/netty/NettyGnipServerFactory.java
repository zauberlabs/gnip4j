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
