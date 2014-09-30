package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
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
	public void setElementForProperties(Answer element) {
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
		String instanceName = instance.getName();
		try {
			instance.setName(answerTechnicalLabel.getValue());
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has modified the Answer '" + instanceName + "' property 'Name' to '" + instance.getName()
					+ "'.");
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				try {
					instance.setName(answerTechnicalLabel.getValue().substring(0, 185));
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Answer '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "' (Name too long).");
				} catch (CharacterNotAllowedException e1) {
					MessageManager.showWarning(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS,
							LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION);
					try {
						instance.setName(instance.getSimpleAsciiName());
					} catch (CharacterNotAllowedException e2) {
						// Impossible.
					}
				}
			} catch (FieldTooLongException e1) {
				// Impossible.
			}
		} catch (CharacterNotAllowedException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS,
					LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION);
			try {
				instance.setName(instance.getSimpleAsciiName());
			} catch (FieldTooLongException | CharacterNotAllowedException e1) {
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