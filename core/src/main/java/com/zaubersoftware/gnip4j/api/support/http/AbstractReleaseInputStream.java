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
package com.zaubersoftware.gnip4j.api.support.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream} that knows how to release the resources of httpclient classes.  
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public abstract class AbstractReleaseInputStream extends InputStream {
    protected InputStream target;

    /** constructor */
    public  AbstractReleaseInputStream(final InputStream target) throws IOException {
        if(target == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.target = target;
    }

    @Override
    public final int read() throws IOException {
        return target == null ? -1 : target.read();
    }

    @Override
    public final int hashCode() {
        return target.hashCode();
    }

    @Override
    public final int read(final byte[] b) throws IOException {
        return target == null ? -1 : target.read(b);
    }

    @Override
    public final boolean equals(final Object obj) {
        return target.equals(obj);
    }

    @Override
    public final int read(final byte[] b, final int off, final int len) throws IOException {
        return target == null ? -1 : target.read(b, off, len);
    }

    @Override
    public final long skip(final long n) throws IOException {
        return target.skip(n);
    }

    @Override
    public final int available() throws IOException {
        return target == null ? 0 : target.available();
    }

    @Override
    public final String toString() {
        return target == null ? null : target.toString();
    }

    @Override
    public final void mark(final int readlimit) {
        target.mark(readlimit);
    }

    @Override
    public final void reset() throws IOException {
        target.reset();
    }

    @Override
    public final boolean markSupported() {
        return target.markSupported();
    }
}