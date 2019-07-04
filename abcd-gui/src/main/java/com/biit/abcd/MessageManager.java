package com.biit.abcd;

import java.util.Locale;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class MessageManager {
	private final static Integer MESSAGE_DURATION_MILISECONDS = 4000;

	private static void showMessage(LanguageCodes caption, LanguageCodes description, Notification.Type type,
			Position position, Object... args) {
		// Log the message.
		try {
			String user;
			if (UserSessionHandler.getUser() != null) {
				user = UserSessionHandler.getUser().getEmailAddress();
			} else {
				user = "none";
			}

			String logTranslatedCaption = ServerTranslate.translate(caption, Locale.ENGLISH);
			String logTranslatedDescription = new String();
			if (description != null) {
				logTranslatedDescription = ServerTranslate.translate(description, Locale.ENGLISH, args);
			}

			AbcdLogger.info(MessageManager.class.getName(), "Message '" + logTranslatedCaption + " "
					+ logTranslatedDescription + "' (" + type + ") displayed to user '" + user + "'.");
		} catch (Exception e) {
			AbcdLogger.errorMessage(MessageManager.class.getName(), e);
		}

		if (UI.getCurrent() != null) {
			String notificationTranslatedCaption = ServerTranslate.translate(caption);
			String notificationTranslatedDescription = ServerTranslate.translate(description, args);
			Notification notif = new Notification(notificationTranslatedCaption, notificationTranslatedDescription,
					type);

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

	private static void showMessage(LanguageCodes caption, LanguageCodes description, Notification.Type type,
			Object... args) {
		showMessage(caption, description, type, Position.TOP_CENTER, args);
	}

	public static void showWarning(LanguageCodes caption, LanguageCodes description, Object... args) {
		showMessage(caption, description, Notification.Type.WARNING_MESSAGE, args);
	}

	public static void showWarning(LanguageCodes caption, Object... args) {
		showWarning(caption, null, args);
	}

	public static void showError(LanguageCodes caption, LanguageCodes description, Object... args) {
		showMessage(caption, description, Notification.Type.ERROR_MESSAGE, args);
	}

	public static void showError(LanguageCodes caption) {
		showError(caption, null);
	}

	private static void showInfo(LanguageCodes caption, LanguageCodes description) {
		showMessage(caption, description, Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void showInfo(LanguageCodes caption) {
		showInfo(caption, null);
	}

	/**
	 * Last resort to show an error in the UI
	 * 
	 * @param error the error message.
	 */
	public static void showError(String error) {
		try {
			String user;
			if (UserSessionHandler.getUser() != null) {
				user = UserSessionHandler.getUser().getEmailAddress();
			} else {
				user = "none";
			}

			AbcdLogger.severe(MessageManager.class.getName(), "Message '" + error + "' displayed to user '" + user
					+ "'.");
		} catch (Exception e) {
			AbcdLogger.errorMessage(MessageManager.class.getName(), e);
		}
		if (UI.getCurrent() != null) {
			Notification notif = new Notification(error, "", Type.ERROR_MESSAGE);

			// Set the position.
			notif.setPosition(Position.TOP_CENTER);
			notif.setDelayMsec(-1);

			// Show it in the main window.
			notif.show(ApplicationFrame.getCurrent().getPage());
		}
	}

}
