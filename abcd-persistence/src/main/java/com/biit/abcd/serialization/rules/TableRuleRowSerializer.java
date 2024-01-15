package com.biit.abcd.serialization.rules;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TableRuleRowSerializer extends StorableObjectSerializer<TableRuleRow> {

    @Override
    public void serialize(TableRuleRow src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeObjectField("conditions", src.getConditions());
        jgen.writeObjectField("actions", src.getActions());
    }
}
