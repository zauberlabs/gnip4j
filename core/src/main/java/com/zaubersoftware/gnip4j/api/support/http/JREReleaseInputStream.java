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
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 24, 2011
 */
public class JREReleaseInputStream extends AbstractReleaseInputStream {
    private final URLConnection huc;

    /** Creates the JREReleaseInputStream. */
    public JREReleaseInputStream(final URLConnection huc, final InputStream target) throws IOException {
        super(target);
        
        this.huc = huc;
    }

    @Override
    public final void close() throws IOException {
        try {
            if(target != null) {
                try {
                    target.close();
                    target = null;
                } catch (final IOException e) {
                    // ignore
                }
            }
        } finally {
            if(huc instanceof HttpURLConnection) {
                ((HttpURLConnection)huc).disconnect();
            }
        }
    }
    
}
