package com.biit.abcd;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.liferay.portal.model.User;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}

	// User --> Id Form
	private static HashMap<User, Long> formsInUse = new HashMap<User, Long>();

	public static synchronized boolean isUserUsingForm(User user, Form form) {
		return formsInUse.get(user) != null && formsInUse.get(user).equals(form.getId());
	}

	public static synchronized User getUserUsingForm(Form form) {
		for (User user : formsInUse.keySet()) {
			if (formsInUse.get(user).equals(form.getId())) {
				return user;
			}
		}
		return null;
	}

	public static synchronized User getUserUsingForm(Long formId) {
		for (User user : formsInUse.keySet()) {
			if (formsInUse.get(user) != null && formsInUse.get(user).equals(formId)) {
				return user;
			}
		}
		return null;
	}

	public static synchronized void lockForm(Form form, User user) {
		if (form == null || user == null) {
			return;
		}

		if (!formsInUse.containsValue(form.getId())) {
			AbcdLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has locked '" + form.getLabel()
					+ "'");
			formsInUse.put(user, form.getId());
		}
	}

	public static synchronized void releaseForm(User user) {
		if (user != null) {
			if (formsInUse.get(user) != null) {
				AbcdLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has released form with id '"
						+ formsInUse.get(user) + "'");
				formsInUse.remove(user);
			}
		}
	}
}
