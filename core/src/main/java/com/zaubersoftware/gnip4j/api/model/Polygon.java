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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * @author Martin Silva
 * @since Feb 15, 2012
 */

@JsonAutoDetect(getterVisibility = Visibility.PUBLIC_ONLY)
public class Polygon implements Geometry, Iterable<LinearRing> {
    private List<LinearRing> holes;
    
    Polygon() {
    }
    
    /** Creates the Polygon. */
    public Polygon(final List<LinearRing> holes) {
        this.holes = holes;
    }
    
    
    public Polygon(final LinearRing ...holes) {
        this(Arrays.asList(holes));
    }
    

    @Override
    public final Iterator<LinearRing> iterator() {
        return this.holes.iterator();
    }
    
    
    public final List<LinearRing> getHoles() {
        return holes;
    }

    @Override
    public final Geometries getType() {
        return Geometries.Polygon;
    }
    
    
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        
        for (final LinearRing p : this.holes) {
            builder.append(p.toString());
        }
        
        builder.append(']');
        
        return builder.toString();
    }
    
    @Override
    public boolean equals(final java.lang.Object obj) {
        boolean ret = false;
        
        if(obj == this) {
            ret = true;
        } else if(obj instanceof Polygon) {
            final Polygon p = (Polygon) obj;
            ret = holes.equals(p.holes);
        }
        
        return ret;
    }
    
    @Override
    public int hashCode() {
        return holes.hashCode();
    }
}
