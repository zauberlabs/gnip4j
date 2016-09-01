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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.codehaus.jackson.map.ObjectMapper;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.ErrorCodes;
import com.zaubersoftware.gnip4j.api.support.base64.Base64PasswordEncoderFactory;
import com.zaubersoftware.gnip4j.api.support.base64.spi.Base64PasswordEncoder;
import com.zaubersoftware.gnip4j.api.support.http.Errors.Error;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * JRE Resource Provider
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 23, 2011
 */
public class JRERemoteResourceProvider extends AbstractRemoteResourceProvider {
    private static final Logger logger = LoggerFactory.getLogger(JRERemoteResourceProvider.class);
    private final GnipAuthentication authentication;
    private final Base64PasswordEncoder encoder = Base64PasswordEncoderFactory.getEncoder();
    private final int connectTimeout = 10000;
    private final int readTimeout = 35000;
    
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
    public final InputStream getResource(final URI uri) throws AuthenticationGnipException,
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
                validateStatusLine(uri, huc.getResponseCode(), huc.getResponseMessage(), 
                                   new DefaultErrorProvider(huc));
            }
            final InputStream is = getRealInputStream(uc, uc.getInputStream()); 
            
            return new JREReleaseInputStream(uc, is);
        } catch (final MalformedURLException e) {
            throw new TransportGnipException(e);
        } catch (final IOException e) {
            throw new TransportGnipException(e);
        }
    }

    /** applies content enconding transformation */
    static InputStream getRealInputStream(final URLConnection uc, InputStream is) throws IOException {
        final String encoding = uc.getContentEncoding();
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            is = new StreamingGZIPInputStream(is);
        } else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
            is = new InflaterInputStream(is, new Inflater(true));
        }
        return is;
    }
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public final void postResource(final URI uri, final Object resource) throws AuthenticationGnipException,
            TransportGnipException {
        
        OutputStream outStream = null;
        try {
            final URLConnection uc = uri.toURL().openConnection();
            HttpURLConnection huc = null;
            
            if (uc instanceof HttpURLConnection) {
                huc = (HttpURLConnection) uc;
            }
            uc.setAllowUserInteraction(false);
            uc.setDefaultUseCaches(false);
            uc.setConnectTimeout(connectTimeout);
            uc.setReadTimeout(readTimeout);
            uc.setDoOutput(true); // Needed in order to make a POST request
            uc.setRequestProperty("Accept-Encoding", "gzip, deflate"); 
            uc.setRequestProperty("User-Agent", USER_AGENT);
            uc.setRequestProperty("Authorization", "Basic " + encoder.encode(authentication));
            uc.setRequestProperty("Content-type", "application/json");
            doConfiguration(uc);
            
            outStream = uc.getOutputStream();
            outStream.write(mapper.writeValueAsString(resource).getBytes("UTF-8"));
            
            if (huc != null) {
                validateStatusLine(uri, huc.getResponseCode(), huc.getResponseMessage(),
                        new DefaultErrorProvider(huc));
            }
            
        } catch (final MalformedURLException e) {
            throw new TransportGnipException(e);
        } catch (final IOException e) {
            throw new TransportGnipException(e);
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (final IOException e) {
                // Nothing to be done here!
            }
        }
    }
    
    @Override
    public final void deleteResource(final URI uri, final Object resource) throws AuthenticationGnipException,
            TransportGnipException {
        
        OutputStream outStream = null;
        try {
            final URLConnection uc = uri.toURL().openConnection();
            HttpURLConnection huc = null;
            
            if (uc instanceof HttpURLConnection) {
                huc = (HttpURLConnection) uc;
                huc.setRequestMethod("DELETE"); //to set HTTP method to DELETE
            }
            uc.setAllowUserInteraction(false);
            uc.setDefaultUseCaches(false);
            uc.setConnectTimeout(connectTimeout);
            uc.setReadTimeout(readTimeout);
            uc.setDoOutput(true); // Needed in order to make a POST request
            uc.setRequestProperty("Accept-Encoding", "gzip, deflate"); 
            uc.setRequestProperty("User-Agent", USER_AGENT);
            uc.setRequestProperty("Authorization", "Basic " + encoder.encode(authentication));
            uc.setRequestProperty("Content-type", "application/json");
            doConfiguration(uc);
            
            outStream = uc.getOutputStream();
            outStream.write(new ObjectMapper().writeValueAsString(resource).getBytes("UTF-8"));
            
            if (huc != null) {
                validateStatusLine(uri, huc.getResponseCode(), huc.getResponseMessage(), new DefaultErrorProvider(huc));
            }
            
        } catch (final MalformedURLException e) {
            throw new TransportGnipException(e);
        } catch (final IOException e) {
            throw new TransportGnipException(e);
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (final IOException e) {
                // Nothing to be done here!
            }
        }
    }  

    /** template method for configuring the URLConnection */
    protected void doConfiguration(final URLConnection uc) {
        
    }
    
    static final String toInputStream(final InputStream is) {
        if(is == null){
            return "";
        }

        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final byte []buff = new byte[4096];
            int i;
            while((i = is.read(buff)) >= 0) {
                bos.write(buff, 0, i);
            }
            
            return new String(bos.toByteArray(), "utf-8");
        } catch(final IOException e) {
            return "";
        }
    }
    static final ObjectMapper m = new ObjectMapper();
    
    static class DefaultErrorProvider implements ErrorProvider {
        private final HttpURLConnection huc;
        
        /** Creates the DefaultErrorProvider. */
        public DefaultErrorProvider(final HttpURLConnection huc) {
            this.huc = huc;
        }
        
        @Override
        public Errors getError() {
            try {
                final InputStream is = JRERemoteResourceProvider.getRealInputStream(huc, huc.getErrorStream());
                if(huc.getContentType() != null && huc.getContentType().startsWith("application/json")) {
                    return m.readValue(is, Errors.class);
                } else {
                    final Errors errors = new Errors();
                    final Error error = new Error();
                    error.setMessage(toInputStream(is));
                    
                    return errors;
                }
            } catch (final IOException e) {
                logger.warn("Exception trying to read error message ", e);
                return null;
            }
        }
    }
}
