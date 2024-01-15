package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramText;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramTextDeserializer extends StorableObjectDeserializer<DiagramText> {

    @Override
    public void deserialize(DiagramText element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setText(parseString("text", jsonObject));
        element.setFill(parseString("fill", jsonObject));
        element.setFontSize(parseString("fontSize", jsonObject));
        element.setStroke(parseString("stroke", jsonObject));
        element.setStrokeWidth(parseString("strokeWidth", jsonObject));
    }
}