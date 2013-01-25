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
package com.zaubersoftware.gnip4j.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 * 
 * 
 * @author Juan F. Codagnone
 * @since May 28, 2011
 */
public class GnipUrl {
    private String url;

    @JsonProperty(value = "expanded_url") private String expandedUrl;

    public final String getUrl() {
        return url;
    }

    public final GnipUrl setUrl(final String url) {
        this.url = url;
        return this;
    }

    public final String getExpandedUrl() {
        return expandedUrl;
    }

    public final GnipUrl setExpandedUrl(final String expandedUrl) {
        this.expandedUrl = expandedUrl;
        return this;
    }
}
