package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class FormProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Form instance;
	private TextField formName;
	private TextField formVersion;
	private DateField availableFrom;
	private DateField availableTo;

	public FormProperties() {
	}

	@Override
	public void setElementAbstract(TreeObject element) {
		instance = (Form) element;

		formName = new TextField(ServerTranslate.tr(LanguageCodes.FORM_PROPERTIES_NAME));
		formName.setValue(instance.getName());
		formName.setEnabled(false);
		formVersion = new TextField(ServerTranslate.tr(LanguageCodes.FORM_PROPERTIES_VERSION));
		formVersion.setValue(instance.getVersion().toString());
		formVersion.setEnabled(false);

		availableFrom = new DateField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_FROM));
		availableFrom.setValue(instance.getAvailableFrom());
		addValueChangeListenerToField(availableFrom);

		availableTo = new DateField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_TO));
		availableTo.setValue(instance.getAvailableTo());
		addValueChangeListenerToField(availableTo);

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(formName);
		answerForm.addComponent(formVersion);
		answerForm.addComponent(availableFrom);
		answerForm.addComponent(availableTo);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_FORM_FORM_CAPTION),0);

	}

	@Override
	public void updateElement() {

	}

}
