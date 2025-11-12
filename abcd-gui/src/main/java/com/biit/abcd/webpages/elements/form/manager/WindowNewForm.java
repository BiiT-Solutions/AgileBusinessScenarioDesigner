package com.biit.abcd.webpages.elements.form.manager;

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

import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class WindowNewForm extends AcceptCancelWindow {
	private static final long serialVersionUID = 2963807969133587359L;
	private static final String width = "640px";
	private static final String height = "180px";

	private TextField textField;
	private ComboBox organizationField;
	private IActivity[] exclusivePermissionFilter;

	private IAbcdFormAuthorizationService securityService;

	public WindowNewForm(LanguageCodes windowsCaption, LanguageCodes inputFieldCaption, LanguageCodes groupCaption,
			AbcdActivity[] exclusivePermissionFilter) {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
		this.exclusivePermissionFilter = exclusivePermissionFilter;
		setContent(generateContent(inputFieldCaption, groupCaption));
		setCaption(ServerTranslate.translate(windowsCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public String getValue() {
		return textField.getValue();
	}

	@SuppressWarnings("unchecked")
	public IGroup<Long> getOrganization() {
		return (IGroup<Long>) organizationField.getValue();
	}

	private Component generateContent(LanguageCodes inputFieldCaption, LanguageCodes groupCaption) {
		textField = new TextField(ServerTranslate.translate(inputFieldCaption));
		textField.focus();
		textField.setWidth("100%");
		// textField.addValidator(new ValidatorTreeObjectName(BaseForm.NAME_ALLOWED));
		// textField.addValidator(new ValidatorTreeObjectNameLength());

		textField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4953347262492851075L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				textField.isValid();
			}
		});

		organizationField = new ComboBox(ServerTranslate.translate(groupCaption));
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");
		try {
			Set<IGroup<Long>> organizations = securityService.getUserOrganizations(UserSessionHandler.getUser());
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the filter in the group, then exit
					if (!securityService.isAuthorizedActivity(UserSessionHandler.getUser(), organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			for (IGroup<Long> organization : organizations) {
				organizationField.addItem(organization);
				organizationField.setItemCaption(organization, organization.getUniqueName());
			}
			if (!organizations.isEmpty()) {
				Iterator<IGroup<Long>> organizationIterator = organizations.iterator();
				organizationField.setValue(organizationIterator.next());
			}
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
		}

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		rootLayout.addComponent(textField);
		rootLayout.addComponent(organizationField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(organizationField, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(textField, 0.6f);
		rootLayout.setExpandRatio(organizationField, 0.4f);
		return rootLayout;
	}

	public void setValue(String value) {
		textField.setValue(value);
	}

	public boolean isValid() {
		return textField.isValid();
	}
}
