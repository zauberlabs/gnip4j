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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
public class Gnip {
    private Language language;
    @JsonProperty(value = "matching_rules")
    private List<MatchingRules> matchingRules;
    private List<Url> urls;
    @JsonProperty(value = "klout_score")
    private Float kloutScore;
    @JsonProperty(value = "favorite_count")
    private Long favoriteCount;

    public final Language getLanguage() {
        return language;
    }

    public final void setLanguage(final Language value) {
        language = value;
    }

    /** Gets the value of the matchingRules property.*/
    @JsonProperty(value = "matching_rules")
    public final List<MatchingRules> getMatchingRules() {
        if (matchingRules == null) {
            matchingRules = new ArrayList<MatchingRules>();
        }
        return matchingRules;
    }

    public final List<Url> getUrls() {
        if (urls  == null) {
            urls  = new ArrayList<Url>();
        }
        return urls;
    }
    
    public final Float getKloutScore() {
    	return kloutScore;
    }
    
    public final Long getFavoriteCount() {
        return favoriteCount;
    }
    
    public final void setFavoriteCount(final Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
