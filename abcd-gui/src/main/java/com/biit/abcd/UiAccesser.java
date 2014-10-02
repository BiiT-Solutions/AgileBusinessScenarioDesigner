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

	private static HashMap<User, Form> formsInUse = new HashMap<User, Form>();

	public static synchronized boolean isUserUserUsingForm(User user, Form form) {
		return formsInUse.get(form) != null && formsInUse.get(form).equals(user);
	}

	public static synchronized User getUserUsingForm(Form form) {
		for (User user : formsInUse.keySet()) {
			if (formsInUse.get(user).equals(form)) {
				return user;
			}
		}
		return null;
	}

	public static synchronized User getUserUsingForm(Long formId) {
		for (User user : formsInUse.keySet()) {
			if (formsInUse.get(user) != null && formsInUse.get(user).getId().equals(formId)) {
				return user;
			}
		}
		return null;
	}

	public static synchronized void lockForm(Form form, User user) {
		if (form == null || user == null) {
			return;
		}

		if (!formsInUse.containsKey(form)) {
			AbcdLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has locked '" + form
					+ "'");
			formsInUse.put(user, form);
		}
	}

	public static synchronized void releaseForm(User user) {
		if (user != null) {
			if (formsInUse.get(user) != null) {
				AbcdLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress() + "' has released '"
						+ formsInUse.get(user) + "'");
				formsInUse.remove(user);
			}
		}
	}
}
