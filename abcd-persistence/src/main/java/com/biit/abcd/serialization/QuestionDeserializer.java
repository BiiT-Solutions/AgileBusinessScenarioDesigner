package com.biit.abcd.serialization;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class QuestionDeserializer extends TreeObjectDeserializer<Question> {


    public void deserialize(Question element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("answerType") != null) {
            element.setAnswerType(AnswerType.get(jsonObject.get("answerType").textValue()));
        }
        try {
            if (jsonObject.get("answerFormat") != null) {
                final String answerFormat = jsonObject.get("answerFormat").textValue();
                if (answerFormat != null) {
                    element.setAnswerFormat(AnswerFormat.get(answerFormat));
                }
            }
        } catch (InvalidAnswerFormatException e) {
            throw new RuntimeException(e);
        }
    }

}
