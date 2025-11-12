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

import com.biit.form.entity.TreeObject;

public enum CustomVariableScope {

    FORM(Form.class, "class.Form", "Form"),

    CATEGORY(Category.class, "class.Category", "Category"),

    GROUP(Group.class, "class.Group", "Group"),

    QUESTION(Question.class, "class.Question", "Question");

    private Class<? extends TreeObject> scope;
    private String translation;
    private String name;

    private CustomVariableScope(Class<? extends TreeObject> scope, String translation, String name) {
        this.scope = scope;
        this.translation = translation;
        this.name = name;
    }

    public Class<? extends TreeObject> getScope() {
        return scope;
    }

    public String getTranslationCode() {
        return translation;
    }

    public String getName() {
        return name;
    }

    public static CustomVariableScope get(String scope) {
        for (CustomVariableScope customVariableScope : CustomVariableScope.values()) {
            if (customVariableScope.name().equalsIgnoreCase(scope)) {
                return customVariableScope;
            }
        }
        return null;
    }

}
