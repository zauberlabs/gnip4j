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
package com.zaubersoftware.gnip4j.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * {@link InputStream} that knows how to release the resources of httpclient classes.  
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public final class HttpResponseReleaseInputStream extends InputStream {
    private InputStream target;
    private HttpEntity entity;

    /** constructor */
    public HttpResponseReleaseInputStream(final HttpResponse response) throws IOException {
        if(response == null) {
            throw new IllegalArgumentException("null argument");
        }
        entity = response.getEntity();
        target = entity.getContent();
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
    @Override
    public int read() throws IOException {
        return target.read();
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return target.read(b);
    }

    @Override
    public boolean equals(final Object obj) {
        return target.equals(obj);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return target.read(b, off, len);
    }

    @Override
    public long skip(final long n) throws IOException {
        return target.skip(n);
    }

    @Override
    public int available() throws IOException {
        return target.available();
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public void mark(final int readlimit) {
        target.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        target.reset();
    }

    @Override
    public boolean markSupported() {
        return target.markSupported();
    }
}