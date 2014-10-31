package com.biit.abcd.language;

import java.util.Locale;

import com.vaadin.data.util.converter.AbstractStringToNumberConverter;

public class UserLocaleStringToStringConverter extends AbstractStringToNumberConverter<String> {
	private static final long serialVersionUID = 7146306342542649529L;

	@Override
	public String convertToModel(String value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value;
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

}
