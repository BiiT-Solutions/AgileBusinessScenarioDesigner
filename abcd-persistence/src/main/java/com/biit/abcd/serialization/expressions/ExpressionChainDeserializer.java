package com.biit.abcd.serialization.expressions;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionChainDeserializer extends ExpressionDeserializer<ExpressionChain> {

    @Override
    public void deserialize(ExpressionChain element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));

        // Diagram objects deserialization
        final JsonNode diagramObjects = jsonObject.get("expressions");
        if (diagramObjects != null) {
            //Handle children one by one.
            if (diagramObjects.isArray()) {
                for (JsonNode childNode : diagramObjects) {
                    try {
                        final Class<? extends Expression> classType = (Class<? extends Expression>) Class.forName(childNode.get("class").asText());
                        element.addExpression(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid expression:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}