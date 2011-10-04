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
package org.mule.transport.gnip;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

/**
 * <code>GnipConnector</code> 
 */
public class GnipConnector extends AbstractConnector {
    /* This constant defines the main transport protocol identifier */
    public static final String GNIP = "gnip";
    private GnipFacade facade;
    private String username;
    private String password;

    public final GnipFacade getFacade() {
        return facade;
    }
    
    public GnipConnector(final MuleContext context) {
        super(context);
        registerSupportedProtocol(GNIP);
    }

    @Override
    public final void doInitialise() throws InitialisationException {
        facade = new DefaultGnipFacade(new JRERemoteResourceProvider(
                new ImmutableGnipAuthentication(username, password)));

    }

    @Override
    public void doConnect() throws Exception {
        // void
    }

    @Override
    public void doDisconnect() throws Exception {
     // void
    }

    @Override
    public void doStart() throws MuleException {
     // void
    }

    @Override
    public void doDispose() {
        // void
    }

    @Override
    public final String getProtocol() {
        return GNIP;
    }

    @Override
    protected void doStop() throws MuleException {
        // void
        
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String username) {
        this.username = username;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }
    
    
}
