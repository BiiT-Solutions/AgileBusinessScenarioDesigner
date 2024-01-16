package com.biit.abcd.serialization.expressions;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperator;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionOperatorDeserializer<T extends ExpressionOperator> extends ExpressionDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            if (jsonObject.get("value") != null) {
                element.setValue(AvailableOperator.get(jsonObject.get("value").toString()));
            }
        } catch (NotValidOperatorInExpression e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }
}