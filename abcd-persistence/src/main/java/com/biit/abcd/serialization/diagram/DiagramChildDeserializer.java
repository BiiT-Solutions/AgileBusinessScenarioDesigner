package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class DiagramChildDeserializer extends DiagramElementDeserializer<DiagramChild> {

    @Override
    public void deserialize(DiagramChild element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("diagram") != null) {
            element.setDiagram(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("diagram").toString(), Diagram.class));
        }
    }
}