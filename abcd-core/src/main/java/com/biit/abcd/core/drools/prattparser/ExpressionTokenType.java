package com.biit.abcd.core.drools.prattparser;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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
 * Enum class that defines the token identifiers for our parser.
 */
public enum ExpressionTokenType {

	// Available functions
	NOT("NOT("),

	MAX("MAX("),

	MIN("MIN("),

	ABS("ABS("),

	SQRT("SQRT("),

	IN("IN("),

	BETWEEN("BETWEEN("),

	ROUND("ROUND("),

	AVG("AVG("),

	PMT("PMT("),

	SUM("SUM("),

	IF("IF("),

	LOG("LOG("),

	CONCAT("CONCAT("),
	
	CONCAT_SEPARATOR("CONCAT_SEP("),
	
	ELEMENT_XPATH("ELEMENT_XPATH("),
	
	ELEMENT_PATH("ELEMENT_PATH("),
	
	ELEMENT_NAME("ELEMENT_NAME("),
	
	ELEMENT_ID("ELEMENT_ID("),

	// Available operators
	NULL(null),

	AND("AND"),

	OR("OR"),

	EQUALS("=="),

	NOT_EQUALS("!="),

	GREATER_THAN(">"),

	GREATER_EQUALS(">="),

	LESS_THAN("<"),

	LESS_EQUALS("<="),

	ASSIGNATION("="),

	PLUS("+"),

	MINUS("-"),

	MULTIPLICATION("*"),

	DIVISION("/"),

	MODULE("%"),

	POW("^"),

	// Available symbols
	RIGHT_BRACKET(")"),

	LEFT_BRACKET("("),

	COMMA(","),

	// Special types for the parser, end of file and generic name
	EOF(null),

	NAME(null),

	// Special type for the plugin methods
	IPLUGIN("IPlugin");

	private String punctuator;

	private ExpressionTokenType(String punctuator) {
		this.punctuator = punctuator;
	}

	/**
	 * If the TokenType represents a punctuator (i.e. a token that can split an
	 * identifier like '+', this will get its text.
	 */
	public String getPunctuator() {
		return punctuator;
	}
}
