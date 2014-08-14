package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;
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
		answerTechnicalLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		answerTechnicalLabel.setValue(instance.getName());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(answerTechnicalLabel);

		addTab(answerForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_ANSWER_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		try {
			instance.setName(answerTechnicalLabel.getValue());
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				instance.setName(answerTechnicalLabel.getValue().substring(0, 185));
			} catch (FieldTooLongException e1) {
				// Impossible.
			}
		}
		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}
}