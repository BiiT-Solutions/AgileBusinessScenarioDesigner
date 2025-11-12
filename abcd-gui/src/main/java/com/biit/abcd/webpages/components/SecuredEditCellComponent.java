package com.biit.abcd.webpages.components;

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

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.vaadin.server.VaadinServlet;

public class SecuredEditCellComponent extends EditCellComponent {

	private static final long serialVersionUID = -2609821632560785408L;

	private IAbcdFormAuthorizationService securityService;

	protected SecuredEditCellComponent() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	protected void addButtons() {
		if (getEditButton().getParent() == null) {
			if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getUser())) {
				if (!isOnlyEdit()) {
					getRootLayout().addComponent(getRemoveButton(), 0);
				}
				getRootLayout().addComponent(getEditButton(), 0);
			}
		}
	}

	@Override
	protected void removeButtons() {
		if (getEditButton().getParent() != null) {
			if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getUser())) {
				getRootLayout().removeComponent(getEditButton());
				if (!isOnlyEdit()) {
					getRootLayout().removeComponent(getRemoveButton());
				}
			}
		}
	}

	@Override
	protected void doubleClickElement() {
		if (!securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			super.doubleClickElement();
		}
	}
}
