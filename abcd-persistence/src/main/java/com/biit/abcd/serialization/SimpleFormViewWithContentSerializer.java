package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleFormViewWithContent;
import com.biit.form.jackson.serialization.BaseStorableObjectDeserializer;
import com.biit.form.jackson.serialization.BaseStorableObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class SimpleFormViewWithContentSerializer extends BaseStorableObjectSerializer<SimpleFormViewWithContent> {

    @Override
    public void serialize(SimpleFormViewWithContent src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getName() != null) {
            jgen.writeStringField("name", src.getName());
        }
        if (src.getLabel() != null) {
            jgen.writeStringField("label", src.getLabel());
        }
        if (src.getVersion() != null) {
            jgen.writeNumberField("version", src.getVersion());
        }
        if (src.getOrganizationId() != null) {
            jgen.writeNumberField("organizationId", src.getOrganizationId());
        }
        if (src.getAvailableFrom() != null) {
            jgen.writeStringField("availableFrom", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableFrom().toLocalDateTime()));
        }
        if (src.getAvailableTo() != null) {
            jgen.writeStringField("availableTo", BaseStorableObjectDeserializer.TIMESTAMP_FORMATTER.format(src.getAvailableTo().toLocalDateTime()));
        }
        if (src.getStatus() != null) {
            jgen.writeStringField("status", src.getStatus().name());
        }
        if (src.getJson() != null) {
            jgen.writeStringField("json", src.getJson());
        }
    }
}
