package com.biit.abcd.webpages.elements.testscenario;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.BaseGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("rawtypes")
public class TestScenarioMainLayout extends HorizontalLayout {
	private static final long serialVersionUID = -3526986076061463631L;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private Map<Field, TestScenarioQuestion> fieldQuestionMap;
	private TestScenarioTable treeTestTable;
	private FormLayout rightFormLayout;

	public TestScenarioMainLayout() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException {
		removeAllComponents();
		createContent(form, testScenario);
	}

	/**
	 * Creates the content for the complete test scenario definition
	 * 
	 * @param form
	 * @param testScenario
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 */
	private void createContent(Form form, TestScenario testScenario) throws NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException {
		if (form != null && testScenario != null) {
			fieldQuestionMap = new HashMap<Field, TestScenarioQuestion>();

			createTreeTable(testScenario);
			createEditForm();

			addComponent(treeTestTable);
			addComponent(rightFormLayout);
		}
	}

	private void createTreeTable(TestScenario testScenario) {
		treeTestTable = new TestScenarioTable();
		treeTestTable.setSizeFull();
		treeTestTable.setRootElement(testScenario.getTestScenarioForm());
		treeTestTable.setSelectable(true);
		treeTestTable.setNullSelectionAllowed(false);
		treeTestTable.setImmediate(true);
		treeTestTable.setValue(testScenario.getTestScenarioForm());
		treeTestTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1835114202597377993L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Object valueSelected = event.getProperty().getValue();
				if (valueSelected instanceof BaseGroup) {
					setEditFormFields(((BaseGroup) valueSelected).getChildren(Question.class));
				}
			}
		});
	}

	private void createEditForm() {
		rightFormLayout = new FormLayout();
		rightFormLayout.setSizeFull();
	}

	public FormLayout getRightFormLayout() {
		return rightFormLayout;
	}

	public void setRightFormLayout(FormLayout rightFormLayout) {
		this.rightFormLayout = rightFormLayout;
	}

	public TestScenarioTable getTreeTestTable() {
		return treeTestTable;
	}

	public void setTreeTestTable(TestScenarioTable treeTestTable) {
		this.treeTestTable = treeTestTable;
	}
	
	public void refreshTable(TreeObject treeObject){
		this.treeTestTable.setRootElement(treeObject);
	}

	private void setEditFormFields(List<TreeObject> questions) {
		getRightFormLayout().removeAllComponents();
		for (TreeObject question : questions) {
			if (question instanceof TestScenarioQuestion) {
				TestScenarioQuestion testQuestion = (TestScenarioQuestion) question;
				getRightFormLayout().addComponent(getFormLayoutField(testQuestion));
			}
		}
	}

	private Field<Field> getFormLayoutField(TestScenarioQuestion testQuestion) {
		TestAnswer testAnswer = null;
		Field field = null;
		switch (testQuestion.getAnswerType()) {
		case RADIO:
			field = new ComboBox(testQuestion.getName());
			for (TreeObject answer : testQuestion.getChildren()) {
				((ComboBox) field).addItem(answer.getName());
			}

			break;
		case MULTI_CHECKBOX:
			field = new ListSelect(testQuestion.getName());
			((ListSelect) field).setMultiSelect(true);
			((ListSelect) field).setRows(4);
			for (TreeObject answer : testQuestion.getChildren()) {
				((ListSelect) field).addItem(answer.getName());
			}
			break;
		case INPUT:
			switch (testQuestion.getAnswerFormat()) {
			case TEXT:
				field = new TextField(testQuestion.getName());
				((TextField) field).setInputPrompt("TEXT");
				break;
			case POSTAL_CODE:
				field = new TextField(testQuestion.getName());
				((TextField) field).setInputPrompt("0000AA");
				break;
			case NUMBER:
				field = new TextField(testQuestion.getName());
				((TextField) field).setInputPrompt("1.234");
				((TextField) field).addValidator(new RegexpValidator(NUMBER_FIELD_VALIDATOR_REGEX, ServerTranslate
						.translate(LanguageCodes.INPUT_DATA_FORMAT_INCORRECT_ERROR)));
				break;
			case DATE:
				field = new PopupDateField(testQuestion.getName());
				((PopupDateField) field).setInputPrompt("dd/mm/yy");
				break;
			}
		}
		if (field != null) {
			((AbstractComponent) field).setImmediate(true);
			field.addValueChangeListener(new FieldValueChangeListener((AbstractField<?>) field));
			// Add the value to a map to be consulted later
			fieldQuestionMap.put(field, testQuestion);
		}
		return field;
	}

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
		} catch (NotValidAnswerValue | NullPointerException e) {
			e.printStackTrace();
		}
	}

	// private Field<Field> getFormLayoutField(Question question,
	// TestScenarioObject testScenarioObject,
	// String parentIdentifier) throws NotValidChildException,
	// FieldTooLongException, CharacterNotAllowedException {
	// TestAnswer testAnswer = null;
	// Field field = null;
	// String questionIdentifier = parentIdentifier + "_" +
	// question.getDefaultTechnicalName()
	// + question.getParent().getChildren().indexOf(question) +
	// question.getSimpleAsciiName();
	//
	// TestScenarioObject testQuestion =
	// getTestScenarioObject(testScenarioObject, questionIdentifier, true);
	// testQuestion.setXmlTag(question.getName());
	//
	// testQuestion.setName(question.getUniqueNameReadable());
	// if (testQuestion instanceof TestScenarioQuestionAnswer) {
	// TestScenarioQuestionAnswer testQuestionAnswer =
	// (TestScenarioQuestionAnswer) testQuestion;
	// testAnswer = testQuestionAnswer.getTestAnswer();
	//
	// switch (question.getAnswerType()) {
	// case RADIO:
	// field = new ComboBox(question.getName());
	// for (TreeObject answer : question.getChildren()) {
	// ((ComboBox) field).addItem(answer.getName());
	// }
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerRadioButton();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerRadioButton.class,
	// testAnswer)) {
	// if (testAnswer.getValue() != null) {
	// ((ComboBox) field).select(testAnswer.getValue());
	// }
	// } else {
	// testAnswer = new TestAnswerRadioButton();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// break;
	// case MULTI_CHECKBOX:
	// field = new ListSelect(question.getName());
	// ((ListSelect) field).setMultiSelect(true);
	// ((ListSelect) field).setRows(4);
	// for (TreeObject answer : question.getChildren()) {
	// ((ListSelect) field).addItem(answer.getName());
	// }
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerMultiCheckBox();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (testAnswer.getValue() != null) {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerMultiCheckBox.class,
	// testAnswer)) {
	// Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
	// for (String value : values) {
	// ((ListSelect) field).select(value);
	// }
	// } else {
	// testAnswer = new TestAnswerMultiCheckBox();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// }
	// break;
	// case INPUT:
	// switch (question.getAnswerFormat()) {
	// case TEXT:
	// field = new TextField(question.getName());
	// ((TextField) field).setInputPrompt("TEXT");
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerInputText();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerInputText.class,
	// testAnswer)) {
	// if (testAnswer.getValue() != null) {
	// ((TextField) field).setValue(testAnswer.getValue().toString());
	// }
	// } else {
	// testAnswer = new TestAnswerInputText();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// break;
	// case POSTAL_CODE:
	// field = new TextField(question.getName());
	// ((TextField) field).setInputPrompt("0000AA");
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerInputPostalCode();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerInputPostalCode.class,
	// testAnswer)) {
	// if (testAnswer.getValue() != null) {
	// ((TextField) field).setValue(testAnswer.getValue().toString());
	// }
	// } else {
	// testAnswer = new TestAnswerInputPostalCode();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// break;
	// case NUMBER:
	// field = new TextField(question.getName());
	// ((TextField) field).setInputPrompt("1.234");
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerInputNumber();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerInputNumber.class,
	// testAnswer)) {
	// if (testAnswer.getValue() != null) {
	// ((TextField) field).setValue(testAnswer.getValue().toString());
	// }
	// } else {
	// testAnswer = new TestAnswerInputNumber();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// ((TextField) field).addValidator(new
	// RegexpValidator(NUMBER_FIELD_VALIDATOR_REGEX, ServerTranslate
	// .translate(LanguageCodes.INPUT_DATA_FORMAT_INCORRECT_ERROR)));
	// break;
	// case DATE:
	// field = new PopupDateField(question.getName());
	// ((PopupDateField) field).setInputPrompt("dd/mm/yy");
	// if (testAnswer == null) {
	// testAnswer = new TestAnswerInputDate();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// } else {
	// if (checkQuestionTestAnswerIntegrity(TestAnswerInputDate.class,
	// testAnswer)) {
	// if (testAnswer.getValue() != null) {
	// Timestamp value = ((TestAnswerInputDate) testAnswer).getValue();
	// ((PopupDateField) field).setValue(new Date(value.getTime()));
	// }
	// } else {
	// testAnswer = new TestAnswerInputDate();
	// testQuestionAnswer.setTestAnswer(testAnswer);
	// }
	// }
	// break;
	// }
	// }
	//
	// if (field != null) {
	// ((AbstractComponent) field).setImmediate(true);
	// field.addValueChangeListener(new
	// FieldValueChangeListener((AbstractField<?>) field));
	// // Add the value to a map to be consulted later
	// fieldQuestionMap.put(field, testQuestionAnswer);
	// }
	// }
	// return field;
	// }
	//
	// private class FieldValueChangeListener implements ValueChangeListener {
	// private static final long serialVersionUID = 2277281871213884287L;
	// AbstractField<?> field;
	//
	// public FieldValueChangeListener(AbstractField<?> field) {
	// this.field = field;
	// }
	//
	// @Override
	// public void valueChange(ValueChangeEvent event) {
	// if (field.isAttached() && field.isEnabled()) {
	// if ((field.getValidators() != null) &&
	// (!field.getValidators().isEmpty())) {
	// Collection<Validator> validators = field.getValidators();
	// for (Validator validator : validators) {
	// try {
	// validator.validate(field.getValue());
	// updateTestScenario(field);
	// } catch (InvalidValueException e) {
	// AbcdLogger.warning(this.getClass().getName(), e.toString());
	// }
	// }
	// } else {
	// updateTestScenario(field);
	// }
	// }
	// }
	// }
	//
	// private void updateTestScenario(Field field) {
	// try {
	// TestScenarioQuestionAnswer questionAnswer = fieldQuestionMap.get(field);
	// if (questionAnswer.getTestAnswer() instanceof TestAnswerInputNumber) {
	// try {
	// Double value = Double.parseDouble(field.getValue().toString());
	// questionAnswer.getTestAnswer().setValue(value);
	// } catch (NumberFormatException | NullPointerException e) {
	// AbcdLogger.errorMessage(this.getClass().getName(), e);
	// }
	// } else {
	// questionAnswer.getTestAnswer().setValue(field.getValue());
	// }
	// } catch (NotValidAnswerValue | NullPointerException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private boolean checkQuestionTestAnswerIntegrity(Class<?> answerType,
	// TestAnswer answer) {
	// if (answerType.isInstance(answer)) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	//
	// public TestScenario getTestScenario() {
	// return testScenario;
	// }
	//
	// public Form getForm() {
	// return form;
	// }
	//
	// /**
	// * Creates the map that stores the test scenario objects information
	// */
	// private void createTestScenarioObjectAbsolutePathNameMap() {
	// absolutePathTestScenarioObjectMap = new HashMap<String, TreeObject>();
	// TestScenarioObject testForm = testScenario.getTestScenarioForm();
	// if (testForm != null) {
	// fillAbsolutePathTestScenarioObjectMap(testForm);
	// }
	// }
	//
	// private void fillAbsolutePathTestScenarioObjectMap(TestScenarioObject
	// testScenarioObject) {
	// absolutePathTestScenarioObjectMap.put(testScenarioObject.getAbsoluteGenericPath(),
	// testScenarioObject);
	// for (TreeObject child : testScenarioObject.getChildren()) {
	// fillAbsolutePathTestScenarioObjectMap((TestScenarioObject) child);
	// }
	// }
	//
	// /**
	// * Returns the test scenario object or a new test scenario object if not
	// * found
	// *
	// * @param testScenarioObjectParent
	// * : parent of the object searched
	// * @param absolutePathName
	// * : absolute name path of the object searched
	// * @return
	// * @throws NotValidChildException
	// */
	// private TestScenarioObject getTestScenarioObject(TestScenarioObject
	// testScenarioObjectParent,
	// String absolutePathName, boolean question) throws NotValidChildException
	// {
	// TestScenarioObject testScenarioObject;
	// if (absolutePathTestScenarioObjectMap.containsKey(absolutePathName)) {
	// testScenarioObject = (TestScenarioObject)
	// absolutePathTestScenarioObjectMap.get(absolutePathName);
	// System.out.println("TREE OBJECT RETRIEVED: " +
	// testScenarioObject.getAbsoluteGenericPath());
	// } else {
	// if (question) {
	// testScenarioObject = new TestScenarioQuestionAnswer(absolutePathName);
	// } else {
	// testScenarioObject = new TestScenarioObject(absolutePathName);
	// }
	// try {
	// testScenarioObjectParent.addChild(testScenarioObject);
	// testScenarioObject.setAbsoluteGenericPath(absolutePathName);
	// absolutePathTestScenarioObjectMap.put(absolutePathName,
	// testScenarioObject);
	// System.out.println("TREE OBJECT CREATED: " +
	// testScenarioObject.getAbsoluteGenericPath());
	// } catch (NotValidChildException e) {
	// if (testScenarioObjectParent instanceof
	// TestScenarioRepeatedGroupContainer) {
	// // The user changed the property repeatable of the group in
	// // the form structure
	// // We have to create a new parent
	// testScenarioObjectParent = new
	// TestScenarioObject(testScenarioObjectParent.getAbsoluteGenericPath());
	// testScenarioObjectParent.setXmlTag(testScenarioObjectParent.getXmlTag());
	// absolutePathTestScenarioObjectMap.put(testScenarioObjectParent.getAbsoluteGenericPath(),
	// testScenarioObjectParent);
	// getTestScenarioObject(testScenarioObjectParent, absolutePathName,
	// question);
	// System.out.println("TREE OBJECT MODIFIED: " +
	// testScenarioObjectParent.getAbsoluteGenericPath());
	// }
	// // throw new NotValidChildException(e.getMessage());
	// }
	//
	// }
	// return testScenarioObject;
	// }
}
