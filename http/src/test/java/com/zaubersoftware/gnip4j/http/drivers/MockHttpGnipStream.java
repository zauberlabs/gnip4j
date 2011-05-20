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
package com.zaubersoftware.gnip4j.http.drivers;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.http.AbstractHttpGnipStream;
import com.zaubersoftware.gnip4j.http.HttpGnipFacade;

/**
 * Mock {@link HttpGnipFacade}.  
 * 
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public final class MockHttpGnipStream extends AbstractHttpGnipStream {

    private HttpResponse response = null;
    
    /** Creates the MockHttpGnipStream. */
    public MockHttpGnipStream(
            @NotNull final DefaultHttpClient client, 
            @NotNull final String domain, 
            final long dataCollectorId,
            @NotNull final GnipAuthentication auth,
            final HttpResponse response) {
        super(client, domain, dataCollectorId, auth);
        this.response = response;
    }

    public void setResponse(final HttpResponse response) {
        this.response = response;
    }
    
    @Override
    protected HttpResponse getResponse(final HttpUriRequest request) throws IOException {
        return response;
    }

}
