package com.biit.abcd.serialization.globalvariables;

import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.form.jackson.serialization.CustomDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.sql.Timestamp;

public class VariableDataDateDeserializer extends VariableDataDeserializer<VariableDataDate> {

    @Override
    public void deserialize(VariableDataDate element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setValidFrom(parseTimestamp("value", jsonObject));
    }
}