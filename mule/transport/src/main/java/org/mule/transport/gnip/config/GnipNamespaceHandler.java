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
package org.mule.transport.gnip.config;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.transport.gnip.GnipConnector;
import org.mule.endpoint.URIBuilder;

/**
 * Registers a Bean Definition Parser for handling <code><gnip:connector></code>
 * elements and supporting endpoint elements.
 */
public class GnipNamespaceHandler extends AbstractMuleNamespaceHandler {
    @Override
    public final void init() {
        registerStandardTransportEndpoints(GnipConnector.GNIP, URIBuilder.PATH_ATTRIBUTES);
        registerConnectorDefinitionParser(GnipConnector.class);
    }
}
