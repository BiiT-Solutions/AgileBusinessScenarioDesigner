package com.biit.abcd.security;

import com.biit.abcd.UiAccesser;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.liferay.portal.model.User;

public class AbcdFormAuthorizationService extends AbcdAuthorizationService {

	private static AbcdFormAuthorizationService instance = new AbcdFormAuthorizationService();

	protected AbcdFormAuthorizationService() {
		super();
	}

	public static AbcdFormAuthorizationService getInstance() {
		return instance;
	}

	public boolean isFormReadOnly(Form form, User user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form, user) || isFormAlreadyInUse(form.getId(), user) || !form.isLastVersion();
	}

	public boolean isFormReadOnly(SimpleFormView form, User user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form.getOrganizationId(), user) || isFormAlreadyInUse(form.getId(), user)
				|| !form.isLastVersion();
	}

	public boolean isFormAlreadyInUse(Long formId, User user) {
		User userUsingForm = UiAccesser.getUserUsingForm(formId);
		return (userUsingForm != null) && userUsingForm.getUserId() != user.getUserId();
	}

}
