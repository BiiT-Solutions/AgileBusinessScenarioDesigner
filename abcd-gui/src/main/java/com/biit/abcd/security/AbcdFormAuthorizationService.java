package com.biit.abcd.security;

import com.biit.abcd.UiAccesser;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.security.SecurityService;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.vaadin.server.VaadinServlet;

public class AbcdFormAuthorizationService extends SecurityService implements IAbcdFormAuthorizationService {

	@Override
	public boolean isFormReadOnly(Form form, IUser<Long> user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form, user) || isFormAlreadyInUse(form.getId(), user) || !form.isLastVersion()
				|| !form.getStatus().equals(FormWorkStatus.DESIGN);
	}

	@Override
	public boolean isFormReadOnly(SimpleFormView form, IUser<Long> user) {
		if (form == null || user == null) {
			return true;
		}
		return !isAuthorizedToForm(form.getOrganizationId(), user) || isFormAlreadyInUse(form.getId(), user)
				|| !form.isLastVersion() || !form.getStatus().equals(FormWorkStatus.DESIGN);
	}

	@Override
	public boolean isFormAlreadyInUse(Long formId, IUser<Long> user) {
		IUser<Long> userUsingForm = UiAccesser.getUserUsingForm(formId);
		return (userUsingForm != null) && !userUsingForm.getUniqueId().equals(user.getUniqueId());
	}

	/**
	 * Autowired not working correctly with this configuration of Vaadin. Use the helper if needed.
	 * 
	 * @return the authentication service.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IAuthenticationService<Long, Long> getAuthenticationService() {
		if (super.getAuthenticationService() == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			super.setAuthenticationService((IAuthenticationService<Long, Long>) helper.getBean("authenticationService"));
		}
		return super.getAuthenticationService();
	}

	/**
	 * Autowired not working correctly with this configuration of Vaadin. Use the helper if needed.
	 * 
	 * @return the authentication service.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IAuthorizationService<Long, Long, Long> getAuthorizationService() {
		if (super.getAuthorizationService() == null) {
			SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
			super.setAuthorizationService((IAuthorizationService<Long, Long, Long>) helper
					.getBean("authenticationService"));
		}
		return super.getAuthorizationService();
	}

}
