package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.form.jackson.serialization.BaseFormDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class FormDeserializer extends BaseFormDeserializer<Form> {

    @Override
    public void deserialize(Form element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setAvailableFrom(parseTimestamp("availableFrom", jsonObject));
        element.setAvailableTo(parseTimestamp("availableTo", jsonObject));
        element.setStatus(FormWorkStatus.getFromString(jsonObject.get("status").textValue()));
    }
}
