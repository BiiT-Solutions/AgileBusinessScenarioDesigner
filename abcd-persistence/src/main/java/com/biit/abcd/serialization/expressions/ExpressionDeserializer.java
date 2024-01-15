package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ExpressionDeserializer<T extends Expression> extends StorableObjectDeserializer<T> {

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setSortSeq(parseInteger("sortSeq", jsonObject));
    }
}