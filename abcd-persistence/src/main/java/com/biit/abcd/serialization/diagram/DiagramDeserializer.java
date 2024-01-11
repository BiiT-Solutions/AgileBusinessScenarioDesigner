package com.biit.abcd.serialization.diagram;

import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.form.log.FormStructureLogger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class DiagramDeserializer extends StorableObjectDeserializer<Diagram> {

    @Override
    public void deserialize(Diagram element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setName(parseString("name", jsonObject));

//        // Children deserialization
//        final JsonNode diagramObjects = jsonObject.get("diagramObjects");
//        if (diagramObjects != null) {
//            //Handle children one by one.
//            if (diagramObjects.isArray()) {
//                for (JsonNode childNode : diagramObjects) {
//                    try {
//                        final Class<T> classType = (Class<T>) Class.forName(childNode.get("class").asText());
//                        element.addChild(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
//                    } catch (ClassNotFoundException | NotValidChildException | ElementIsReadOnly
//                             | NullPointerException e) {
//                        FormStructureLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
//                        FormStructureLogger.errorMessage(this.getClass().getName(), e);
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//
//            final List<T> children = ObjectMapperFactory.getObjectMapper().readValue(diagramObjects.toPrettyString(),
//                    new TypeReference<List<T>>() {
//                    });
//            try {
//                element.addChildren(children);
//            } catch (NotValidChildException | ElementIsReadOnly e) {
//                FormStructureLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
//                throw new RuntimeException(e);
//            }
    }
}