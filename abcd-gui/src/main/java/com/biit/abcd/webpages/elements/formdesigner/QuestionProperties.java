package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.AnswerTypeUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class QuestionProperties extends GenericFormElementProperties<Question> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Question instance;
	private TextField questionTechnicalLabel;
	private ComboBox answerType;
	private ComboBox answerFormat;

	public QuestionProperties() {
		super(Question.class);
	}

	@Override
	public void setElementAbstract(Question element) {
		instance = element;
		questionTechnicalLabel = new TextField(ServerTranslate.translate(LanguageCodes.PROPERTIES_TECHNICAL_NAME));
		questionTechnicalLabel.setValue(instance.getName());

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
		try {
			instance.setName(questionTechnicalLabel.getValue());
		} catch (FieldTooLongException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_NAME_TOO_LONG,
					LanguageCodes.WARNING_NAME_TOO_LONG_DESCRIPTION);
			try {
				instance.setName(questionTechnicalLabel.getValue().substring(0, 185));
			} catch (FieldTooLongException e1) {
				// Impossible.
			}
		}
		try {
			instance.setAnswerFormat((AnswerFormat) answerFormat.getValue());
		} catch (InvalidAnswerFormatException e) {
			// Not input fields must remove any answer format
			try {
				instance.setAnswerFormat(null);
			} catch (InvalidAnswerFormatException e1) {
				// Do nothing.
			}
		}
		instance.setAnswerType((AnswerType) answerType.getValue());

		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}
}