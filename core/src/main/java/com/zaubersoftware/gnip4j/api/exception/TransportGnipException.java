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

import java.io.IOException;

/**
 * Represents an exception that was caused by a transport error.
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class TransportGnipException extends GnipException {

    
    /** <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3682753762097222234L;

    /** Creates the TransportGnipException. */
    public TransportGnipException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /** Creates the TransportGnipException. */
    public TransportGnipException(final String message) {
        super(message);
    }

    /** Creates the TransportGnipException. */
    public TransportGnipException(final Throwable cause) {
        super(cause);
    }

    /**
     * Checks if the error was caused by a network exception.
     * 
     * @return True if the error was caused by a newtork exception
     */
    public final boolean isNetworkError() {
        return (getCause() != null) ? getCause() instanceof IOException : false;
    }
    
    //TODO Add support for transport information error, for example, status code Maybe by subclassing this
    //exception in HttpRequestGnipException
}
