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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.zaubersoftware.gnip4j.api.support.http.AbstractReleaseInputStream;

/**
 * {@link InputStream} that knows how to release the resources of httpclient classes.  
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public final class HttpResponseReleaseInputStream extends AbstractReleaseInputStream {
    private HttpEntity entity;

    /** constructor */
    public HttpResponseReleaseInputStream(final HttpResponse response) throws IOException {
        super(response.getEntity().getContent());
        
        entity = response.getEntity();
    }

    @Override
    public void close() throws IOException {
        try {
            if(target != null) {
                try {
                    target.close();
                    target = null;
                } catch (IOException e) {
                    // ignore
                }
            }
        } finally {
            if(entity != null) {
                try {
                    EntityUtils.consume(entity);
                    entity = null;
                } catch (final IOException e) {
                    // ignore
                }
            }
        }
    }

}