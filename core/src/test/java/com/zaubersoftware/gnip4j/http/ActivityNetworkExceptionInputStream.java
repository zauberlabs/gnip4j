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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * InputStream that simulates errors 
 * 
 * @author Juan F. Codagnone
 * @since May 20, 2011
 */
public final class ActivityNetworkExceptionInputStream extends FilterInputStream {
    private int bytesBefore;
    
    /** Creates the ActivityExceptionInputStream. */
    public ActivityNetworkExceptionInputStream(final InputStream in, final int bytesBeforeError) {
        super(in);
        if(in == null) {
            throw new IllegalArgumentException("Null resource!");
        }
        this.bytesBefore = bytesBeforeError;
    }
    
    /** Creates the ActivityExceptionInputStream. */
    public ActivityNetworkExceptionInputStream(final String classpath, final int bytesBeforeError) {
        this(ActivityNetworkExceptionInputStream.class.getClassLoader()
                .getResourceAsStream(classpath), bytesBeforeError);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if(bytesBefore <= 0) {
            throw new IOException("mock connection closed");
        }
        final int ret = super.read(b, off, len);
        bytesBefore -= ret;
        return ret;
    }
}