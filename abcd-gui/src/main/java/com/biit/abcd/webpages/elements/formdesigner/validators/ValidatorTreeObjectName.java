package com.biit.abcd.webpages.elements.formdesigner.validators;

import java.util.regex.Pattern;

import com.vaadin.data.Validator;

public class ValidatorTreeObjectName implements Validator {
	private static final long serialVersionUID = 8591363723969902840L;

	private Pattern pattern;

	public ValidatorTreeObjectName(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!pattern.matcher((String) value).matches()) {
			throw new InvalidValueException("'" + value + "' doesn't comply XML tag restrictions");
		}
	}

}
