package com.biit.abcd.webpages.elements.diagram.builder;

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

import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;

public abstract class SecuredDiagramElementProperties<T> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = 3707582105881695713L;

	private IAbcdFormAuthorizationService securityService;

	public SecuredDiagramElementProperties(Class<? extends T> type) {
		super(type);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	public void setElement(Object element) {
		super.setElement(element);
		disableProtectedElements();
	}

	protected void disableProtectedElements() {
		if (securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(),
				UserSessionHandler.getUser())) {
			for (AbstractComponent component : getProtectedElements()) {
				if (component != null) {
					component.setEnabled(false);
				}
			}
		}
	}

	/**
	 * Returns a list of elements that would be protected if the user has not
	 * the correct rights.
	 *
	 * @return a set of components.
	 */
	protected abstract Set<AbstractComponent> getProtectedElements();

}
