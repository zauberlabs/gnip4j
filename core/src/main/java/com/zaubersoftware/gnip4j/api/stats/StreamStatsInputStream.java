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
package com.zaubersoftware.gnip4j.api.stats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zaubersoftware.gnip4j.api.stats.commonsio.TeeInputStream;

/**
 * Capture stats from the stream
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public class StreamStatsInputStream extends TeeInputStream {
    
    /** Creates the StreamStatsInputStream. */
    public StreamStatsInputStream(final ModifiableStreamStats stats,
            final InputStream target) {
        super(target, new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                stats.incrementTransferedBytes();
                
                // new line not sufficient to count as activity rcvd since stream
                // sends CRLF every 30 secs or so to keep-alive
                /*if (b == '\n') {
                    stats.incrementTransferedActivities();
                }*/
            }
        });
    }
}