package com.biit.abcd.core.drools.prattparser;

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
