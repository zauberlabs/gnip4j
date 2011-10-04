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
package com.zaubersoftware.gnip4j.api.support.base64;

import static org.junit.Assert.*;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoder;
import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoderImpl;


/**
 * Test {@link Base64PasswordEncoder}
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class Base64PasswordEncoderTest {

    /** {@link Test} */
    @Test
    public final void foo() {
        assertEquals("anVhbjpqdWFu",
          new Base64PasswordEncoderImpl().encode(new ImmutableGnipAuthentication("juan", "juan")));
    }
}
