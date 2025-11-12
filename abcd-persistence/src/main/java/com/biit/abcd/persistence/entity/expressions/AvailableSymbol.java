package com.biit.abcd.persistence.entity.expressions;

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

/**
 * Non operators symbols used for defining an expression.
 */
public enum AvailableSymbol {

    RIGHT_BRACKET(")", false),

    LEFT_BRACKET("(", true),

    // Comma is used for separating parameters of a function.
    COMMA(",", false),

    PILCROW("\u00B6", false);

    private String value;
    // Some symbols are composed as a pair. This flag indicates if the left part or the right one.
    private Boolean leftSymbol;

    private AvailableSymbol(String value, Boolean leftSymbol) {
        this.value = value;
        this.leftSymbol = leftSymbol;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public Boolean getLeftSymbol() {
        return leftSymbol;
    }

    public static AvailableSymbol get(String availableSymbol) {
        for (AvailableSymbol symbol : AvailableSymbol.values()) {
            if (symbol.name().equalsIgnoreCase(availableSymbol)) {
                return symbol;
            }
        }
        return null;
    }
}
