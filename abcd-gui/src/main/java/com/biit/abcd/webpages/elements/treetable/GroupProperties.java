package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class GroupProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Group instance;
	private TextField groupTechnicalLabel;
	private CheckBox groupIsRepeatable;

	public GroupProperties() {
	}

	@Override
	public void setElementAbstract(TreeObject element) {
		instance = (Group) element;

		groupTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.setValue(instance.getName());
		addValueChangeListenerToField(groupTechnicalLabel);
		groupIsRepeatable = new CheckBox(ServerTranslate.tr(LanguageCodes.GROUP_PROPERTIES_REPEAT));
		groupIsRepeatable.setValue(instance.isRepetable());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(groupTechnicalLabel);
		answerForm.addComponent(groupIsRepeatable);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_GROUP_FORM_CAPTION),0);
	}

	@Override
	public void updateElement() {
		instance.setName(groupTechnicalLabel.getValue());
		instance.setRepetable(groupIsRepeatable.getValue());
		instance.setUpdatedBy(UserSessionHandler.getUser());
		instance.setUpdateTime();
		firePropertyUpdateListener(instance);
	}
}