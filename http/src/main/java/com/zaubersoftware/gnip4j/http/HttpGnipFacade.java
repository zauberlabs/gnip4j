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

import static com.zaubersoftware.gnip4j.http.ErrorCodes.ERROR_NULL_HTTPCLIENT;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpVersion;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.ClientParamBean;
import org.apache.http.conn.params.ConnManagerParamBean;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
/**
 * Http implementation for the {@link GnipFacade}  
 * 
 * @author Guido Marucci Blas
 * @since Apr 29, 2011
 */
public class HttpGnipFacade implements GnipFacade {
    private static final String USER_AGENT = "Gnip4j (https://github.com/zaubersoftware/gnip4j/)";
    private final DefaultHttpClient client;
    
    /** Creates the HttpGnipFacade. */
    public HttpGnipFacade() {
        this(createHttpClient());
    }

    /**
     * Creates the HttpGnipFacade.
     *
     * @param client
     */
    public HttpGnipFacade(@NotNull final DefaultHttpClient client) {
        if(client == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        
        this.client = client;
    }

    @Override
    public final GnipStream createStream(
            @NotNull final String domain,
            @NotNull final long dataCollectorId, 
            @NotNull final GnipAuthentication auth) {
        //TODO Add caching
        return new HttpGnipStream(client, domain, dataCollectorId, auth);
    }
    
    /** create the default http client */
    private static DefaultHttpClient createHttpClient() {
        final DefaultHttpClient  client = new DefaultHttpClient();
        
        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
        client.setCredentialsProvider(credsProvider);
        final HttpParams params = client.getParams();
        
        final HttpProtocolParamBean httpProtocol = new HttpProtocolParamBean(params);
        httpProtocol.setContentCharset("UTF-8");
        httpProtocol.setUserAgent(USER_AGENT);
        httpProtocol.setVersion(HttpVersion.HTTP_1_1);

        final HttpConnectionParamBean bean = new HttpConnectionParamBean(params);
        bean.setConnectionTimeout(60 * 10000); // timeout in milliseconds until a connection is established.
        bean.setSoTimeout(60000); // a maximum period inactivity between two consecutive data packets
        bean.setSocketBufferSize(8192); // the internal socket buffer used to buffer data while 
                                        // receiving / transmitting HTTP messages. 
        bean.setTcpNoDelay(true); // http.connection.stalecheck. overhead de 30ms 
        bean.setStaleCheckingEnabled(true); // determines whether Nagle's algorithm is to be used 
        bean.setLinger(-1); // sets SO_LINGER with the specified linger time in seconds

        final ClientParamBean clientParam = new ClientParamBean(params);
        clientParam.setHandleRedirects(true);
        clientParam.setRejectRelativeRedirect(true);
        clientParam.setMaxRedirects(5);
        clientParam.setAllowCircularRedirects(false);
        
//        final ConnRouteParamBean connRoute = new ConnRouteParamBean(params);
        // TODO proxy settings
        
        final ConnManagerParamBean connManager = new ConnManagerParamBean(params);
        connManager.setMaxTotalConnections(20);
        
        // TODO GZIP saves bandwith but delays reception (compression buffers)
        final GZipInterceptor gzip = new GZipInterceptor();
        client.addRequestInterceptor(gzip);
        client.addResponseInterceptor(gzip);
        return client;
    }
}
