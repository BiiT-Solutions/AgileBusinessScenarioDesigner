package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.form.jackson.serialization.CustomSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DiagramTextSerializer extends CustomSerializer<DiagramText> {

    @Override
    public void serialize(DiagramText src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("text", String.valueOf(src.getText()));
        jgen.writeStringField("fill", String.valueOf(src.getFill()));
        jgen.writeStringField("fontSize", String.valueOf(src.getFontSize()));
        jgen.writeStringField("stroke", String.valueOf(src.getStroke()));
        jgen.writeStringField("strokeWidth", String.valueOf(src.getStrokeWidth()));
    }
}
