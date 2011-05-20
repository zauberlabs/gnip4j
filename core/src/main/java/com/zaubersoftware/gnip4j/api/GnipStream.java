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
package com.zaubersoftware.gnip4j.api;


/**
 * <p>
 * A Stream for the Gnip. Once created with {@link GnipFacade} you más register
 * some observer that will process the data. User MUST call {@link #close()} 
 * to release all the releated resources (or when you want to handle gracefull shutdowns)
 * </p>
 * <p>
 * Implementations SHOULD handle reconnections and timeouts.
 * </p>
 * <p>
 *    You should first call to {@link #addObserver(StreamNotification)} and
 *    the {@link #open()}/{{@link #openAndAwait()}} the stream.
 *    When you don't wan't any more data, call to {@link #close()} to release
 *    all the stream resources. 
 * </p>
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public interface GnipStream {

    /**
     * Adds a {@link StreamNotification} observer
     * 
     * @param notification an implementation of {@link StreamNotification}.
     */
    void addObserver(StreamNotification notification);

    /** 
     * Opens the stream 
     * 
     * @throws IllegalStateException if the stream is already opened.
     */
    void open();
    
    /**
     * Opens the stream and await for termination
     * 
     * @throws IllegalStateException if the stream is already opened.
     * @see GnipStream#open()
     * @see GnipStream#await()
     */
    void openAndAwait() throws InterruptedException;
    
    /** release the stream  */
    void close();

    /**
     * await for the stream to be {@link #close()} or 
     * to be shutdown because of some catastrofic issue.
     */
    void await() throws InterruptedException;
}
