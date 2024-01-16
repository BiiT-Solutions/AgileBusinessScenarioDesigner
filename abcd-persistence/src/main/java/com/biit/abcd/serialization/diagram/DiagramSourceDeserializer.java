package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramSourceDeserializer extends DiagramElementDeserializer<DiagramSource> {

    @Override
    public void deserialize(DiagramSource element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }

    @Override
    public DiagramSource deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final DiagramSource element = new DiagramSource();
        deserialize(element, jsonObject, deserializationContext);
        return element;
    }
}