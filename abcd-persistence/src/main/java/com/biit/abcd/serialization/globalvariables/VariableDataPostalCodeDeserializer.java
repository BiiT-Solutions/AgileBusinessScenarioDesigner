package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalcode;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class VariableDataPostalCodeDeserializer extends VariableDataDeserializer<VariableDataPostalcode> {

    @Override
    public void deserialize(VariableDataPostalcode element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            element.setValue(parseString("value", jsonObject));
        } catch (NotValidTypeInVariableData e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }
}