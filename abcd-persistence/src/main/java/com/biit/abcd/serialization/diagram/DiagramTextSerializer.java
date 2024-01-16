package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramTextSerializer extends StorableObjectSerializer<DiagramText> {

    @Override
    public void serialize(DiagramText src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("text", String.valueOf(src.getText()));
        jgen.writeStringField("fill", String.valueOf(src.getFill()));
        //As comes from jscript
        jgen.writeStringField("font_size", String.valueOf(src.getFontSize()));
        jgen.writeStringField("stroke", String.valueOf(src.getStroke()));
        //As comes from jscript
        jgen.writeStringField("stroke_width", String.valueOf(src.getStrokeWidth()));
    }
}
