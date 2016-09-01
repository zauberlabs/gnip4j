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
package com.zaubersoftware.gnip4j.api.exception;

/**
 * There was a problem with remote authentication.
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class AuthenticationGnipException extends GnipException {
    private static final long serialVersionUID = -5679596104912905821L;

    /**
     * Creates the AuthenticationGnipException.
     *
     */
    public AuthenticationGnipException() {
        super();
    }

    /** Creates the AuthenticationGnipException. */
    public AuthenticationGnipException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /** Creates the AuthenticationGnipException. */
    public AuthenticationGnipException(final String message) {
        super(message);
    }

    /** Creates the AuthenticationGnipException. */
    public AuthenticationGnipException(final Throwable cause) {
        super(cause);
    }

    //TODO Add the user credentials that were trying to be authenticated
}
