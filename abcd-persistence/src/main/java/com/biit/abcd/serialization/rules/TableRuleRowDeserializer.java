package com.biit.abcd.serialization.rules;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;

public class TableRuleRowDeserializer extends StorableObjectDeserializer<TableRuleRow> {

    @Override
    public void deserialize(TableRuleRow element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setConditions(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("conditions").asText(), ExpressionChain.class));
        element.setActions(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("actions").asText(), ExpressionChain.class));
    }
}