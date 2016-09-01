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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.protocol.HttpContext;

/**
 * GZIP support. From HttpClient examples. 
 * 
 * @author Juan F. Codagnone
 * @since Feb 27, 2010
 */
public class GZipInterceptor implements HttpRequestInterceptor, 
                                        HttpResponseInterceptor {
    
    /** @see HttpRequestInterceptor#process(HttpRequest, HttpContext) */
    public final void process(final HttpRequest request, final HttpContext context) 
         throws HttpException, IOException {
        if (!request.containsHeader("Accept-Encoding")) {
            request.addHeader("Accept-Encoding", "gzip");
        }
    }

    /** @see HttpResponseInterceptor#process(HttpResponse, HttpContext) */
    public final void process(final HttpResponse response, 
            final HttpContext context) throws HttpException, IOException {
        final HttpEntity entity = response.getEntity();
        final Header ceheader = entity.getContentEncoding();
        if (ceheader != null) {
            final HeaderElement[] codecs = ceheader.getElements();
            for (int i = 0; i < codecs.length; i++) {
                if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                    response.setEntity(new GzipDecompressingEntity(
                            response.getEntity())); 
                    return;
                }
            }
        }
    }
    
}

/**
 * GZIP Entity
 * 
 * @author Juan F. Codagnone
 * @since Feb 27, 2010
 */
class GzipDecompressingEntity extends HttpEntityWrapper {

    /** constructor */
    public GzipDecompressingEntity(final HttpEntity entity) {
        super(entity);
    }

    /** @see HttpEntityWrapper#getContent() */
    @Override
    public final InputStream getContent() throws IOException, IllegalStateException {

        // the wrapped entity's getContent() decides about repeatability
        InputStream wrappedin = wrappedEntity.getContent();

        return new GZIPInputStream(wrappedin);
    }

    /** @see HttpEntityWrapper#getContentLength() */
    @Override
    public final long getContentLength() {
        // length of ungzipped content is not known
        return -1;
    }
} 
