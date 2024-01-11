package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Category;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CategoryDeserializer extends TreeObjectDeserializer<Category> {

    @Override
    public void deserialize(Category element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }

}
