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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;

import ar.com.zauber.commons.validate.Validate;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;

/**
 * Http implementation for the {@link GnipFacade}  
 * 
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class HttpGnipFacade implements GnipFacade {
    private String domain;
    private final DefaultHttpClient client = new DefaultHttpClient();
    private final CredentialsProvider credsProvider = new BasicCredentialsProvider();

    /**
     * Creates the HttpGnipFacade.
     *
     */
    public HttpGnipFacade(final String domain) {
        Validate.notBlank(domain, "you must provide a valid domain");
        
        this.domain = domain;
        client.setCredentialsProvider(credsProvider);
        
        final HttpParams params = client.getParams();
        new HttpConnectionParamBean(params);
        params.setBooleanParameter("handleRedirect", true);
        
    }

    @Override
    public final GnipStream createStream(final GnipAuthentication auth) {
        credsProvider.setCredentials(
                new AuthScope(domain + ".gnip.com", AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword()));
        return new HttpGnipStream(handshake());
    }
    
    /**
     * @return 
     * 
     */
    private  HttpResponse handshake() {
        final HttpGet get = new HttpGet("https://" + domain + ".gnip.com/data_collectors/1/track.json");
        try {
            final HttpResponse response  = client.execute(get);
            
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            if(statusCode == 401) {
                throw new AuthenticationGnipException(statusLine.getReasonPhrase());
            } else if(statusCode == 200) {
                return response;
            } else {
                System.out.println(statusCode);
                System.out.println(statusCode);
                throw new TransportGnipException("... TODO ....");
            }
        } catch (ClientProtocolException e) {
            throw new NotImplementedException(e);
        } catch (IOException e) {
            throw new NotImplementedException(e);
        }
        
    }

    /**
     * @param response
     * @return
     */
    private Object HttpGnipStream(HttpResponse response) {
        // TODO: Auto-generated method stub
        return null;
    }


}
