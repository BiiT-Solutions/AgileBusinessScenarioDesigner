package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class AnswerProperties extends PropertiesComponent {
	private static final long serialVersionUID = -7673405239560362757L;

	private Answer instance;
	private TextField answerTechnicalLabel;

	public AnswerProperties() {
	}

	@Override
	public void setElementAbstract(TreeObject element) {
		instance = (Answer) element;

		answerTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		answerTechnicalLabel.setValue(instance.getName());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(answerTechnicalLabel);
		addValueChangeListenerToFormComponents(answerForm);

		getRootAccordion().addTab(answerForm,
				ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_ANSWER_FORM_CAPTION),true,0);
	}

	@Override
	public void updateElement() {
		instance.setName(answerTechnicalLabel.getValue());
		instance.setUpdatedBy(UserSessionHandler.getUser());
		instance.setUpdateTime();
		firePropertyUpdateListener(instance);
	}
}