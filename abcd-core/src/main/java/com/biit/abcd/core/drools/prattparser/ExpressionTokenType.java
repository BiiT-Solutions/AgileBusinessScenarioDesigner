package com.biit.abcd.core.drools.prattparser;

/**
 * Enum class that defines the token identifiers for our parser.
 */
public enum ExpressionTokenType {

	// Available functions
	NOT, MAX, MIN, ABS, SQRT, IN, BETWEEN, ROUND, AVG, PMT, SUM, IF, LOG,
	// Available operators
	NULL, AND, OR, EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_EQUALS, LESS_THAN, LESS_EQUALS, ASSIGNATION, PLUS, MINUS, MULTIPLICATION, DIVISION, MODULE, POW,
	// Available symbols
	RIGHT_BRACKET, LEFT_BRACKET, COMMA,
	// Special types for the parser, end of file and generic name
	EOF, NAME,
	// Special type for the plugin methods
	IPLUGIN;

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
		case MODULE:
			return "%";
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
		case MAX:
			return "MAX(";
		case AVG:
			return "AVG(";
		case SUM:
			return "SUM(";
		case PMT:
			return "PMT(";
		case LOG:
			return "LOG(";
		case IF:
			return "IF(";
		case GREATER_THAN:
			return ">";
		case GREATER_EQUALS:
			return ">=";
		case LESS_THAN:
			return "<";
		case LESS_EQUALS:
			return "<=";
		case IPLUGIN:
			return "IPlugin";
		default:
			return null;
		}
	}
}
