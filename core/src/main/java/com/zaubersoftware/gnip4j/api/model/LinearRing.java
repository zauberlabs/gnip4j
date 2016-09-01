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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * LinearRing  
 * 
 * @author Juan F. Codagnone
 * @since Jul 24, 2015
 */
public class LinearRing implements  Iterable<Point>, Serializable {
    private List<Point> coordinates;

    public LinearRing() {
        // void
    }
    
    public LinearRing(final Point ... coordinates) {
        this(Arrays.asList(coordinates));
    }
    
    public LinearRing(final List<Point> coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
    public Iterator<Point> iterator() {
        return coordinates.iterator();
    }
    
    public final List<Point> getCoordinates() {
        return coordinates;
    }

    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        
        for(final Point p : this.coordinates) {
            builder.append("[ ")
                .append(p.toString())
                .append(" ]");
        }
        
        builder.append("]");
        
        return builder.toString();
    }
    
    @Override
    public boolean equals(final java.lang.Object obj) {
        boolean ret = false;
        
        if(this == obj) {
            ret = true;
        } else if(obj instanceof LinearRing) {
            ret = coordinates.equals(((LinearRing)obj).coordinates);
        }
        
        return ret;
    }
    
    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

}
