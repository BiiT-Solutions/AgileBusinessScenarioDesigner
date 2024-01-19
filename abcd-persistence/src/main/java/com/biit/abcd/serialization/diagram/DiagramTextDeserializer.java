package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTextDeserializer extends CustomDeserializer<DiagramText> {

    @Override
    public void deserialize(DiagramText element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setText(parseString("text", jsonObject));
        element.setFill(parseString("fill", jsonObject));
        //As comes from jscript
        element.setFontSize(parseString("font_size", jsonObject));
        element.setStroke(parseString("stroke", jsonObject));
        //As comes from jscript
        element.setStrokeWidth(parseString("stroke_width", jsonObject));
    }

    @Override
    public DiagramText getObject() {
        return new DiagramText();
    }
}