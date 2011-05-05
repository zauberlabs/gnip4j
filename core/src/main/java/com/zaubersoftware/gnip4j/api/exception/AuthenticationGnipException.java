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
package com.zaubersoftware.gnip4j.api.exception;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class AuthenticationGnipException extends GnipException {

    /**
     * Creates the AuthenticationGnipException.
     *
     */
    public AuthenticationGnipException() {
        super();
        // TODO: Auto-generated constructor stub
    }

    /**
     * Creates the AuthenticationGnipException.
     *
     * @param message
     * @param cause
     */
    public AuthenticationGnipException(String message, Throwable cause) {
        super(message, cause);
        // TODO: Auto-generated constructor stub
    }

    /**
     * Creates the AuthenticationGnipException.
     *
     * @param message
     */
    public AuthenticationGnipException(String message) {
        super(message);
        // TODO: Auto-generated constructor stub
    }

    /**
     * Creates the AuthenticationGnipException.
     *
     * @param cause
     */
    public AuthenticationGnipException(Throwable cause) {
        super(cause);
        // TODO: Auto-generated constructor stub
    }
    
    //TODO Add the user credentials that were trying to be authenticated
}
