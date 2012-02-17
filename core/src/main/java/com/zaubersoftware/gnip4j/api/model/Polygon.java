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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * @author Martin Silva
 * @since Feb 15, 2012
 */

@JsonAutoDetect(getterVisibility=Visibility.PUBLIC_ONLY)
public class Polygon implements Geometry, Iterable<Point> {
    
    private List<Point> points;
    
    
    /**
     * Creates the Polygon.
     *
     */
    
    Polygon() {
    }
    
    /**
     * Creates the Polygon.
     *
     */
    public Polygon(List<Point> points) {
        this.points = points;
    }
    
    
    public Polygon(Point points[]) {
        this.points = Arrays.asList(points);
    }
    

    /** @see java.lang.Iterable#iterator() */
    @Override
    public Iterator<Point> iterator() {
        return this.points.iterator();
    }
    
    
    /**
     * Returns the points.
     * 
     * @return <code>List<Point></code> with the points.
     */
    public List<Point> getPoints() {
        return points;
    }

    /** @see com.zaubersoftware.gnip4j.api.model.Geometry#getType() */
    @Override
    public Geometries getType() {
        return Geometries.Polygon;
    }
    
    
    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        
        for (Point p : this.points) {
            builder.append("[ " + p.toString() + " ]");
        }
        
        builder.append("]");
        
        return builder.toString();
    }
}
