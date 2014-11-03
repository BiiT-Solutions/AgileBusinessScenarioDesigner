package com.biit.abcd.security;

import java.util.Enumeration;
import java.util.Hashtable;

import com.biit.abcd.persistence.entity.Form;
import com.liferay.portal.model.User;

/**
 * Defines if an activity is authorized by an user or not in a form.
 */
public class AuthorizationByFormPool {

	private final static long EXPIRATION_TIME = 300000;// 300 seconds

	// Form -> time.
	private Hashtable<Form, Long> formTime;
	// Form -> user -> time.
	private Hashtable<Form, Hashtable<User, Long>> userTime;
	// Form -> user -> activity -> allowed.
	private Hashtable<Form, Hashtable<User, Hashtable<DActivity, Boolean>>> usersActivities;

	public AuthorizationByFormPool() {
		formTime = new Hashtable<Form, Long>();
		userTime = new Hashtable<Form, Hashtable<User, Long>>();
		usersActivities = new Hashtable<Form, Hashtable<User, Hashtable<DActivity, Boolean>>>();
	}

	/**
	 * Deletes old forms.
	 */
	private void updateForms() {
		long now = System.currentTimeMillis();
		Enumeration<Form> e = formTime.keys();
		while (e.hasMoreElements()) {
			Form form = e.nextElement();
			if ((now - formTime.get(form)) > EXPIRATION_TIME) {
				removeFormLists(form);
			}
		}
	}

	/**
	 * Returns true or false if the activity is authorized and null if is not catched.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public Boolean isAuthorizedActivity(Form form, User user, DActivity activity) {
		long now = System.currentTimeMillis();
		User userForm = null;

		if (form == null || usersActivities.get(form) == null) {
			return null;
		}

		if (userTime.get(form).size() > 0) {
			Enumeration<User> e = userTime.get(form).keys();
			while (e.hasMoreElements()) {
				userForm = e.nextElement();
				if ((now - userTime.get(form).get(userForm)) > EXPIRATION_TIME) {
					// object has expired
					removeUser(form, userForm);
					userForm = null;
				} else if (user.equals(userForm)) {
					if (usersActivities.get(form).get(user) != null) {
						return usersActivities.get(form).get(user).get(activity);
					}
				}
			}
		}
		return null;
	}

	public void addUser(Form form, User user, DActivity activity, boolean allowed) {
		if (form != null && user != null && activity != null) {
			if (userTime.get(form) == null) {
				userTime.put(form, new Hashtable<User, Long>());
			}

			if (usersActivities.get(form) == null) {
				usersActivities.put(form, new Hashtable<User, Hashtable<DActivity, Boolean>>());
			}

			if (usersActivities.get(form).get(user) == null) {
				usersActivities.get(form).put(user, new Hashtable<DActivity, Boolean>());
			}

			userTime.get(form).put(user, System.currentTimeMillis());
			usersActivities.get(form).get(user).put(activity, allowed);
		}
	}

	private void removeUser(Form form, User user) {
		if (form != null && user != null) {
			if (userTime.get(form) != null) {
				userTime.get(form).remove(user);
			}
			if (usersActivities.get(form) != null) {
				usersActivities.get(form).remove(user);
			}
		}
	}

	private void removeFormLists(Form form) {
		if (form != null) {
			userTime.remove(form);
			usersActivities.remove(form);
			formTime.remove(form);
		}
	}

	/**
	 * This method is used to
	 * 
	 * @param form
	 */
	public void removeForm(Form form) {
		if (form != null) {
			removeFormLists(form);
			// Check also for old forms.
			updateForms();
		}
	}
}
