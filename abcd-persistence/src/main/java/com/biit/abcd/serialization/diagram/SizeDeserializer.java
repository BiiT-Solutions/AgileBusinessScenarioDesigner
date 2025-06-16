package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Size;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SizeDeserializer extends CustomDeserializer<Size> {

    @Override
    public void deserialize(Size element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setHeight(parseInteger("height", jsonObject));
        element.setWidth(parseInteger("width", jsonObject));
    }

    public Size getObject() {
        return new Size();
    }
}