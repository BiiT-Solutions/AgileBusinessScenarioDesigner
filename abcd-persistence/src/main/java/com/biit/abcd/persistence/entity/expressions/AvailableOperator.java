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

public enum AvailableOperator {

	NULL("NULL", "CLEAR"),

	AND("&&", "AND"),

	OR("||", "OR"),

	EQUALS("==", "=="),

	NOT_EQUALS("!=", "!="),

	GREATER_THAN(">", ">"),

	GREATER_EQUALS(">=", ">="),

	LESS_THAN("<", "<"),

	LESS_EQUALS("<=", "<="),

	ASSIGNATION("=", "="),

	PLUS("+", "+"),

	MINUS("-", "-"),

	MULTIPLICATION("*", "*"),

	DIVISION("/", "/"),

	MODULE("%", "%"), 
	
	POW("^", "^");

	private String value;
	private String caption;

	private AvailableOperator(String value, String caption) {
		this.value = value;
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public String getCaption() {
		return caption;
	}

	@Override
	public String toString() {
		return caption;
	}

	public static AvailableOperator getOperator(String value) {
		for (AvailableOperator operator : AvailableOperator.values()) {
			if (operator.caption.equalsIgnoreCase(value)) {
				return operator;
			}
		}
		return null;
	}

	public static AvailableOperator get(String value) {
		for (AvailableOperator operator : AvailableOperator.values()) {
			if (operator.name().equalsIgnoreCase(value)) {
				return operator;
			}
		}
		return null;
	}
}
