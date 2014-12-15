package com.biit.abcd.authentication.exceptions;

public class NotEnoughRightsToChangeStatusException extends Exception {
	private static final long serialVersionUID = 3635518427063354683L;

	public NotEnoughRightsToChangeStatusException(String message) {
		super(message);
	}

}
