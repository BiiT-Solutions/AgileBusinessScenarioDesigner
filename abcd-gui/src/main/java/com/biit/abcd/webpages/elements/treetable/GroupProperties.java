package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;

public class GroupProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Group instance;
	private TextField groupTechnicalLabel;
	private CheckBox groupIsRepeatable;
	private TextField elementCreatedBy;
	private TextField elementCreationTime;
	private TextField elementUpdatedBy;
	private TextField elementUpdateTime;

	public GroupProperties() {
	}

	@Override
	public void setElement(TreeObject element) {
		instance = (Group) element;

		groupTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.setValue(instance.getTechnicalName());
		groupIsRepeatable = new CheckBox(ServerTranslate.tr(LanguageCodes.GROUP_PROPERTIES_REPEAT));
		groupIsRepeatable.setValue(instance.isRepetable());

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

		getFormLayout().addComponent(groupTechnicalLabel);
		getFormLayout().addComponent(groupIsRepeatable);
		getFormLayout().addComponent(elementCreatedBy);
		getFormLayout().addComponent(elementCreationTime);
		getFormLayout().addComponent(elementUpdatedBy);
		getFormLayout().addComponent(elementUpdateTime);
	}
}