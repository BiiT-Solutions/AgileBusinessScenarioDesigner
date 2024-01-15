package com.biit.abcd.serialization.rules;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TableRuleSerializer extends StorableObjectSerializer<TableRule> {

    @Override
    public void serialize(TableRule src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("name", src.getName());
        jgen.writeObjectField("rules", src.getRules());

//        jgen.writeFieldName("rules");
//        jgen.writeStartArray("rules");
//        for (TableRuleRow variableData : src.getRules()) {
//            jgen.writeObject(variableData);
//        }
//        jgen.writeEndArray();
    }
}
