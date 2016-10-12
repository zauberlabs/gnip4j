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
package com.zaubersoftware.gnip4j.api.support.http;

import java.net.URI;
import java.util.Locale;

import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.GnipUnprocessableEntityException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Abstract {@link RemoteResourceProvider}.
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public abstract class AbstractRemoteResourceProvider implements RemoteResourceProvider {
    protected static final String USER_AGENT = "Gnip4j (https://github.com/zaubersoftware/gnip4j/)";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /** validate responses */
    public final void validateStatusLine(final URI uri, final int statusCode, final String reason,
            final ErrorProvider errorProvider) {
        if (statusCode >= 200 && statusCode <= 299) {
            // nothing to do
        } else { 
            String msg = null;
            Errors errors = null;
            if (errorProvider != null) {
                errors = errorProvider.getError();
                if(errors != null) {
                    msg = errors.toHumanMessage();
                }
            }

            if (msg == null) {
                logger.warn("there isn't message for code {} and provider {}", statusCode, errorProvider);
                msg = "";
            }

            if (statusCode == 401) {
                throw new AuthenticationGnipException(reason);
            } else if (statusCode == 422) {
                GnipUnprocessableEntityException exception = null;
                try {
                    if (errors != null) {
                        exception = new GnipUnprocessableEntityException(String.format(Locale.ENGLISH, "Connection to %s", uri), errors);
                    } else {
                        exception = new GnipUnprocessableEntityException(String.format(Locale.ENGLISH, "Connection to %s", uri), msg);
                    }
                } catch (final Exception e) {
                    throw new TransportGnipException(
                            String.format(Locale.ENGLISH, "Connection to %s: status code: %s %s %s", uri, statusCode, reason, msg), e);
                }
                throw exception;
            } else {
                throw new TransportGnipException(String.format(Locale.ENGLISH, "Connection to %s: Unexpected status code: %s %s %s",
                        uri, statusCode, reason, msg));
            }
        }
    }
}

