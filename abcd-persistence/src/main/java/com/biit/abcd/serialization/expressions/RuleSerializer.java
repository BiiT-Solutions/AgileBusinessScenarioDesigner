package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class RuleSerializer<T extends Rule> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("name", src.getName());

        jgen.writeObjectField("conditions", src.getConditions());
        jgen.writeObjectField("actions", src.getActions());
    }
}
