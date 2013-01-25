/**
 * Copyright (c) 2013 Rincaro IP Holding Company
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class GeoSerializer extends StdSerializer<Geo> {

	public GeoSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);

	}

	@Override
	public void serialize(Geo value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
		if( value.getType().equals("Point") ) {
			jgen.writeStartObject();
			jgen.writeFieldName("type");
			jgen.writeString(Geometries.Point.name());
			jgen.writeFieldName("coordinates");
			jgen.writeStartArray();
			jgen.writeNumber(((Point)(value.getCoordinates())).getLatitude());
			jgen.writeNumber(((Point)(value.getCoordinates())).getLongitude());
			jgen.writeEndArray();
			jgen.writeEndObject();
		} else {
			Polygon polygon = (Polygon) (value.getCoordinates());
			jgen.writeStartObject();
			jgen.writeFieldName("type");
			jgen.writeString(Geometries.Polygon.name());
			jgen.writeFieldName("coordinates");
			jgen.writeStartArray();
			jgen.writeStartArray();
			for( Point p : polygon.getPoints() ) {
				jgen.writeStartArray();
				jgen.writeNumber(p.getLatitude());
				jgen.writeNumber(p.getLongitude());
				jgen.writeEndArray();
			}
			jgen.writeEndArray();
			jgen.writeEndArray();
			jgen.writeEndObject();
		}
		
	}

}