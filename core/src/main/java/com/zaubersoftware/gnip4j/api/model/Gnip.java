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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 */
public class Gnip {
    private Language language;
    
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty(value = "matching_rules") private List<MatchingRules> matchingRules = new ArrayList<MatchingRules>();
	
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<Url> urls = new ArrayList<Url>();
    
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty(value = "klout_score")
	private Long kloutScore;


	public void clear() {
		language = null;
		urls.clear();
		matchingRules.clear();
		kloutScore = null;
	}
	
    /**
     * Gets the value of the language property.
     * 
     * @return possible object is {@link Language }
     * 
     */
    public final Language getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *            allowed object is {@link Language }
     * 
     */
    public final void setLanguage(final Language value) {
        language = value;
    }

    /**
     * Gets the value of the matchingRules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the matchingRules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getMatchingRules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MatchingRules }
     * 
     * 
     */
    public final List<MatchingRules> getMatchingRules() {
        return matchingRules;
    }

    public final List<Url> getUrls() {
        return urls;
    }

    public final Long getKloutScore() {
        return kloutScore;
    }

    public void setMatchingRules(List<MatchingRules> matchingRules) {
        this.matchingRules = matchingRules;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public void setKloutScore(Long kloutScore) {
        this.kloutScore = kloutScore;
    }

}
