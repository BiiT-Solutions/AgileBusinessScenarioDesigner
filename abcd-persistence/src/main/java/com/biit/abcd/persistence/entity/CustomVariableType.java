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

public enum CustomVariableType {
    STRING("class.String", " "),

    NUMBER("class.Number", "0"),

    DATE("class.Date", null);

    private final String translation;

    private final String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    private CustomVariableType(String translation, String defaultValue) {
        this.translation = translation;
        this.defaultValue = defaultValue;
    }

    public String getTranslationCode() {
        return translation;
    }

    public static CustomVariableType get(String type) {
        for (CustomVariableType customVariableType : CustomVariableType.values()) {
            if (customVariableType.name().equalsIgnoreCase(type)) {
                return customVariableType;
            }
        }
        return null;
    }
}
