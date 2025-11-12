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

public enum AnswerType {
    RADIO(null, true, true, null, true, true),

    MULTI_CHECKBOX(null, true, true, null, true, true),

    // Uses answer format.
    INPUT(AnswerFormat.TEXT, false, false, null, true, false);

    public boolean isInputField() {
        return this.equals(AnswerType.INPUT);
    }

    private AnswerFormat defaultAnswerFormat;
    private boolean childrenAllowed;
    private boolean subChildrenAllowed;
    private Boolean defaultHorizontal;
    private Boolean defaultMandatory;
    private boolean nestedAnswerAllowed;

    AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, boolean subchildrenAllowed,
               Boolean defaultHorizontal, Boolean defaultMandatory, boolean nestedAnswerAllowed) {
        this.defaultAnswerFormat = defaultAnswerType;
        this.childrenAllowed = childrenAllowed;
        this.defaultHorizontal = defaultHorizontal;
        this.defaultMandatory = defaultMandatory;
        this.nestedAnswerAllowed = nestedAnswerAllowed;
        this.subChildrenAllowed = subchildrenAllowed;
    }

    public boolean isAnswerFormatEnabled() {
        return defaultAnswerFormat != null;
    }

    public boolean isHorizontalEnabled() {
        return defaultHorizontal != null;
    }

    public Boolean getDefaultHorizontal() {
        if (defaultHorizontal != null) {
            return defaultHorizontal;
        }
        return false;
    }

    public AnswerFormat getDefaultAnswerFormat() {
        return defaultAnswerFormat;
    }

    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    public boolean isMandatoryEnabled() {
        return defaultMandatory != null;
    }

    public boolean getDefaultMandatory() {
        if (defaultMandatory != null) {
            return defaultMandatory;
        }
        return false;
    }

    public boolean isNestedAnswerAllowed() {
        return nestedAnswerAllowed;
    }

    public boolean isSubChildrenAllowed() {
        return subChildrenAllowed;
    }

    public static AnswerType get(String answerType) {
        for (AnswerType type : AnswerType.values()) {
            if (type.name().equalsIgnoreCase(answerType)) {
                return type;
            }
        }
        return null;
    }
}
