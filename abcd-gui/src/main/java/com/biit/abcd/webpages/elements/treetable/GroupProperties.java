package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class GroupProperties extends GenericFormElementProperties<Group> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Group instance;
	private TextField groupTechnicalLabel;
	private CheckBox groupIsRepeatable;

	public GroupProperties() {
		super(new Group());
	}

	@Override
	public void setElementAbstract(Group element) {
		instance = element;
		groupTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		groupTechnicalLabel.setValue(instance.getName());
		groupIsRepeatable = new CheckBox(ServerTranslate.tr(LanguageCodes.GROUP_PROPERTIES_REPEAT));
		groupIsRepeatable.setValue(instance.isRepetable());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(groupTechnicalLabel);
		answerForm.addComponent(groupIsRepeatable);
		addValueChangeListenerToFormComponents(answerForm);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_GROUP_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		instance.setName(groupTechnicalLabel.getValue());
		instance.setRepetable(groupIsRepeatable.getValue());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}
}