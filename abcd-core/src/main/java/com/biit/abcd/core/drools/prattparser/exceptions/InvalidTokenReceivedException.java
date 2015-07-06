package com.biit.abcd.core.drools.prattparser.exceptions;

public class InvalidTokenReceivedException extends RuntimeException{
	
	private static final long serialVersionUID = 4111396107010142091L;
	private String message;
	
	public InvalidTokenReceivedException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}

}
