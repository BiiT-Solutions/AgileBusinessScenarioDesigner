package com.biit.abcd.core.drools.rules.validators;

/**
 * Exception created for the expression validator
 * 
 */
public class InvalidExpressionException extends Exception {
	private static final long serialVersionUID = -9031196357941801998L;
	
	private String message;
	
	public InvalidExpressionException(){}
	
	public InvalidExpressionException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
