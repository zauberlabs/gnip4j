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

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Twitter Video Info
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 3, 2015
 */
public class VideoInfo {
    @JsonProperty(value = "duration_millis")
    private Long durationMillis;
    private List<TwitterVideoVariant> variants;
    @JsonProperty(value = "aspect_ratio")
    private List<Integer> aspectRatio;
    public Long getDurationMillis() {
        return durationMillis;
    }
    public void setDurationMillis(final Long durationMillis) {
        this.durationMillis = durationMillis;
    }
    
    public List<TwitterVideoVariant> getVariants() {
        return variants;
    }
    public void setVariants(final List<TwitterVideoVariant> variants) {
        this.variants = variants;
    }
    public List<Integer> getAspectRatio() {
        return aspectRatio;
    }
    public void setAspectRatio(final List<Integer> aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
}
