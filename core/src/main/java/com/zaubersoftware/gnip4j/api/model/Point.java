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

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * TODO Descripcion de la clase. Los comentarios van en castellano.
 * 
 * 
 * @author Martin Silva
 * @since Feb 15, 2012
 */

@JsonAutoDetect(getterVisibility=Visibility.PUBLIC_ONLY)
public class Point implements Geometry{
    
    /**
     * Creates the Point.
     *
     */
    private Point() {
    }
    
    /**
     * Creates the Point.
     *
     * @param latitude
     * @param longitude
     */
    public Point(double latitude, double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }




    private double latitude;
    private double longitude;
    
    
    
    /**
     * Returns the latitude.
     * 
     * @return <code>double</code> with the latitude.
     */
    public double getLatitude() {
        return latitude;
    }
    
    
    /**
     * Returns the longitude.
     * 
     * @return <code>double</code> with the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /** @see com.zaubersoftware.gnip4j.api.model.Geometry#getType() */
    @Override
    public Geometries getType() {
        return Geometries.Point;
    }
    
    
    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "lat: " + latitude + " lon: " + longitude;
    }
}
