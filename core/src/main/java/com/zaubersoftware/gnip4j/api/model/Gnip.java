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
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
public class Gnip implements Serializable {
    private static final long serialVersionUID = -928243859838308139L;
    
    private Language language;
    @JsonProperty(value = "matching_rules")
    private List<MatchingRules> matchingRules;
    private List<Url> urls;
    @JsonProperty(value = "klout_score")
    private Float kloutScore;
    @JsonProperty(value = "favorite_count")
    private Long favoriteCount;
    @JsonProperty(value = "profileLocations")
    private List<Location> profileLocations;
    
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
            matchingRules = new ArrayList<>();
        }
        return matchingRules;
    }

    public void setMatchingRules(final List<MatchingRules> matchingRules) {
        this.matchingRules = matchingRules;
    }

    public final List<Url> getUrls() {
        if (urls  == null) {
            urls  = new ArrayList<>();
        }
        return urls;
    }

    public void setUrls(final List<Url> urls) {
        this.urls = urls;
    }
    
    public final Float getKloutScore() {
    	return kloutScore;
    }

    public void setKloutScore(final Float kloutScore) {
        this.kloutScore = kloutScore;
    }
    
    public final Long getFavoriteCount() {
        return favoriteCount;
    }
    
    public final void setFavoriteCount(final Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public List<Location> getProfileLocations() {
        if (profileLocations == null) {
            profileLocations = new ArrayList<>();
        }
        return profileLocations;
    }

}
