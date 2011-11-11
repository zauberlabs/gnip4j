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
