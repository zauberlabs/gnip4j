/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
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
                } catch (IOException e) {
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
