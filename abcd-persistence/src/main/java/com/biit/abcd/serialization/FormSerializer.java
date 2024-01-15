package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.jackson.serialization.BaseFormSerializer;
import com.biit.form.jackson.serialization.BaseStorableObjectDeserializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class FormSerializer extends BaseFormSerializer<Form> {

    @Override
    public void serialize(Form src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src.getAvailableFrom() != null) {
            jgen.writeStringField("availableFrom", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableFrom().toLocalDateTime()));
        }
        if (src.getAvailableTo() != null) {
            jgen.writeStringField("availableTo", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableTo().toLocalDateTime()));
        }
        if (src.getStatus() != null) {
            jgen.writeStringField("status", src.getStatus().name());
        }

        jgen.writeFieldName("diagrams");
        jgen.writeStartArray("diagrams");
        for (Diagram diagram : src.getDiagrams()) {
            jgen.writeObject(diagram);
        }
        jgen.writeEndArray();


        jgen.writeFieldName("tableRules");
        jgen.writeStartArray("tableRules");
        for (TableRule tableRule : src.getTableRules()) {
            jgen.writeObject(tableRule);
        }
        jgen.writeEndArray();

        jgen.writeFieldName("customVariables");
        jgen.writeStartArray("customVariables");
        for (CustomVariable customVariable : src.getCustomVariables()) {
            jgen.writeObject(customVariable);
        }
        jgen.writeEndArray();

        jgen.writeFieldName("expressionChains");
        jgen.writeStartArray("expressionChains");
        for (ExpressionChain expressionChain : src.getExpressionChains()) {
            jgen.writeObject(expressionChain);
        }
        jgen.writeEndArray();

        jgen.writeFieldName("rules");
        jgen.writeStartArray("rules");
        for (Rule rule : src.getRules()) {
            jgen.writeObject(rule);
        }
        jgen.writeEndArray();

    }
}
