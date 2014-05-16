package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class QuestionProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Question instance;
	private TextField questionTechnicalLabel;

	public QuestionProperties() {
	}

	@Override
	public void setElementAbstract(TreeObject element) {
		instance = (Question) element;

		questionTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		questionTechnicalLabel.setValue(instance.getName());
		addValueChangeListenerToField(questionTechnicalLabel);

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(questionTechnicalLabel);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_QUESTION_FORM_CAPTION),0);
	}

	@Override
	public void updateElement() {
		instance.setName(questionTechnicalLabel.getValue());
		instance.setUpdatedBy(UserSessionHandler.getUser());
		instance.setUpdateTime();
		firePropertyUpdateListener(instance);
	}

}