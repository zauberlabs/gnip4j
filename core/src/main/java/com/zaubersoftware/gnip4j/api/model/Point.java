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

@JsonAutoDetect(getterVisibility = Visibility.PUBLIC_ONLY)
public class Point implements Geometry {

    /** Creates the Point.*/
    @SuppressWarnings("unused")
    private Point() {
    }


    public Point(final double latitude, final double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }




    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public Geometries getType() {
        return Geometries.Point;
    }

    @Override
    public String toString() {
        return "lat: " + latitude + " lon: " + longitude;
    }
}
