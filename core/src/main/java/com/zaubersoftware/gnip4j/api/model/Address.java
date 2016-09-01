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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Juan F. Codagnone
 * @since Mar 20, 2015
 */
public class Address implements Serializable {
    // Los Angeles County
    private String subRegion;
    // California
    private String region;
    // San Fernando
    private String locality;
    // US
    private String countryCode;
    // United States
    private String country;

    @JsonIgnore
    public boolean isOnlyCountry() { 
        return countryCode != null && region == null && locality == null; 
    }
    
    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(final String subRegion) {
        this.subRegion = subRegion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }


}
