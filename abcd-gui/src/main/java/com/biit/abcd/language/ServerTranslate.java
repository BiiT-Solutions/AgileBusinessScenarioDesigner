package com.biit.abcd.language;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Locale;

import org.springframework.util.StringUtils;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;

public class ServerTranslate {

	private static SpringContextHelper helper = null;

	private static void initialize() {
		if (helper == null) {
			helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		}
	}

	private static String translationException(String code, Locale locale, Object... args) {
		initialize();
		String translation;
		if (locale == null) {
			translation = helper.getContext().getMessage(code, args, getLocale());
		} else {
			translation = helper.getContext().getMessage(code, args, locale);
		}
		return translation;
	}

	private static Locale getLocale() {
		if (UserSessionHandler.getUser() != null) {
			return StringUtils.parseLocaleString(UserSessionHandler.getUser().getLanguageId());
		} else {
			if (Page.getCurrent() != null) {
				return Page.getCurrent().getWebBrowser().getLocale();
			} else {
				return null;
			}
		}
	}
	
	public static String translate(String code, Object... args){
		return translate(code, null, args);
	}
	
	
	public static String translate(LanguageCodes code, Object... args){
		return translate(code, null, args);
	}

	public static String translate(LanguageCodes code, Locale locale, Object... args) {
		if (code == null) {
			return null;
		}
		return translate(code.toString(), locale, args);
	}

	public static String translate(String code, Locale locale, Object... args) {
		try {
			return translationException(code, locale, args);
		} catch (RuntimeException e) {
			AbcdLogger.errorMessage(ServerTranslate.class.getName(), e);
			try {
				MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			} catch (RuntimeException e2) {
				MessageManager.showError("Fatal error in the translations.");
			}
			return "No translation.";
		}
	}

}
