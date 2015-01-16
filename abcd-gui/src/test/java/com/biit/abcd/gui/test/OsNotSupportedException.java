package com.biit.abcd.gui.test;

public class OsNotSupportedException extends Exception {
	private static final long serialVersionUID = 8898429508545676197L;
	private String message;
	
	public OsNotSupportedException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
