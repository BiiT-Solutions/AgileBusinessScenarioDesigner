package com.biit.abcd.core.drools.prattparser;

public enum ExpressionTokenType {

	// Available functions
	NOT, MAX, MIN, ABS, SQRT, IN, BETWEEN, ROUND, AVG, PMT,
	// Available operators
	NULL, AND, OR, EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_EQUALS, LESS_THAN, ASSIGNATION, PLUS, MINUS, MULTIPLICATION, DIVISION, MODULE, POW,
	// Available symbols
	RIGHT_BRACKET, LEFT_BRACKET, COMMA,
	// Special types for the parser, end of file and generic name
	EOF, NAME;

	/**
	 * If the TokenType represents a punctuator (i.e. a token that can split an
	 * identifier like '+', this will get its text.
	 */
	public String punctuator() {
		switch (this) {
		case LEFT_BRACKET:
			return "(";
		case RIGHT_BRACKET:
			return ")";
		case COMMA:
			return ",";
		case ASSIGNATION:
			return "=";
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		case MULTIPLICATION:
			return "*";
		case DIVISION:
			return "/";
		case POW:
			return "^";
		case NOT:
			return "NOT(";
		case AND:
			return "AND";
		case OR:
			return "OR";
		case EQUALS:
			return "==";
		case NOT_EQUALS:
			return "!=";
		case BETWEEN:
			return "BETWEEN(";
		case IN:
			return "IN(";
		case MIN:
			return "MIN(";
		case PMT:
			return "PMT(";
		default:
			return null;
		}
	}
}
