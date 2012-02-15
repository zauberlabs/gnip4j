/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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

import org.apache.commons.lang.UnhandledException;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractMessageReceiver;
import org.mule.transport.ConnectException;
import org.mule.util.UriParamFilter;

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
        String uri = getEndpointURI().getPath();
        
        String account = uri.substring(uri.indexOf("accounts/") + "accounts/".length(), uri.indexOf("/publishers"));
        String streamName = uri.substring(uri.indexOf("streams/track/") + "streams/track/".length(), uri.indexOf(".json"));
        
        stream = getGnipFacade().createStream(account, streamName, new StreamNotificationAdapter() {
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
