package com.biit.abcd.webpages.elements.formmanager;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectName;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectNameLength;
import com.biit.form.entity.BaseForm;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.liferay.portal.model.Organization;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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

	public WindowNewForm(LanguageCodes windowsCaption, LanguageCodes inputFieldCaption, LanguageCodes groupCaption,
			AbcdActivity[] exclusivePermissionFilter) {
		super();
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

	public Organization getOrganization() {
		return (Organization) organizationField.getValue();
	}

	private Component generateContent(LanguageCodes inputFieldCaption, LanguageCodes groupCaption) {
		textField = new TextField(ServerTranslate.translate(inputFieldCaption));
		textField.focus();
		textField.setWidth("100%");
		//textField.addValidator(new ValidatorTreeObjectName(BaseForm.NAME_ALLOWED));
		//textField.addValidator(new ValidatorTreeObjectNameLength());

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
			Set<Organization> organizations = AbcdFormAuthorizationService.getInstance().getUserOrganizations(
					UserSessionHandler.getUser());
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the filter in the group, then exit
					if (!AbcdFormAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
							organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			for (Organization organization : organizations) {
				organizationField.addItem(organization);
				organizationField.setItemCaption(organization, organization.getName());
			}
			if (!organizations.isEmpty()) {
				Iterator<Organization> organizationIterator = organizations.iterator();
				organizationField.setValue(organizationIterator.next());
			}
		} catch (IOException | AuthenticationRequired e) {
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
