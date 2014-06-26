package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class GenericFormElementProperties<T> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -8230738772806878748L;

	public GenericFormElementProperties(Class<? extends T> type) {
		super(type);
	}

	public void setElement(Object element) {
		super.setElement(element);
		initCommonProperties(element);
		
	}

	protected void initCommonProperties(Object element) {
		createdBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdBy.setEnabled(false);
		creationTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTime.setEnabled(false);
		updatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedBy.setEnabled(false);
		updateTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTime.setEnabled(false);
		//init values;
		initCommonPropertiesValues((TreeObject) element);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdBy);
		commonProperties.addComponent(creationTime);
		commonProperties.addComponent(updatedBy);
		commonProperties.addComponent(updateTime);

		addTab(commonProperties, ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION), false);
	}

	protected void initCommonPropertiesValues(TreeObject element) {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";
		try {
			valueCreatedBy = element.getCreatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(element.getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueCreatedBy = element.getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = element.getUpdatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(element.getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueUpdatedBy = element.getUpdatedBy() + "";
		}

		String valueCreationTime = element.getCreationTime() == null ? "" : element.getCreationTime().toString();
		String valueUpdatedTime = element.getUpdateTime() == null ? "" : element.getUpdateTime().toString();

		createdBy.setValue(valueCreatedBy);
		creationTime.setValue(valueCreationTime);
		updatedBy.setValue(valueUpdatedBy);
		updateTime.setValue(valueUpdatedTime);
	}

	@Override
	public void updateElement() {
		getTreeObjectInstance().setUpdatedBy(UserSessionHandler.getUser());
		getTreeObjectInstance().setUpdateTime();
		updateConcreteFormElement();
		// Update common ui fields.
		initCommonPropertiesValues(getTreeObjectInstance());
	}

	protected abstract void updateConcreteFormElement();

	protected abstract TreeObject getTreeObjectInstance();

	@Override
	protected void firePropertyUpdateOnExitListener() {
		firePropertyUpdateListener(getTreeObjectInstance());
	}
}
