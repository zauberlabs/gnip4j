/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
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

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since May 28, 2011
 */
public class Url implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;

    @JsonProperty(value = "expanded_url")
    private String expandedUrl;

    @JsonProperty(value = "expanded_status")
    private Integer expandedStatus;

    public final String getUrl() {
        return url;
    }

    public final void setUrl(final String url) {
        this.url = url;
    }

    public final String getExpandedUrl() {
        return expandedUrl;
    }

    public final void setExpandedUrl(final String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    public Integer getExpandedStatus() {
        return expandedStatus;
    }

    public void setExpandedStatus(final Integer expandedStatus) {
        this.expandedStatus = expandedStatus;
    }

}
