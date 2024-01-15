package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class VariableDataNumberDeserializer extends VariableDataDeserializer<VariableDataNumber> {

    @Override
    public void deserialize(VariableDataNumber element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            element.setValue(parseDouble("value", jsonObject));
        } catch (NotValidTypeInVariableData e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }
}