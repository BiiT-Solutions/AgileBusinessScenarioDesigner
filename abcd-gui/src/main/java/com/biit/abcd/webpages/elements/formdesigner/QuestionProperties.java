package com.biit.abcd.webpages.elements.formdesigner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.AnswerTypeUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class QuestionProperties extends SecuredFormElementProperties<Question> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Question instance;
	private TextField questionTechnicalLabel;
	private ComboBox answerType;
	private ComboBox answerFormat;
	private final String TECHNICAL_NAME_VALIDATOR_REGEX = "([A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff_])([0-9A-Za-z\\xc0-\\xd6\\xd8-\\xf6\\xf8-\\xff\\_\\xb7]){2,}";

	public QuestionProperties() {
		super(Question.class);
	}

	@Override
	public void setElementForProperties(Question element) {
		instance = element;
		questionTechnicalLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		questionTechnicalLabel.setValue(instance.getName());
		questionTechnicalLabel.addValidator(new RegexpValidator(TECHNICAL_NAME_VALIDATOR_REGEX, ServerTranslate
				.translate(LanguageCodes.TECHNICAL_NAME_ERROR)));

		initializeSelectionLists();
		if (instance.getAnswerType() != null) {
			answerType.setValue(instance.getAnswerType());
			answerFormat.setValue(instance.getAnswerFormat());
		}

		FormLayout questionForm = new FormLayout();
		questionForm.setWidth(null);
		questionForm.addComponent(questionTechnicalLabel);
		questionForm.addComponent(answerType);
		questionForm.addComponent(answerFormat);

		addTab(questionForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_QUESTION_FORM_CAPTION),
				true, 0);
	}

	private void initializeSelectionLists() {

		answerFormat = new ComboBox(ServerTranslate.translate(LanguageCodes.PROPERTIES_QUESTION_ANSWER_FORMAT));
		answerFormat.setImmediate(true);
		for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
			answerFormat.addItem(answerFormatUi.getAnswerFormat());
			answerFormat.setItemCaption(answerFormatUi.getAnswerFormat(),
					ServerTranslate.translate(answerFormatUi.getLanguageCode()));
		}

		answerType = new ComboBox(ServerTranslate.translate(LanguageCodes.PROPERTIES_QUESTION_ANSWER_TYPE));
		answerType.setNullSelectionAllowed(false);
		answerType.setImmediate(true);
		for (AnswerTypeUi answerTypeUi : AnswerTypeUi.values()) {
			answerType.addItem(answerTypeUi.getAnswerType());
			answerType.setItemCaption(answerTypeUi.getAnswerType(),
					ServerTranslate.translate(answerTypeUi.getLanguageCode()));
		}
		answerType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6322255129871221711L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (!((AnswerType) answerType.getValue()).equals(AnswerType.INPUT)) {
					answerFormat.setNullSelectionAllowed(true);
					answerFormat.setValue(null);
					answerFormat.setEnabled(false);
				} else {
					answerFormat.setEnabled(true);
					answerFormat.setNullSelectionAllowed(false);
					answerFormat.setValue(AnswerFormatUi.values()[0].getAnswerFormat());
				}
			}
		});
		answerType.setValue(AnswerTypeUi.values()[0].getAnswerType());
	}

	@Override
	protected void updateConcreteFormElement() {
		String instanceName = instance.getName();
		try {
			instance.setName(questionTechnicalLabel.getValue());
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has modified the Question '" + instanceName + "' property 'Name' to '" + instance.getName()
					+ "'.");
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				try {
					instance.setName(questionTechnicalLabel.getValue().substring(0, 185));
					AbcdLogger.info(this.getClass().getName(), "User '"
							+ UserSessionHandler.getUser().getEmailAddress() + "' has modified the Question '"
							+ instanceName + "' property 'Name' to '" + instance.getName() + "' (Name too long).");
				} catch (CharacterNotAllowedException e1) {
					MessageManager.showWarning(
							ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS), ServerTranslate
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
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS),
					ServerTranslate.translate(LanguageCodes.WARNING_NAME_INVALID_CHARACTERS_DESCRIPTION, new Object[] {
							instance.getName(), instance.getSimpleAsciiName() }));
			try {
				instance.setName(instance.getSimpleAsciiName());
			} catch (FieldTooLongException | CharacterNotAllowedException e1) {
				// Impossible.
			}
		}
		try {
			instance.setAnswerFormat((AnswerFormat) answerFormat.getValue());
			AbcdLogger.info(this.getClass().getName(),
					"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has modified the Question '"
							+ instance.getName() + "' property 'AnswerType' to '" + instance.getAnswerFormat() + "'.");
		} catch (InvalidAnswerFormatException e) {
			// Not input fields must remove any answer format
			try {
				instance.setAnswerFormat(null);
				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has modified the Question '" + instance.getName() + "' property 'AnswerType' to '" + null
						+ "'.");
			} catch (InvalidAnswerFormatException e1) {
				// Do nothing.
			}
		}
		instance.setAnswerType((AnswerType) answerType.getValue());
		AbcdLogger.info(
				this.getClass().getName(),
				"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has set the Question '"
						+ instance.getName() + "' property 'AnswerFormat' to '" + instance.getAnswerType() + "'.");

		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(questionTechnicalLabel, answerType, answerFormat));
	}
}