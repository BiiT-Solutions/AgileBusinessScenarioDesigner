package com.biit.abcd.webpages.elements.testscenario;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputPostalCode;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputText;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerRadioButton;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

@SuppressWarnings("rawtypes")
public class CustomQuestionEditor extends CustomComponent {
	private static final long serialVersionUID = 3099517634702528173L;
	private FormLayout questionsLayout;
	private Map<Field, TestScenarioQuestion> fieldQuestionMap;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private static String DATE_FORMAT = "dd/MM/yyyy";
	private Set<FieldValueChangedListener> fieldValueChangeListeners;

	public CustomQuestionEditor(HashMap<String, TreeObject> originalReferenceTreeObjectMap,
			List<TreeObject> testScenarioObjects) {
		this.originalReferenceTreeObjectMap = originalReferenceTreeObjectMap;
		fieldValueChangeListeners = new HashSet<>();
		setCompositionRoot(generateContent());
		setQuestionLayoutFields(testScenarioObjects);
	}

	private Component generateContent() {
		fieldQuestionMap = new HashMap<Field, TestScenarioQuestion>();
		questionsLayout = new FormLayout();
		questionsLayout.setWidth("100%");
		questionsLayout.setHeight(null);
		return questionsLayout;
	}

	private void setQuestionLayoutFields(List<TreeObject> questions) {
		for (TreeObject question : questions) {
			if (question instanceof TestScenarioQuestion) {
				TestScenarioQuestion testQuestion = (TestScenarioQuestion) question;
				Field field = getFormLayoutField(testQuestion);
				if (field != null) {
					questionsLayout.addComponent(field);
				}
			}
		}
	}

