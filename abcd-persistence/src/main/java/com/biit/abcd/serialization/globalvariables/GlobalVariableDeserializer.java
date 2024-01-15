package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalVariableDeserializer extends StorableObjectDeserializer<GlobalVariable> {

    @Override
    public void deserialize(GlobalVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            element.setName(parseString("name", jsonObject));
        } catch (FieldTooLongException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
        if (jsonObject.get("answerFormat") != null) {
            element.setFormat(AnswerFormat.get(jsonObject.get("answerFormat").textValue()));
        }

        // Diagram objects deserialization
        final JsonNode variableData = jsonObject.get("variableData");
        if (variableData != null) {
            //Handle children one by one.
            if (variableData.isArray()) {
                final List<VariableData> data = new ArrayList<>();
                for (JsonNode childNode : variableData) {
                    try {
                        final Class<? extends VariableData> classType = (Class<? extends VariableData>) Class.forName(childNode.get("class").asText());
                        data.add(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                    } catch (ClassNotFoundException | NullPointerException e) {
                        AbcdLogger.severe(this.getClass().getName(), "Invalid VariableData object:\n" + jsonObject.toPrettyString());
                        AbcdLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
                element.setVariableData(data);
            }
        }
    }
}