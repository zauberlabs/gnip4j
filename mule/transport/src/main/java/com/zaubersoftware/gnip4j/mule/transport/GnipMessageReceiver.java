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

import org.apache.commons.lang.UnhandledException;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractMessageReceiver;
import org.mule.transport.ConnectException;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * <code>GnipMessageReceiver</code> TODO document
 */
public class GnipMessageReceiver extends AbstractMessageReceiver {
    private GnipStream stream;
    
    /** constructor */
    public GnipMessageReceiver(final Connector connector,
            final FlowConstruct flowConstruct, final InboundEndpoint endpoint)
            throws CreateException {
        super(connector, flowConstruct, endpoint);
    }

    @Override
    public final void doConnect() throws ConnectException {
        final String subdomain = getEndpoint().getEndpointURI().getAuthority();
        final long collectorId = Long.parseLong(getEndpoint().getEndpointURI().getPath().replaceAll("^[/]", ""));
        
        stream = getGnipFacade().createStream(subdomain, collectorId, new StreamNotificationAdapter() {
            @Override
            public void notify(final Activity activity, final GnipStream stream) {
                try {
                    routeMessage(createMuleMessage(activity));
                } catch (final MuleException e) {
                    throw new UnhandledException(e);
                }
            }
        });
    }

    public final GnipFacade getGnipFacade() {
        return ((GnipConnector) getConnector()).getFacade();
    }
    @Override
    public void doDisconnect() throws ConnectException {
        // void

    }

    @Override
    public void doStart() {
        // void
    }

    @Override
    public void doStop() {
        // void
    }

    @Override
    public final void doDispose() {
        stream.close();
    }

}
