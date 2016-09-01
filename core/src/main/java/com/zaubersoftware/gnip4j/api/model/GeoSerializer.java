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

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @see GeoDeserializer
 *
 * @author Juan F. Codagnone
 * @since Jul 24, 2015
 */
public class GeoSerializer extends JsonSerializer<Geo> {

    @Override
    public void serialize(
            final Geo value, 
            final JsonGenerator jgen, 
            final SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();

        jgen.writeStringField("type", value.getType());
        final Geometry geo = value.getCoordinates();
        if(null != geo) {
            jgen.writeArrayFieldStart("coordinates");
            if(geo instanceof Point) {
                final Point p = (Point) geo;
                jgen.writeNumber(p.getLatitude());
                jgen.writeNumber(p.getLongitude());
            } else if(geo instanceof Polygon) {
                final Polygon p = (Polygon) geo;
                for(final LinearRing ring : p.getHoles()) {
                    jgen.writeStartArray();
                    for(final Point point : ring.getCoordinates()) {
                        jgen.writeStartArray();
                        jgen.writeNumber(point.getLatitude());
                        jgen.writeNumber(point.getLongitude());
                        jgen.writeEndArray();
                    }
                    jgen.writeEndArray();
                }
            }
            
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

}
