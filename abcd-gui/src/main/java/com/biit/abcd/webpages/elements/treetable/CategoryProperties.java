package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class CategoryProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Category instance;
	private TextField categoryLabel;

	public CategoryProperties() {
	}

	@Override
	public void setElementAbstract(TreeObject element) {
		instance = (Category) element;

		categoryLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		categoryLabel.setValue(instance.getName());
		addValueChangeListenerToField(categoryLabel);

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(categoryLabel);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION),0);
	}

	@Override
	public void updateElement() {
		instance.setName(categoryLabel.getValue());
		instance.setUpdatedBy(UserSessionHandler.getUser());
		instance.setUpdateTime();
		firePropertyUpdateListener(instance);
	}

}