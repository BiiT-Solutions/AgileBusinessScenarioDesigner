package com.biit.abcd.persistence.entity.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class TreeObjectDeserializer<T extends TreeObject> extends StorableObjectDeserializer<T> {

	Class<T> specificClass;

	public TreeObjectDeserializer(Class<T> specificClass) {
		this.specificClass = specificClass;
	}

	public void deserialize(JsonElement json, JsonDeserializationContext context, T element) {
		JsonObject jobject = (JsonObject) json;

		try {
			if (!(element instanceof BaseForm)) {
				element.setName(parseString("name", jobject, context));
			}
			element.setLabel(parseString("label", jobject, context));
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			throw new JsonParseException(e);
		}

		// Children deserialization
		Type listType = new TypeToken<ArrayList<TreeObject>>() {
		}.getType();
		JsonElement childrenJson = jobject.get("children");
		if (childrenJson != null) {
			List<TreeObject> children = context.deserialize(childrenJson, listType);
			try {
				element.addChildren(children);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				throw new JsonParseException(e);
			}
		}

		super.deserialize(json, context, element);
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		T instance;
		try {
			instance = specificClass.newInstance();
			deserialize(json, context, instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JsonParseException(e);
		}
	}

}
