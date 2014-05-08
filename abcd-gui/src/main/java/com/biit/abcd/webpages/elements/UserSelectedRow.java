package com.biit.abcd.webpages.elements;

import java.util.HashMap;

import com.biit.abcd.persistence.entity.Form;
import com.liferay.portal.model.User;

/**
 * Stores the last selected row for a specific user.
 */
class UserSelectedRow {
	private static UserSelectedRow instance = new UserSelectedRow();
	// User has selected a form.
	private HashMap<Long, Long> selectedForm = new HashMap<Long, Long>();

	private UserSelectedRow() {
	}

	protected static UserSelectedRow getInstance() {
		return instance;
	}

	public void setSelected(User user, Form form) {
		if (user != null && form != null) {
			selectedForm.put(user.getUserId(), form.getId());
		}
	}

	public Long getSelectedFormId(User user) {
		if (user != null) {
			return selectedForm.get(user.getUserId());
		}
		return null;
	}
}