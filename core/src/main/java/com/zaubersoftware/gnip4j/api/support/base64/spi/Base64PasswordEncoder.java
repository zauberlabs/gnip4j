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
package com.zaubersoftware.gnip4j.api.support.base64.spi;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;

/**
 * Base64 provider
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public interface Base64PasswordEncoder {
    /** @return "basic" codification base64(auth.user ":" auth.pass)*/
    String encode(GnipAuthentication auth);
}
