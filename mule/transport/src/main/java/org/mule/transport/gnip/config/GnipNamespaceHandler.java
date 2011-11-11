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
