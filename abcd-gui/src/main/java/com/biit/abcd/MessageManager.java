package com.biit.abcd;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class MessageManager {
	private final static Integer MESSAGE_DURATION_MILISECONDS = 4000;

	public static void showWarning(String caption, String description) {
		showMessage(caption, description, Notification.Type.WARNING_MESSAGE);
	}

	public static void showWarning(LanguageCodes caption, LanguageCodes description) {
		showWarning(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	public static void showWarning(LanguageCodes caption) {
		showWarning(ServerTranslate.translate(caption), "");
	}

	private static void showError(String caption, String description) {
		showMessage(caption, description, Notification.Type.ERROR_MESSAGE, Position.TOP_CENTER);
	}

	public static void showError(LanguageCodes caption, LanguageCodes description) {
		showError(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	public static void showError(LanguageCodes caption, String description) {
		showError(ServerTranslate.translate(caption), description);
	}

	public static void showError(String caption) {
		showMessage(caption, "", Notification.Type.ERROR_MESSAGE, Position.TOP_CENTER);
	}

	public static void showError(LanguageCodes caption) {
		showError(ServerTranslate.translate(caption));
	}

	private static void showInfo(String caption, String description) {
		showMessage(caption, description, Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void showInfo(LanguageCodes caption, LanguageCodes description) {
		showInfo(ServerTranslate.translate(caption), ServerTranslate.translate(description));
	}

	private static void showInfo(String caption) {
		showMessage(caption, "", Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void showInfo(LanguageCodes caption) {
		showInfo(ServerTranslate.translate(caption));
	}

	private static void showMessage(String caption, String description, Notification.Type type) {
		showMessage(caption, description, type, Position.TOP_CENTER);
	}

	private static void showMessage(String caption, String description, Notification.Type type, Position position) {
		// Log it.
		try {
			String user;
			if (UserSessionHandler.getUser() != null) {
				user = UserSessionHandler.getUser().getEmailAddress();
			} else {
				user = "none";
			}
			AbcdLogger.info(MessageManager.class.getName(), "Message '" + caption + " " + description + "' (" + type
					+ ") displayed to user '" + user + "'.");
		} catch (Exception e) {
			AbcdLogger.errorMessage(MessageManager.class.getName(), e);
		}

		if (UI.getCurrent() != null) {
			Notification notif = new Notification(caption, description, type);

			// Set the position.
			notif.setPosition(position);

			// Let it stay there until the user clicks it if is error message
			if (type.equals(Notification.Type.ERROR_MESSAGE)) {
				notif.setDelayMsec(-1);
			} else {
				notif.setDelayMsec(MESSAGE_DURATION_MILISECONDS);
			}

			// Show it in the main window.
			notif.show(ApplicationFrame.getCurrent().getPage());
		}
	}
}
