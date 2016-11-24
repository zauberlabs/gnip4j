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

import java.io.InputStream;
import java.net.URI;

import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;

/**
 * Facade to the Network (HTTP)
 * 
 * @author Juan F. Codagnone
 * @since May 22, 2011
 */
public interface RemoteResourceProvider {

    /** Get a remote resource from Gnip. */
    InputStream getResource(final URI uri) throws AuthenticationGnipException, TransportGnipException;

    /** Post a remote resource to Gnip. */
    <T> T postResource(final URI uri, Object resource, Class<T>responseType) throws AuthenticationGnipException, TransportGnipException;
    
    /** Delete a remote resource to Gnip. */
    void deleteResource(final URI uri, Object resource) throws AuthenticationGnipException, TransportGnipException;
}
