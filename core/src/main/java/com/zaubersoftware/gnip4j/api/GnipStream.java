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
package com.zaubersoftware.gnip4j.api;

import java.util.concurrent.TimeUnit;

import com.zaubersoftware.gnip4j.api.stats.StreamStats;



/**
 * <p>
 * A Stream for the Gnip. Once created with {@link GnipFacade} you must register
 * some observer that will process the data. User MUST call {@link #close()} 
 * to release all the related resources (or when you want to handle graceful shutdowns)
 * </p>
 * <p>
 * Implementations SHOULD handle reconnections and timeouts.
 * </p>
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public interface GnipStream {
    
    /** release the stream  */
    void close();

    /**
     * await for the stream to be {@link #close()} or 
     * to be shutdown because of some catastrophic issue.
     */
    void await() throws InterruptedException;
    
    /**
     * await for the stream to be {@link #close()} or 
     * to be shutdown because of some catastrophic issue.
     * 
     * @param time the maximum time to wait
     * @param unit the time unit of the {@code time} argument
     * @return true si fue cerrado, false si salio por timeout 
     */
    boolean await(long time, TimeUnit unit) throws InterruptedException;
    
    
    /** @return an identification for this stream */
    String getStreamName();
    
    /** @return stream stats */
    StreamStats getStreamStats();

}
