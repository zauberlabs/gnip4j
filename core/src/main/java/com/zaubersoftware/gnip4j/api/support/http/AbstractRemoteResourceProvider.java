/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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
package com.zaubersoftware.gnip4j.api.support.http;

import java.net.URI;

import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;

/**
 * Abstract {@link RemoteResourceProvider}.
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public abstract class AbstractRemoteResourceProvider implements RemoteResourceProvider {
    protected static final String USER_AGENT = "Gnip4j (https://github.com/zaubersoftware/gnip4j/)";

    /** validate responses */
    public final void validateStatusLine(final URI uri, final int statusCode, final String reason) {
        System.out.println(uri);
        if (statusCode == 200 || statusCode == 201) {
            // nothing to do
        } else if (statusCode == 401) {
            throw new AuthenticationGnipException(reason);
        } else {
            throw new TransportGnipException(String.format("Connection to %s: Unexpected status code: %s %s", uri,
                    statusCode, reason));
        }
    }
}
