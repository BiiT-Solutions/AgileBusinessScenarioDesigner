package com.biit.abcd.serialization.expressions;

import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class ExpressionPluginMethodSerializer extends ExpressionSerializer<ExpressionPluginMethod> {

    @Override
    public void serialize(ExpressionPluginMethod src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("pluginInterface", src.getPluginInterface().getCanonicalName());
        jgen.writeStringField("pluginName", src.getPluginName());
        jgen.writeStringField("pluginMethodName", src.getPluginMethodName());
    }
}
