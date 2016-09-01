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
 */
public class Urls implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Integer> indices;
    @JsonProperty(value = "expanded_url")
    private String expandedUrl;
    private String url;
    @JsonProperty("display_url")
    private String displayUrl;
    
    
    /**
     * Gets the value of the indices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getIndices() {
        if (indices == null) {
            indices = new ArrayList<Integer>();
        }
        return this.indices;
    }

    /**
     * Gets the value of the expandedUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpandedUrl() {
        return expandedUrl;
    }

    /**
     * Sets the value of the expandedUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpandedUrl(final String value) {
        this.expandedUrl = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(final String value) {
        this.url = value;
    }

    /**
     * Returns the displayUrl.
     * 
     * @return <code>String</code> with the displayUrl.
     */
    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(final String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public void setIndices(final List<Integer> indices) {
        this.indices = indices;
    }
    
    
}
