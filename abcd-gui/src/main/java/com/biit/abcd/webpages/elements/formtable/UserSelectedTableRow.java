package com.biit.abcd.webpages.elements.formtable;

import java.util.HashMap;

import com.biit.abcd.persistence.entity.Form;
import com.liferay.portal.model.User;

/**
 * Stores the last selected row for a specific user.
 */
class UserSelectedTableRow {
	private static UserSelectedTableRow instance = new UserSelectedTableRow();
	// User has selected a form.
	private HashMap<Long, Long> selectedForm = new HashMap<Long, Long>();

	private UserSelectedTableRow() {
	}

	protected static UserSelectedTableRow getInstance() {
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