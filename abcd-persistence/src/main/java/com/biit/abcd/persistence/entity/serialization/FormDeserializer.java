package com.biit.abcd.persistence.entity.serialization;

import com.biit.abcd.persistence.entity.Form;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

public class FormDeserializer extends BaseFormDeserializer<Form> {

	public FormDeserializer() {
		super(Form.class);
	}

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, Form element) {
		// Deserializes childs
		super.deserialize(json, context, element);
	}
}
