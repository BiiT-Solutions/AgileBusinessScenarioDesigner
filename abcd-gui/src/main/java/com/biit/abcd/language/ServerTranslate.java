package com.biit.abcd.language;

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

	private static String translationException(String code, Object[] args) {
		initialize();
		String translation = helper.getContext().getMessage(code, args, getLocale());
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

	public static String translate(LanguageCodes code) {
		if (code == null) {
			return null;
		}
		return translate(code.toString(), null);
	}

	public static String translate(String code) {
		if (code == null) {
			return null;
		}
		return translate(code, null);
	}

	public static String translate(LanguageCodes code, Object[] args) {
		return translate(code.toString(), args);
	}

	public static String translate(String code, Object[] args) {
		try {
			return translationException(code, args);
		} catch (RuntimeException e) {
			AbcdLogger.errorMessage(ServerTranslate.class.getName(), e);
			try {
				MessageManager.showError(ServerTranslate.translationException("error.fatal", null));
			} catch (RuntimeException e2) {
				MessageManager.showError("Fatal error in the translations.");
			}
			return "No translation.";
		}
	}

}
