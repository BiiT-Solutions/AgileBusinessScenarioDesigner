package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;


public class AnswerSerializer extends TreeObjectSerializer<Answer> {

    @Override
    public void serialize(Answer src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }

}
