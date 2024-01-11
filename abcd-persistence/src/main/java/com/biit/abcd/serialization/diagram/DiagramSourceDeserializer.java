package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramSourceDeserializer extends CustomDeserializer<DiagramSource> {

    @Override
    public void deserialize(DiagramSource element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}