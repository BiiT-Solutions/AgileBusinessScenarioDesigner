package com.biit.abcd.webpages.elements.formdesigner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorDuplicateNameOnSameTreeObjectLevel;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectName;
import com.biit.abcd.webpages.elements.formdesigner.validators.ValidatorTreeObjectNameLength;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class AnswerProperties extends SecuredFormElementProperties<Answer> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Answer instance;
	private TextField answerLabel;
	private final String TECHNICAL_NAME_VALIDATOR_REGEX = "[^<& ]+";

	public AnswerProperties() {
		super(Answer.class);
	}

	@Override
	public void setElementForProperties(Answer element) {
		instance = element;

		answerLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		answerLabel.addValidator(new ValidatorTreeObjectName(instance.getNameAllowedPattern()));
		answerLabel.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(instance));
		answerLabel.addValidator(new ValidatorTreeObjectNameLength());
		answerLabel.addValidator(new RegexpValidator(TECHNICAL_NAME_VALIDATOR_REGEX, ServerTranslate
				.translate(LanguageCodes.TECHNICAL_NAME_ERROR)));
		answerLabel.setValue(instance.getName());

		FormLayout answerForm = new FormLayout();
		answerForm.setWidth(null);
		answerForm.addComponent(answerLabel);

		addTab(answerForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_ANSWER_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		if (answerLabel.isValid()) {
			String instanceName = instance.getName();
			// To avoid setting repeated values
			if (!answerLabel.getValue().equals(instanceName)) {
				try {
					instance.setName(answerLabel.getValue());
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Answer '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "'.");
				} catch (FieldTooLongException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
							LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
					try {
						try {
							instance.setName(answerLabel.getValue().substring(0, 185));
							AbcdLogger.info(this.getClass().getName(), "User '"
									+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Answer '"
									+ instanceName + "' property 'Name' to '" + instance.getName()
									+ "' (Name too long).");
						} catch (CharacterNotAllowedException e1) {
							MessageManager.showWarning(ServerTranslate
									.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate
									.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
											instance.getName(), instance.getSimpleAsciiName() }));
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
					MessageManager.showWarning(
							ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate
									.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
											instance.getName(), instance.getSimpleAsciiName() }));
					try {
						instance.setName(instance.getSimpleAsciiName());
					} catch (FieldTooLongException | CharacterNotAllowedException e1) {
						// Impossible.
					}
				}
			}
		}
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(answerLabel));
	}
}