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

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 */
public final class Provider {
    private String displayName;
    private String objectType;
    private String link;

    /**
     * Gets the value of the displayName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDisplayName(final String value) {
        displayName = value;
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setObjectType(final String value) {
        objectType = value;
    }

    /**
     * Gets the value of the link property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLink(final String value) {
        link = value;
    }

}
