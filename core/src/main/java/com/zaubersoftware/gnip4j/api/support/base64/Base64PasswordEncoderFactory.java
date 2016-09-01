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
package com.zaubersoftware.gnip4j.api.support.base64;

import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoder;
import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoderImpl;

/**
 * Lookups de implementation for {@link Base64PasswordEncoder}.
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public final class Base64PasswordEncoderFactory {
    private static Base64PasswordEncoder target;
    
    static {
        target = new Base64PasswordEncoderImpl();
    }
    
    /** Creates the Base64PasswordEncoderFactory. */
    private Base64PasswordEncoderFactory() {
        // void
    }
    
    public static Base64PasswordEncoder getEncoder() {
        return target;
    }
}
