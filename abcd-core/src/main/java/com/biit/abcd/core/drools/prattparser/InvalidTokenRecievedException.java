package com.biit.abcd.core.drools.prattparser;

public class InvalidTokenRecievedException extends RuntimeException{
	private static final long serialVersionUID = 4111396107010142091L;
	private String message;
	
	public InvalidTokenRecievedException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}

}
