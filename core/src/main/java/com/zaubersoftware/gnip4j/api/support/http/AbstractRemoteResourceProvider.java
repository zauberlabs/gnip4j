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
import com.zaubersoftware.gnip4j.api.exception.GnipUnprocessableEntityException;
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
    public final void validateStatusLine(final URI uri, final int statusCode, final String reason,
            final ErrorProvider errorProvider) {
        if (statusCode >= 200 && statusCode <= 299) {
            // nothing to do
        } else { 
            String msg = null;
            Error error = null;
            if(errorProvider != null) {
              error = errorProvider.getError();
              msg = error.getMessage();
            }
            if(msg == null) {
              msg = "";
            }

            if (statusCode == 401) {
              throw new AuthenticationGnipException(reason);
            } else if (statusCode == 422) {
              GnipUnprocessableEntityException exception = null;
              try {
                exception = new GnipUnprocessableEntityException(String.format("Connection to %s", uri), msg);
              } catch (Exception e) {
                throw new TransportGnipException(String.format("Connection to %s: status code: %s %s %s", uri,
                    statusCode, reason, msg), e);
              }
              throw exception;
            }
          else {
              throw new TransportGnipException(
                  String.format("Connection to %s: Unexpected status code: %s %s %s",
                          uri, statusCode, reason, msg));
          }
        }
    }
}

interface ErrorProvider {
    Error getError();
}