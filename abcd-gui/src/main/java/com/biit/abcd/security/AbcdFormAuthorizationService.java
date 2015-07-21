package com.biit.abcd.security;

import com.biit.abcd.UiAccesser;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.usermanager.entity.IUser;

public class AbcdFormAuthorizationService extends AbcdAuthorizationService {

	private static AbcdFormAuthorizationService instance = new AbcdFormAuthorizationService();

	protected AbcdFormAuthorizationService() {
		super();
	}

	public static AbcdFormAuthorizationService getInstance() {
		return instance;
	}

	public boolean isFormReadOnly(Form form, IUser<Long> user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form, user) || isFormAlreadyInUse(form.getId(), user) || !form.isLastVersion()
				|| !form.getStatus().equals(FormWorkStatus.DESIGN);
	}

	public boolean isFormReadOnly(SimpleFormView form, IUser<Long> user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form.getOrganizationId(), user) || isFormAlreadyInUse(form.getId(), user)
				|| !form.isLastVersion() || !form.getStatus().equals(FormWorkStatus.DESIGN);
	}

	public boolean isFormAlreadyInUse(Long formId, IUser<Long> user) {
		IUser<Long> userUsingForm = UiAccesser.getUserUsingForm(formId);
		return (userUsingForm != null) && !userUsingForm.getId().equals(user.getId());
	}

}
