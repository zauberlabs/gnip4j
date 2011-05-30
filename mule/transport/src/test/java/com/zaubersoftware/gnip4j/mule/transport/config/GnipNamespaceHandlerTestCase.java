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
package com.zaubersoftware.gnip4j.mule.transport.config;

import org.mule.construct.SimpleFlowConstruct;
import org.mule.tck.FunctionalTestCase;

/**
 * TODO
 */
public final class GnipNamespaceHandlerTestCase extends FunctionalTestCase {
    @Override
    protected String getConfigResources() {
        // TODO You'll need to edit this file to configure the properties
        // specific to your transport
        return "gnip-namespace-config.xml";
    }

    public void testGnipConfig() throws Exception {
        lookupFlowConstruct("xxx").process(getTestEvent("x"));
        System.in.read();
    }

    private SimpleFlowConstruct lookupFlowConstruct(final String name) {
        return (SimpleFlowConstruct) muleContext.getRegistry()
                .lookupFlowConstruct(name);
    }

}
