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
 * Base exception
 *
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class GnipException extends RuntimeException {
    /** <code>serialVersionUID</code> */
    private static final long serialVersionUID = 7264815091470201543L;

    /**
     * Creates the GnipException.
     *
     */
    public GnipException() {
        super();
    }

    /**
     * Creates the GnipException.
     *
     * @param message
     * @param cause
     */
    public GnipException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates the GnipException.
     *
     * @param message
     */
    public GnipException(final String message) {
        super(message);
    }

    /**
     * Creates the GnipException.
     *
     * @param cause
     */
    public GnipException(final Throwable cause) {
        super(cause);
    }
}
