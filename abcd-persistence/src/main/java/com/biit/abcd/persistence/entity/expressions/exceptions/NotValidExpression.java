package com.biit.abcd.persistence.entity.expressions.exceptions;

public class NotValidExpression extends Exception {
	private static final long serialVersionUID = -2517834060474853502L;

	public NotValidExpression(String text) {
		super(text);
	}
}
