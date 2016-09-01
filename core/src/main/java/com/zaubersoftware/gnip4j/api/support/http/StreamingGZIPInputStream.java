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
import java.util.zip.GZIPInputStream;

public class StreamingGZIPInputStream extends GZIPInputStream {
 
    private final InputStream wrapped;
    public StreamingGZIPInputStream(final InputStream is) throws IOException {
      super(is);
      wrapped = is;
    }
 
    /**
     * GZIPInputStream assumes all data is present when reading/checking available bytes
     * This is not true for streaming use cases like connections to PowerTrack streams.
     * 
     * 
     * We will wrap the GZIPInputStream and use the underlying InputStream to tell us
     * how much data is available.
     * 
     * Without this wrapping, was seeing that the BufferedReader in ByLineFeedProcessor
     * was never geeing lines to read.
     * 
     * Warning dont rely on this method to return the actual number
     * of bytes that could be read without blocking.
     *
     * @return - whatever the wrapped InputStream returns
     * @exception  IOException  if an I/O error occurs.
     */
    
    @Override
    public int available() throws IOException {
      return wrapped.available();
    }
}