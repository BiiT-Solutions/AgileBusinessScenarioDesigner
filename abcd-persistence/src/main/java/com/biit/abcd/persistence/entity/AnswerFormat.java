package com.biit.abcd.persistence.entity;

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

import com.biit.drools.form.DroolsQuestionFormat;

/**
 * Used only for text inputs.
 */
public enum AnswerFormat {

    TEXT(DroolsQuestionFormat.TEXT),

    MULTI_TEXT(DroolsQuestionFormat.MULTI_TEXT),

    NUMBER(DroolsQuestionFormat.NUMBER),

    DATE(DroolsQuestionFormat.DATE),

    POSTAL_CODE(DroolsQuestionFormat.POSTAL_CODE);

    private DroolsQuestionFormat droolsFormat;

    private AnswerFormat(DroolsQuestionFormat droolsFormat) {
        this.droolsFormat = droolsFormat;
    }

    public DroolsQuestionFormat getDroolsFormat() {
        return droolsFormat;
    }

    /**
     * Used in drools to decide if a answerformat is treated as string.
     *
     * @return true is represents a string.
     */
    public boolean isStringData() {
        return this.equals(AnswerFormat.TEXT) || this.equals(AnswerFormat.MULTI_TEXT)
                || this.equals(AnswerFormat.POSTAL_CODE);
    }

    public static AnswerFormat get(String answerFormat) {
        for (AnswerFormat format : AnswerFormat.values()) {
            if (format.name().equalsIgnoreCase(answerFormat)) {
                return format;
            }
        }
        return null;
    }
}
