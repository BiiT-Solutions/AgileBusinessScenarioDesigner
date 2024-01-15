package com.biit.abcd.serialization.rules;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class TableRuleDeserializer extends StorableObjectDeserializer<TableRule> {

    @Override
    public void deserialize(TableRule element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setName(parseString("name", jsonObject));
        element.setRules(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("rules").asText(), TableRuleRow[].class)));
    }
}