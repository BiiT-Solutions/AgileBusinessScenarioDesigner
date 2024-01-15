package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;


public class ExpressionSymbolDeserializer extends ExpressionDeserializer<ExpressionSymbol> {

    @Override
    public void deserialize(ExpressionSymbol element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("value") != null) {
            element.setValue(AvailableSymbol.get(jsonObject.get("value").textValue()));
        }
    }
}