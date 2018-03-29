package com.biit.abcd.webpages.elements.formmanager;

import java.util.Iterator;
import java.util.Set;

import com.biit.abcd.FromJson;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.core.security.ISecurityService;
import com.biit.abcd.exceptions.FormWithSameNameException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.google.gson.JsonParseException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowImportJson extends AcceptCancelWindow {
	private static final long serialVersionUID = -230576980225255963L;

	private static final String WINDOW_WIDTH = "800px";
	private static final String WINDOW_HEIGHT = "600px";
	private TextField textField;
	private ComboBox organizationField;
	private TextArea textArea;

	private ISecurityService securityService;

	public WindowImportJson() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (ISecurityService) helper.getBean("securityService");
		configure();
		setContent(generate());
		addAcceptActionListener(new AcceptActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				try {
					FromJson fromJson = new FromJson();
					fromJson.importFormFromJson(textArea.getValue(), textField.getValue(), ((IGroup<Long>) organizationField.getValue()).getUniqueId());
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.ERROR_REPEATED_FORM_NAME);
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.WARNING_NAME_TOO_LONG);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				} catch (JsonParseException jpe) {
					AbcdLogger.errorMessage(WindowImportJson.class.getName(), jpe);
					MessageManager.showError(LanguageCodes.INVALID_JSON_CODE);
				}
			}
		});
	}

	private Component generate() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		textArea = new TextArea();
		textArea.setSizeFull();

		Component component = nameOrganization(LanguageCodes.FORM_NAME.translate(), LanguageCodes.FORM_GROUP.translate());

		rootLayout.addComponent(textArea);
		rootLayout.addComponent(component);

		rootLayout.setExpandRatio(textArea, 1.0f);
		return rootLayout;
	}

	private Component nameOrganization(String inputFieldCaption, String groupCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();
		textField.setWidth("100%");

		organizationField = new ComboBox(groupCaption);
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");

		IActivity[] exclusivePermissionFilter = new IActivity[] { AbcdActivity.FORM_CREATE };
		try {
			Set<IGroup<Long>> organizations = securityService.getUserOrganizations(UserSessionHandler.getUser());
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
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
				Iterator<IGroup<Long>> organizationsIterator = organizations.iterator();
				organizationField.setValue(organizationsIterator.next());
			}
			if (organizations.size() <= 1) {
				organizationField.setEnabled(false);
			}
		} catch (UserManagementException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
		}

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		rootLayout.addComponent(textField);
		rootLayout.addComponent(organizationField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(organizationField, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(textField, 0.6f);
		rootLayout.setExpandRatio(organizationField, 0.4f);
		return rootLayout;
	}

	private void configure() {
		setDraggable(true);
		setResizable(false);
		setModal(true);
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}
}
