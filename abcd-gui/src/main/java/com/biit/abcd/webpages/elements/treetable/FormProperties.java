package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.TextField;

public class FormProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Form instance;
	private TextField formName;
	private TextField formVersion;
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

		String createdBy = instance.getCreatedBy() == null ? "" : instance.getCreatedBy().toString();
		String updatedBy = instance.getUpdatedBy() == null ? "" : instance.getUpdatedBy().toString();
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

		getFormLayout().addComponent(formName);
		getFormLayout().addComponent(formVersion);
		getFormLayout().addComponent(elementCreatedBy);
		getFormLayout().addComponent(elementCreationTime);
		getFormLayout().addComponent(elementUpdatedBy);
		getFormLayout().addComponent(elementUpdateTime);
	}

}
