package com.biit.abcd.serialization;

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class QuestionSerializer extends TreeObjectSerializer<Question> {

    @Override
    public void serialize(Question src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        if (src.getAnswerType() != null) {
            jgen.writeStringField("answerType", src.getAnswerType().name());
        }
        if (src.getAnswerFormat() != null) {
            jgen.writeStringField("answerFormat", src.getAnswerFormat().name());
        }
    }

}
