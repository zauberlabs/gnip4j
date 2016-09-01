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
package com.zaubersoftware.gnip4j.api.impl;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;

/**
 * Bean for storing basic user credentials
 * 
 * @author Guido Marucci Blas
 * @since Apr 28, 2011
 */
public final class ImmutableGnipAuthentication implements GnipAuthentication {

    private final String username;
    private final String password;
    
    /**
     * Creates the BasicCredentials.
     *
     * @param username
     * @param password
     */
    public ImmutableGnipAuthentication(final String username, final String password) {
        if(username == null) {
            throw new IllegalArgumentException("The username cannot be null");
        }
        if(password == null) {
            throw new IllegalArgumentException("The password cannot be null");
        }
            
        this.username = username;
        this.password = password;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
