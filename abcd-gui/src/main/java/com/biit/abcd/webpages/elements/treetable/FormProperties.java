package com.biit.abcd.webpages.elements.treetable;

import java.sql.Timestamp;
import java.util.Date;

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

		availableTo = new DateField(ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_TO));
		availableTo.setValue(instance.getAvailableTo());

		FormLayout formForm = new FormLayout();
		formForm.setWidth(null);
		formForm.addComponent(formName);
		formForm.addComponent(formVersion);
		formForm.addComponent(availableFrom);
		formForm.addComponent(availableTo);
		addValueChangeListenerToFormComponents(formForm);

		getRootAccordion().addTab(formForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_FORM_FORM_CAPTION),true,0);

	}

	@Override
	public void updateElement() {
		instance.setAvailableFrom(new Timestamp(((Date)availableFrom.getValue()).getTime()));
		instance.setAvailableTo(new Timestamp(((Date)availableTo.getValue()).getTime()));
	}

}
