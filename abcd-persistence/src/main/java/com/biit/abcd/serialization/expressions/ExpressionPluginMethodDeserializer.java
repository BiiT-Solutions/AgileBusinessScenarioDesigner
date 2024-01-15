package com.biit.abcd.serialization.expressions;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionPluginMethodDeserializer extends ExpressionDeserializer<ExpressionPluginMethod> {

    @Override
    public void deserialize(ExpressionPluginMethod element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        try {
            if (jsonObject.get("pluginInterface") != null) {
                element.setPluginInterface(Class.forName(jsonObject.get("pluginInterface").textValue()));
            }
        } catch (ClassNotFoundException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
        element.setPluginName(parseString("pluginName", jsonObject));
        element.setPluginMethodName(parseString("pluginMethodName", jsonObject));
    }
}