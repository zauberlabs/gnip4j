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