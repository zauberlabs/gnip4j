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
package com.zaubersoftware.gnip4j.api;

import java.net.URI;

/**
 * An strategy to generate {@link URI}s to connect against a Gnip endpoint.
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public interface UriStrategy {

    /**
     * Generates a {@link URI} to connect against a Gnip endpoint to consume the activity stream.
     *
     * @param domain
     * @param dataCollectorId
     * @return
     */
    URI createStreamUri(String domain, long dataCollectorId);

    /**
     * Generates a {@link URI} to connect against a Gnip endpoint to get/modify rules.
     *
     * @param domain
     * @param dataCollectorId
     * @return
     */
    URI createRulesUri(String domain, long dataCollectorId);
}