	/**
	 * Creates the field based on the type of the question and retrieves the answer information if there is any.
	 * 
	 * @param testQuestion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Field<Field> getFormLayoutField(TestScenarioQuestion testQuestion) {
		TreeObject treeObject = originalReferenceTreeObjectMap.get(testQuestion.getOriginalReference());
		Question question = (Question) treeObject;
		TestAnswer testAnswer = testQuestion.getTestAnswer();
		Field field = null;
		if (question != null) {
			switch (question.getAnswerType()) {
			case RADIO:
				field = new ComboBox(testQuestion.getName());
				for (TreeObject answer : question.getChildren()) {
					// Check for subAnswers
					List<TreeObject> subAnswerList = answer.getChildren();
					if ((subAnswerList != null) && !(subAnswerList.isEmpty())) {
						for (TreeObject subAnswer : subAnswerList) {
							((ComboBox) field).addItem(subAnswer.getName());
							((ComboBox) field).setItemCaption(subAnswer.getName(),
									answer.getName() + "/" + subAnswer.getName());
						}
					} else {
						((ComboBox) field).addItem(answer.getName());
					}
				}
				if (testAnswer == null) {
					testAnswer = new TestAnswerRadioButton();
					testQuestion.setTestAnswer(testAnswer);
				} else {
					if (testAnswer instanceof TestAnswerRadioButton) {
						if (testAnswer.getValue() != null) {
							((ComboBox) field).select(testAnswer.getValue());
						}
					} else {
						testAnswer = new TestAnswerRadioButton();
						testQuestion.setTestAnswer(testAnswer);
					}
				}
				break;
			case MULTI_CHECKBOX:
				field = new ListSelect(testQuestion.getName());
				((ListSelect) field).setMultiSelect(true);
				((ListSelect) field).setNullSelectionAllowed(true);
				((ListSelect) field).setRows(4);
				for (TreeObject answer : question.getChildren()) {
					((ListSelect) field).addItem(answer.getName());
				}

				if (testAnswer == null) {
					testAnswer = new TestAnswerMultiCheckBox();
					testQuestion.setTestAnswer(testAnswer);
				} else {
					if (testAnswer.getValue() != null) {
						if (testAnswer instanceof TestAnswerMultiCheckBox) {
							Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
							for (String value : values) {
								((ListSelect) field).select(value);
							}
						} else {
							testAnswer = new TestAnswerMultiCheckBox();
							testQuestion.setTestAnswer(testAnswer);
						}
					}
				}
				break;
			case INPUT:
				switch (question.getAnswerFormat()) {
				case TEXT:
					field = new TextField(testQuestion.getName());
					((TextField) field).setInputPrompt("TEXT");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputText();
						testQuestion.setTestAnswer(testAnswer);
					} else {
						if (testAnswer instanceof TestAnswerInputText) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputText();
							testQuestion.setTestAnswer(testAnswer);
						}
					}
					break;
				case POSTAL_CODE:
					field = new TextField(testQuestion.getName());
					((TextField) field).setInputPrompt("0000AA");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputPostalCode();
						testQuestion.setTestAnswer(testAnswer);
					} else {
						if (testAnswer instanceof TestAnswerInputPostalCode) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputPostalCode();
							testQuestion.setTestAnswer(testAnswer);
						}
					}
					break;
				case NUMBER:
					field = new TextField(testQuestion.getName());
					((TextField) field).setInputPrompt("1.234");
					((TextField) field).addValidator(new RegexpValidator(NUMBER_FIELD_VALIDATOR_REGEX, ServerTranslate
							.translate(LanguageCodes.INPUT_DATA_FORMAT_INCORRECT_ERROR)));
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputNumber();
						testQuestion.setTestAnswer(testAnswer);
					} else {
						if (testAnswer instanceof TestAnswerInputNumber) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputNumber();
							testQuestion.setTestAnswer(testAnswer);
						}
					}
					break;
				case DATE:
					field = new PopupDateField(testQuestion.getName());
					((PopupDateField) field).setInputPrompt(DATE_FORMAT);
					((PopupDateField) field).setDateFormat(DATE_FORMAT);
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputDate();
						testQuestion.setTestAnswer(testAnswer);
					} else {
						if (testAnswer instanceof TestAnswerInputDate) {
							if (testAnswer.getValue() != null) {
								Timestamp value = ((TestAnswerInputDate) testAnswer).getValue();
								((PopupDateField) field).setValue(new Date(value.getTime()));
							}
						} else {
							testAnswer = new TestAnswerInputDate();
							testQuestion.setTestAnswer(testAnswer);
						}
					}
					break;
				}
			}
			if (field != null) {
				((AbstractComponent) field).setImmediate(true);
				field.addValueChangeListener(new FieldValueChangeListener((AbstractField<?>) field));
				// Add the value to a map to be consulted later
				fieldQuestionMap.put(field, testQuestion);
			}
		}
		return field;
	}

	/**
	 * Sets the listeners and validators for the created fields
	 * 
	 */
	private class FieldValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = 2277281871213884287L;
		AbstractField<?> field;

		public FieldValueChangeListener(AbstractField<?> field) {
			this.field = field;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (field.isAttached() && field.isEnabled()) {
				if ((field.getValidators() != null) && (!field.getValidators().isEmpty())) {
					Collection<Validator> validators = field.getValidators();
					for (Validator validator : validators) {
						try {
							validator.validate(field.getValue());
							updateTestScenario(field);
						} catch (InvalidValueException e) {
							AbcdLogger.warning(this.getClass().getName(), e.toString());
						}
					}
				} else {
					updateTestScenario(field);
				}
			}
		}
	}

	/**
	 * Stores the value of the field in the test scenario answer
	 * 
	 * @param field
	 */
	private void updateTestScenario(Field field) {
		try {
			TestScenarioQuestion questionAnswer = fieldQuestionMap.get(field);
			if (questionAnswer.getTestAnswer() instanceof TestAnswerInputNumber) {
				try {
					Double value = Double.parseDouble(field.getValue().toString());
					questionAnswer.getTestAnswer().setValue(value);
				} catch (NumberFormatException | NullPointerException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			} else {
				questionAnswer.getTestAnswer().setValue(field.getValue());
			}
			for (FieldValueChangedListener listener : fieldValueChangeListeners) {
				listener.valueChanged(field);
			}
		} catch (NotValidAnswerValue | NullPointerException e) {
			e.printStackTrace();
		}
	}

	protected void addFieldValueChangeListener(FieldValueChangedListener listener) {
		fieldValueChangeListeners.add(listener);
	}
}
