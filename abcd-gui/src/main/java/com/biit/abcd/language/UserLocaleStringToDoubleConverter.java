package com.biit.abcd.language;

import java.text.NumberFormat;
import java.util.Locale;

import com.biit.abcd.authentication.UserSessionHandler;
import com.vaadin.data.util.converter.StringToDoubleConverter;

public class UserLocaleStringToDoubleConverter extends StringToDoubleConverter {
	private static final long serialVersionUID = -4819509538529371129L;

	/**
	 * Locale is overridden by user option selected in Liferay.
	 */
	@Override
	protected NumberFormat getFormat(Locale locale) {
		locale = UserSessionHandler.getUser().getLocale();
		return super.getFormat(locale);
	}
}
