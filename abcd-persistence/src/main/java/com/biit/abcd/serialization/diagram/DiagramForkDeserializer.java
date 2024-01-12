package com.biit.abcd.serialization.diagram;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiagramForkDeserializer extends DiagramElementDeserializer<DiagramFork> {

    @Override
    public void deserialize(DiagramFork element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        // Diagram objects deserialization
        final JsonNode diagramObjects = jsonObject.get("references");
        if (diagramObjects != null) {
            //Handle children one by one.
            if (diagramObjects.isArray()) {
                final List<ExpressionValueTreeObjectReference> references = new ArrayList<>();
                for (JsonNode childNode : diagramObjects) {
                    try {
                        final Class<? extends ExpressionValueTreeObjectReference> classType = (Class<? extends ExpressionValueTreeObjectReference>) Class.forName(childNode.get("class").asText());
                        references.add(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid diagram object:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
                element.setReferences(references);
            }
        }
    }
}