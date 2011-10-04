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

import static com.zaubersoftware.gnip4j.api.impl.ErrorCodes.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;


import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientParamBean;
import org.apache.http.conn.params.ConnManagerParamBean;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.zaubersoftware.gnip4j.api.GnipAuthentication;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.support.http.AbstractRemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since May 21, 2011
 */
public class HttpClientRemoteResourceProvider extends AbstractRemoteResourceProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DefaultHttpClient client;
    private final GnipAuthentication authentication;

    /** Creates the HttpGnipFacade. */
    public HttpClientRemoteResourceProvider(final GnipAuthentication authentication) {
        this(createHttpClient(), authentication);
    }

    /** Creates the HttpGnipFacade. */
    public HttpClientRemoteResourceProvider(
            final DefaultHttpClient client,
            final GnipAuthentication authentication) {
        if(client == null) {
            throw new IllegalArgumentException(ERROR_NULL_HTTPCLIENT);
        }
        if(authentication == null) {
            throw new IllegalArgumentException(ERROR_NULL_AUTH);
        }

        this.authentication = authentication;
        this.client = client;
    }


    @Override
    public final InputStream getResource(final URI uri)
        throws AuthenticationGnipException, TransportGnipException {
        logger.debug("Setting up connection for {}", uri);
        final DefaultHttpClient dclient = client;
        final CredentialsProvider credentialsProvider = dclient.getCredentialsProvider();

        logger.trace("\t-- Setting Gnip credentials. User {}", authentication.getUsername());
        credentialsProvider.setCredentials(
                new AuthScope(uri.getHost(), AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(authentication.getUsername(), authentication.getPassword()));

        final HttpGet get = new HttpGet(uri);
        try {
            logger.trace("\t-- Executing get request to URI {}", uri);
            final HttpResponse response  = client.execute(get);

            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            logger.trace("\t-- Response status code {} for {}", statusCode, uri);
            validateStatusLine(uri, statusCode, statusLine.getReasonPhrase());
            return new HttpResponseReleaseInputStream(response);
        } catch (final ClientProtocolException e) {
            throw new TransportGnipException("Protocol error", e);
        } catch (final IOException e) {
            throw new TransportGnipException("Error", e);
        }
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

	@Override
	public void postResource(URI uri, Object resource)
			throws AuthenticationGnipException, TransportGnipException {
		
		logger.debug("Setting up a POST request for {}", uri);
		final DefaultHttpClient dclient = client;
		final CredentialsProvider credentialsProvider = dclient.getCredentialsProvider();
		
        logger.trace("\t-- Setting Gnip credentials. User {}", authentication.getUsername());
        credentialsProvider.setCredentials(
        		new AuthScope(uri.getHost(), AuthScope.ANY_PORT),
        		new UsernamePasswordCredentials(authentication.getUsername(),authentication.getPassword()));
        
        HttpResponse response = null;
        try {
	        final HttpPost post = new HttpPost(uri);
	        post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(resource)));
	        post.setHeader("Content-type", "application/json");
	        
	        response = dclient.execute(post);
	        
	        final StatusLine statusLine = response.getStatusLine();
	        final int statusCode = statusLine.getStatusCode();
	        logger.trace("\t-- Response status code {} for POST {}", statusCode, uri);
	        validateStatusLine(uri, statusCode, statusLine.getReasonPhrase());
        } catch (JsonGenerationException e) {
			throw new TransportGnipException(e);
		} catch (JsonMappingException e) {
			throw new TransportGnipException(e);
		} catch (UnsupportedEncodingException e) {
			throw new TransportGnipException(e);
		} catch (IOException e) {
			throw new TransportGnipException(e);
		} finally {
			// We need to fully read in this response in order to release the connection
			// back to the SingleClientConnManager.
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				logger.warn("Wasn't able to completely read POST response.");
			}
        }
        
	}
}
