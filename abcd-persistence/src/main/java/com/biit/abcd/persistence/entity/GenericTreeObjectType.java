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


public enum GenericTreeObjectType {

    CATEGORY(Category.class, "Categories", "Categories"),

    GROUP(Group.class, "Groups", "Groups"),

    QUESTION_CATEGORY(Question.class, "Questions", "Category.Questions"),

    QUESTION_GROUP(Question.class, "Questions", "Group.Questions");

    private Class<?> scope;
    private String tableName;
    private String expressionName;

    private GenericTreeObjectType(Class<?> scope, String tableName, String expressionName) {
        this.scope = scope;
        this.tableName = tableName;
        this.expressionName = expressionName;
    }

    public Class<?> getScope() {
        return scope;
    }

    public String getTableName() {
        return tableName;
    }

    public String getExpressionName() {
        return expressionName;
    }

    @Override
    public String toString() {
        return getExpressionName();
    }

    public static GenericTreeObjectType get(String genericTreeObjectType) {
        for (GenericTreeObjectType type : GenericTreeObjectType.values()) {
            if (type.name().equalsIgnoreCase(genericTreeObjectType)) {
                return type;
            }
        }
        return null;
    }
}
