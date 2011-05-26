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
package com.zaubersoftware.gnip4j.api.support.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.ErrorCodes;
import com.zaubersoftware.gnip4j.api.support.base64.Base64PasswordEncoderFactory;
import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoder;

/**
 * JRE Resource Provider
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class JRERemoteResourceProvider extends AbstractRemoteResourceProvider {
    private final GnipAuthentication authentication;
    private final Base64PasswordEncoder encoder = Base64PasswordEncoderFactory.getEncoder();
    private int connectTimeout = 10000;
    private int readTimeout = 10000;
    
    /** Creates the JRERemoteResourceProvider. */
    public JRERemoteResourceProvider(final GnipAuthentication authentication) {
        if(authentication == null) {
            throw new IllegalArgumentException(ErrorCodes.ERROR_NULL_AUTH);
        }
        
        this.authentication = authentication;
        if(CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
    }
    
    @Override
    public final InputStream getResouce(final URI uri) throws AuthenticationGnipException,
            TransportGnipException {
        
        try {
            final URLConnection uc = uri.toURL().openConnection();
            HttpURLConnection huc = null;
            
            if(uc instanceof HttpURLConnection) {
                huc = (HttpURLConnection) uc;
            }
            uc.setAllowUserInteraction(false);
            uc.setDefaultUseCaches(false);
            uc.setConnectTimeout(connectTimeout);
            uc.setReadTimeout(readTimeout);
            uc.setRequestProperty("Accept-Encoding", "gzip, deflate"); 
            uc.setRequestProperty("User-Agent", USER_AGENT);
            uc.setRequestProperty ("Authorization", "Basic " + encoder.encode(authentication));
            doConfiguration(uc);
            uc.connect();
            
            if(huc != null) {
                validateStatusLine(uri, huc.getResponseCode(), huc.getResponseMessage());
            }
            InputStream is = uc.getInputStream(); 
            final String encoding = uc.getContentEncoding();
            if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
                is = new GZIPInputStream(is);
            } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
                is = new InflaterInputStream(is, new Inflater(true));
            }
            return new JREReleaseInputStream(uc, is);
        } catch (MalformedURLException e) {
            throw new TransportGnipException(e);
        } catch (IOException e) {
            throw new TransportGnipException(e);
        }
    }

    /** template method for configuring the URLConnection */
    protected void doConfiguration(final URLConnection uc) {
        
    }
}
