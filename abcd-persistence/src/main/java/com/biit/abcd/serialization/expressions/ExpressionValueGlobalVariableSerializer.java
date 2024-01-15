package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionValueGlobalVariableSerializer extends ExpressionValueSerializer<ExpressionValueGlobalVariable> {

    @Override
    public void serialize(ExpressionValueGlobalVariable src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("globalVariable", src.getValue());
    }
}
