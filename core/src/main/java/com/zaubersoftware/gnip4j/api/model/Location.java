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

/**
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 20, 2015
 */
public class Location implements Serializable {
    // San Fernando, California, United States
    private String displayName;
    private Address address;
    private Geo geo;
    
    // place
    private String objectType;
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(final Address address) {
        this.address = address;
    }
    public Geo getGeo() {
        return geo;
    }
    public void setGeo(final Geo geo) {
        this.geo = geo;
    }
    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }
}
