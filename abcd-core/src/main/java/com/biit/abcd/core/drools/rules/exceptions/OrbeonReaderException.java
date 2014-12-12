package com.biit.abcd.core.drools.rules.exceptions;

public class OrbeonReaderException  extends Exception {
	private static final long serialVersionUID = 3380907755128318651L;
	private Exception generatedException;

	public OrbeonReaderException(String message, Exception generatedException) {
		super(message);
		this.generatedException = generatedException;
	}

	public Exception getGeneratedException() {
		return generatedException;
	}
}
