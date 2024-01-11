package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
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

    }
}
