/*
 * $Id$
 * --------------------------------------------------------------------------
 * ------------ Copyright (c) MuleSoft, Inc. All rights reserved.
 * http://www.mulesoft.com
 * 
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.zaubersoftware.gnip4j.mule.transport;

import java.sql.Date;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.transport.AbstractConnector;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.InmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

/**
 * <code>GnipConnector</code> TODO document
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
                new InmutableGnipAuthentication(username, password)));

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
