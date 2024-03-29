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
import java.util.Objects;


/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 */
public class Geo implements Serializable {
    private Geometry coordinates;
    private String type;

    /** Creates the Geo. */
    public Geo() {
    }

    public Geo(final String type, final Geometry geometry) {
        setType(type);
        setCoordinates(geometry);
    }
    
    public final Geometry getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(final Geometry coordinates) {
        this.coordinates = coordinates;
    }

    public final String getType() {
        return type;
    }

    public final void setType(final String value) {
        type = value;
    }

    @Override
    public String toString() {
        return "{\"type\": \"" + type + "\", \"coordinates\": " + coordinates + "}";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
            coordinates,
            type
        );
    }

    @Override
    public boolean equals(final java.lang.Object obj) {
        boolean ret = false;
        
        if(this == obj) {
            ret = true;
        } else if(obj instanceof Geo) {
            final Geo other = (Geo) obj;
            ret = Objects.equals(type, other.type)
              && Objects.equals(coordinates, other.coordinates);
        }
        return ret;
    }
}
