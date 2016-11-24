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
package com.zaubersoftware.gnip4j.http;

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutorService;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipStream;
import com.zaubersoftware.gnip4j.api.impl.PowerTrackV2UriStrategy;


/**
 * Tests input validations
 *
 *
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public class ValidationTest {

    private final UriStrategy uriStrategy = new PowerTrackV2UriStrategy();

    @Test
    public final void facadeNullClient() {
        try {
            new DefaultGnipFacade(null);
            fail();
        } catch(final IllegalArgumentException e) {
            assertEquals(ERROR_NULL_HTTPCLIENT, e.getMessage());
        }
    }

    @Test
    public final void streamNullClient() {
        try {
            new DefaultGnipStream(null, "account", "stream", mock(ExecutorService.class), uriStrategy);
            fail();
        } catch(final IllegalArgumentException e) {
            assertEquals(ERROR_NULL_HTTPCLIENT, e.getMessage());
        }
    }


    /** test */
    @Test
    public final void streamEmptyDomain() {
        try {
            new DefaultGnipStream(mock(RemoteResourceProvider.class),
                    " \t", "stream", mock(ExecutorService.class), uriStrategy);
            fail();
        } catch(final IllegalArgumentException e) {
            assertEquals(ERROR_EMPTY_ACCOUNT, e.getMessage());
        }
    }

    /** test */
    @Test
    public final void streamNullExecutorService() {
        try {
            new DefaultGnipStream(mock(RemoteResourceProvider.class),
                    "xxx \t", "stream", null, uriStrategy);
            fail();
        } catch(final IllegalArgumentException e) {
            assertEquals(ERROR_NULL_ACTIVITY_SERVICE, e.getMessage());
        }
    }

    /** test */
    @Test
    public final void streamNullUriStrategy() {
        try {
            new DefaultGnipStream(mock(RemoteResourceProvider.class),
                    "xxx \t", "stream", mock(ExecutorService.class), null);
            fail();
        } catch(final IllegalArgumentException e) {
            assertEquals(ERROR_NULL_BASE_URI_STRATEGY, e.getMessage());
        }
    }

}
