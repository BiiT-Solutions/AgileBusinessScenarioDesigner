package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class AnswerProperties extends GenericFormElementProperties<Answer> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Answer instance;
	private TextField answerTechnicalLabel;

	public AnswerProperties() {
		super(Answer.class);
	}

	@Override
	public void setElementAbstract(Answer element) {
		instance = element;
		answerTechnicalLabel = new TextField(ServerTranslate.tr(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		answerTechnicalLabel.setValue(instance.getName());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(answerTechnicalLabel);

		addTab(answerForm, ServerTranslate.tr(LanguageCodes.TREE_OBJECT_PROPERTIES_ANSWER_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		instance.setName(answerTechnicalLabel.getValue());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}
}