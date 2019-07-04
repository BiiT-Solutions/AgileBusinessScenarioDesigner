package com.biit.abcd.webpages.elements.form.designer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.form.entity.TreeObject;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class GenericFormElementProperties<T> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -8230738772806878748L;

	private IAbcdFormAuthorizationService securityService;

	public GenericFormElementProperties(Class<? extends T> type) {
		super(type);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	public void setElement(Object element) {
		super.setElement(element);
		initCommonProperties(element);
	}

	protected void initCommonProperties(Object element) {
		createdBy = new TextField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdBy.setEnabled(false);
		creationTime = new TextField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTime.setEnabled(false);
		updatedBy = new TextField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedBy.setEnabled(false);
		updateTime = new TextField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTime.setEnabled(false);
		clearOriginalReference = new Button(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_CLEAR_REFERENCE_ID));
		clearOriginalReference.setEnabled(false);
		updateTime.setEnabled(false);
		// init values;
		initCommonPropertiesValues((TreeObject) element);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdBy);
		commonProperties.addComponent(creationTime);
		commonProperties.addComponent(updatedBy);
		commonProperties.addComponent(updateTime);
		commonProperties.addComponent(clearOriginalReference);

		addTab(commonProperties, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION), false);
	}

	protected void initCommonPropertiesValues(final TreeObject element) {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";
		try {
			valueCreatedBy = element.getCreatedBy() == null ? "" : securityService.getUserById(element.getCreatedBy()).getEmailAddress();
		} catch (UserManagementException udne) {
			valueCreatedBy = element.getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = element.getUpdatedBy() == null ? "" : securityService.getUserById(element.getUpdatedBy()).getEmailAddress();
		} catch (UserManagementException udne) {
			valueUpdatedBy = element.getUpdatedBy() + "";
		}

		String valueCreationTime = element.getCreationTime() == null ? "" : element.getCreationTime() + "";
		String valueUpdatedTime = element.getUpdateTime() == null ? "" : element.getUpdateTime() + "";

		createdBy.setValue(valueCreatedBy);
		creationTime.setValue(valueCreationTime);
		updatedBy.setValue(valueUpdatedBy);
		updateTime.setValue(valueUpdatedTime);

		if (!element.getOriginalReference().equals(element.getComparationId())) {
			clearOriginalReference.setEnabled(true);
			clearOriginalReference.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -6636243712290823803L;

				@Override
				public void buttonClick(ClickEvent event) {
					final AlertMessageWindow windowAccept = new AlertMessageWindow(LanguageCodes.WARNING_TEST_SCENARIOS_CLEAR_ID);
					windowAccept.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							element.setOriginalReference(element.getComparationId());
							clearOriginalReference.setEnabled(false);
							windowAccept.close();
						}
					});
					windowAccept.showCentered();
				}
			});
		}
	}

	@Override
	public void updateElement() {
		getTreeObjectInstance().setUpdatedBy(UserSessionHandler.getUser());
		getTreeObjectInstance().setUpdateTime();
		updateConcreteFormElement();
		// Update common ui fields.
		initCommonPropertiesValues(getTreeObjectInstance());

		firePropertyUpdateListener(getTreeObjectInstance());
	}

	protected abstract void updateConcreteFormElement();

	protected abstract TreeObject getTreeObjectInstance();

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(getTreeObjectInstance());
	}
}
