package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class CategoryProperties extends GenericFormElementProperties<Category> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Category instance;
	private TextField categoryLabel;

	public CategoryProperties() {
		super(new Category());
	}

	@Override
	public void setElementAbstract(Category element) {
		instance = element;
		categoryLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		categoryLabel.setValue(instance.getName());

		FormLayout categoryForm = new FormLayout();
		categoryForm.setWidth(null);
		categoryForm.addComponent(categoryLabel);

		addTab(categoryForm, ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_CATEGORY_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		instance.setName(categoryLabel.getValue());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

}