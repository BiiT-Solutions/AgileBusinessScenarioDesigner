package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CustomVariableDeserializer extends StorableObjectDeserializer<CustomVariable> {

    @Override
    public void deserialize(CustomVariable element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setName(parseString("name", jsonObject));
        element.setType(CustomVariableType.get(jsonObject.get("type").textValue()));
        element.setScope(CustomVariableScope.get(jsonObject.get("scope").textValue()));
        element.setDefaultValue(parseString("defaultValue", jsonObject));
    }
}
