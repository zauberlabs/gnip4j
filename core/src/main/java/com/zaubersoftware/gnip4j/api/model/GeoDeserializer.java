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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Geo Deserializer
 *
 *
 * @author Martin Silva
 * @since Feb 15, 2012
 */
public class GeoDeserializer extends StdDeserializer<Geo> {

    /** Creates the GeoDeserializer. */
    public GeoDeserializer(final Class<Geo> clazz) {
        super(clazz);
    }

    @Override
    public Geo deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        final JsonNode tree = jp.readValueAsTree();

        final JsonNode coordinates = tree.get("coordinates");
        final JsonNode type = tree.get("type");


        final Geo geo = new Geo();
        geo.setType(type.textValue());

        if(Geometries.Polygon.equals(Geometries.valueOf(type.textValue()))) {
            geo.setCoordinates(createPolygon(coordinates));
        } else {
            geo.setCoordinates(createPoint(coordinates));
        }

        return geo;
    }

    /** @return a point */
    private Point createPoint(final JsonNode coordinates) throws IOException {
        final Point ret;
        if(coordinates.isArray()) {
            ret = new Point(coordinates.get(0).doubleValue(), coordinates.get(1).doubleValue());
        } else {
            ret = null;
        }
        return ret;
    }

    /** @return a polygon */
    private Polygon createPolygon(final JsonNode linearrings) throws IOException {
        final List<LinearRing> linearRings = new ArrayList<>(linearrings.size());
        
        for(final Iterator<JsonNode> it = linearrings.elements(); it.hasNext() ; ) {
            final JsonNode linearRingNode = it.next();
            linearRings.add(parseLinearRing(linearRingNode));
                
        }

        return new Polygon(linearRings);
    }

    private LinearRing parseLinearRing(final JsonNode linearRingNode) throws IOException {
        final List<Point> points = new ArrayList<>(linearRingNode.size());
        for(final Iterator<JsonNode> it = linearRingNode.elements(); it.hasNext() ; ) {
            final JsonNode next = it.next();
            points.add(createPoint(next));
        }
        return new LinearRing(points);
        
    }

}
