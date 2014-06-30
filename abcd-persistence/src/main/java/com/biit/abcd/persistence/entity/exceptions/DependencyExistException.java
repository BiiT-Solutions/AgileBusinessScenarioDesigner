package com.biit.abcd.persistence.entity.exceptions;

public class DependencyExistException extends Exception {
	private static final long serialVersionUID = 3408394952798630280L;

	public DependencyExistException(String message) {
		super(message);
	}
}
