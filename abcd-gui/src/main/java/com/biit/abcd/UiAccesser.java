package com.biit.abcd;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.usermanager.entity.IUser;

public class UiAccesser {
	static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public interface BroadcastListener {
		void receiveBroadcast(String message);
	}

	// User --> Id Form
	private static HashMap<IUser<Long>, Long> formsInUse = new HashMap<IUser<Long>, Long>();

	public static synchronized boolean isUserUsingForm(IUser<Long> user, Form form) {
		return formsInUse.get(user) != null && formsInUse.get(user).equals(form.getId());
	}

	public static synchronized IUser<Long> getUserUsingForm(Form form) {
		for (IUser<Long> user : formsInUse.keySet()) {
			if (formsInUse.get(user).equals(form.getId())) {
				return user;
			}
		}
		return null;
	}

	public static synchronized IUser<Long> getUserUsingForm(Long formId) {
		for (IUser<Long> user : formsInUse.keySet()) {
			if (formsInUse.get(user) != null && formsInUse.get(user).equals(formId)) {
				return user;
			}
		}
		return null;
	}

	public static synchronized void lockForm(Form form, IUser<Long> user) {
		if (form == null || user == null) {
			return;
		}

		if (!formsInUse.containsValue(form.getId())) {
			AbcdLogger.info(UiAccesser.class.getName(),
					"User '" + user.getEmailAddress() + "' has locked '" + form.getLabel() + "'");
			formsInUse.put(user, form.getId());
		}
	}

	public static synchronized void releaseForm(IUser<Long> user) {
		if (user != null) {
			if (formsInUse.get(user) != null) {
				AbcdLogger.info(UiAccesser.class.getName(), "User '" + user.getEmailAddress()
						+ "' has released form with id '" + formsInUse.get(user) + "'");
				formsInUse.remove(user);
			}
		}
	}
}
