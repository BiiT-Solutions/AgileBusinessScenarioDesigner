package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Category;
import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;


public class CategorySerializer extends TreeObjectSerializer<Category> {

    @Override
    public void serialize(Category src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }

}
