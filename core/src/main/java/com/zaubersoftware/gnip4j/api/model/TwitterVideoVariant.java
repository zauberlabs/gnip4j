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

import org.codehaus.jackson.annotate.JsonProperty;

public class TwitterVideoVariant {
    private Integer bitrate;
    @JsonProperty(value = "content_type")
    private String contentType;
    private String url;

    public Integer getBitrate() {
        return bitrate;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setBitrate(final Integer bitrate) {
        this.bitrate = bitrate;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

}
