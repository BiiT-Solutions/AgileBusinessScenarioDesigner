package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.liferay.LiferayServiceAccess;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.vaadin.ui.TextField;

public class FormProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Form instance;
	private TextField formName;
	private TextField formVersion;
	private TextField availableFrom;
	private TextField availableTo;
	private TextField elementCreatedBy;
	private TextField elementCreationTime;
	private TextField elementUpdatedBy;
	private TextField elementUpdateTime;

	public FormProperties() {
	}

	@Override
	public void setElement(TreeObject element) {
		instance = (Form) element;

		formName = new TextField(ServerTranslate.tr(LanguageCodes.FORM_PROPERTIES_NAME));
		formName.setValue(instance.getName());
		formVersion = new TextField(ServerTranslate.tr(LanguageCodes.FORM_PROPERTIES_VERSION));
		formVersion.setValue(instance.getVersion().toString());

		String createdBy = "";
		String updatedBy = "";
		try {
			createdBy = instance.getCreatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(instance.getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			createdBy = instance.getCreatedBy() + "";
		}

		try {
			updatedBy = instance.getUpdatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(instance.getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			updatedBy = instance.getUpdatedBy() + "";
		}
		
		String availableFromTime = instance.getAvailableFrom() == null ? "" : instance.getAvailableFrom().toString();
		availableFrom = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_FROM));
		availableFrom.setValue(availableFromTime);
		
		String availableToTime = instance.getAvailableTo() == null ? "" : instance.getAvailableTo().toString();
		availableTo = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_TO));
		availableTo.setValue(availableToTime);

		String creationTime = instance.getCreationTime() == null ? "" : instance.getCreationTime().toString();
		String updatedTime = instance.getUpdateTime() == null ? "" : instance.getUpdateTime().toString();
		elementCreatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		elementCreatedBy.setValue(createdBy);
		elementCreationTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		elementCreationTime.setValue(creationTime);
		elementUpdatedBy = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		elementUpdatedBy.setValue(updatedBy);
		elementUpdateTime = new TextField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		elementUpdateTime.setValue(updatedTime);

		addFormField(formName);
		addFormField(formVersion);
		addFormField(availableFrom);
		addFormField(availableTo);
		addFormField(elementCreatedBy);
		addFormField(elementCreationTime);
		addFormField(elementUpdatedBy);
		addFormField(elementUpdateTime);
	}

	@Override
	public void updateElement() {

	}

}
