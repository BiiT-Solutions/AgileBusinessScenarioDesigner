package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Point;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class PointSerializer extends CustomSerializer<Point> {

    @Override
    public void serialize(Point src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("x", String.valueOf(src.getX()));
        jgen.writeStringField("y", String.valueOf(src.getY()));
    }
}
