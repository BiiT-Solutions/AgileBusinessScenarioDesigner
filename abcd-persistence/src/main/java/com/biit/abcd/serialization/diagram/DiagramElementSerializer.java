package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramElementSerializer<T extends DiagramElement> extends DiagramObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("tooltip", src.getTooltip());
        jgen.writeObjectField("size", src.getSize());
        jgen.writeObjectField("position", src.getPosition());
        jgen.writeStringField("angle", String.valueOf(src.getAngle()));
        jgen.writeObjectField("text", src.getText());
    }
}
