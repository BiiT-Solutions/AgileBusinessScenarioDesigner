package com.biit.abcd.security;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
