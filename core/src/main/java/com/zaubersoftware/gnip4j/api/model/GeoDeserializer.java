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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * TODO Descripcion de la clase. Los comentarios van en castellano.
 * 
 * 
 * @author Martin Silva
 * @since Feb 15, 2012
 */
public class GeoDeserializer extends StdDeserializer<Geo> {

    /**
     * Creates the GeoDeserializer.
     * 
     * @param vc
     */
    public GeoDeserializer(Class<Geo> clazz) {
        super(clazz);
    }

    /**
     * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser,
     *      org.codehaus.jackson.map.DeserializationContext)
     */
    @Override
    public Geo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode tree = jp.readValueAsTree();

        JsonNode coordinates = tree.findValue("coordinates");
        JsonNode type = tree.findValue("type");

        Geo geo = new Geo();
        geo.setType(type.asText());

        if (Geometries.valueOf(type.asText()) == Geometries.Polygon) {
            geo.setCoordinates(this.createPolygon(coordinates));
        } else {
            geo.setCoordinates(this.createPoint(coordinates));
        }

        return geo;
    }

    /**
     * @param coordinates
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    private Geometry createPoint(JsonNode coordinates) throws JsonParseException, IOException {
        return new Point(coordinates.get(0).asDouble(), coordinates.get(1).asDouble());
    }

    /**
     * @param coordinates
     * @return
     */
    private Geometry createPolygon(JsonNode coordinates) {
        JsonNode values = (ArrayNode) coordinates.get(0);
        Iterator<JsonNode> elements = values.iterator();

        List<Point> points = new ArrayList<Point>();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            points.add(new Point(next.get(0).asDouble(), next.get(1).asDouble()));
        }

        return new Polygon(points);
    }

}
