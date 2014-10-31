package com.biit.abcd.language;

import java.text.DateFormat;
import java.util.Locale;

import com.biit.abcd.authentication.UserSessionHandler;
import com.vaadin.data.util.converter.StringToDateConverter;

public class UserLocaleStringToDateConverter extends StringToDateConverter {
	private static final long serialVersionUID = 9168130031547476860L;

	/**
	 * Locale is overridden by user option selected in Liferay.
	 */
	@Override
	protected DateFormat getFormat(Locale locale) {
		locale = UserSessionHandler.getUser().getLocale();
		return super.getFormat(locale);
	}

}
