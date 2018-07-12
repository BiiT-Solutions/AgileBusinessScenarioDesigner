package com.biit.abcd.core.drools.prattparser.exceptions;

public class PrattParserException extends Exception {
	private static final long serialVersionUID = -3048123597064347435L;
	private String description = "";

	public PrattParserException(String message) {
		super(message);
	}
	
	public PrattParserException(String message, Exception e) {
		super(message, e);
		this.description = message;
	}
	
	public String getDescription() {
		return description;
	}
}
