package com.biit.abcd.webpages.elements.testscenario;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputPostalCode;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputText;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerRadioButton;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioObject;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestionAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioRepeatedGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioRepeatedGroupContainer;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("rawtypes")
public class TestScenarioForm extends Panel {
	private static final long serialVersionUID = -3526986076061463631L;
	private Form form;
	private TestScenario testScenario;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private Accordion backgroundAccordion = null;
	private Map<Field, TestScenarioQuestionAnswer> fieldQuestionMap;
	private HashMap<String, TreeObject> absolutePathTestScenarioObjectMap;

	public TestScenarioForm() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) throws NotValidChildException {
		createContent(form, testScenario);
	}

	/**
	 * Creates the content for the complete test scenario definition
	 * 
	 * @param form
	 * @param testScenario
	 * @throws NotValidChildException
	 */
	private void createContent(Form form, TestScenario testScenario) throws NotValidChildException {
		if ((form != null) && (testScenario != null)) {
			this.form = form;
			this.testScenario = testScenario;
			createTestScenarioObjectAbsolutePathNameMap();

			fieldQuestionMap = new HashMap<Field, TestScenarioQuestionAnswer>();
			backgroundAccordion = new Accordion();
			setCaption(form.getName());
			backgroundAccordion.setSizeFull();
			backgroundAccordion.setStyleName(Runo.ACCORDION_LIGHT);

			TestScenarioObject testForm;
			if (absolutePathTestScenarioObjectMap.containsKey(form.getUniqueNameReadable())) {
				testForm = (TestScenarioObject) absolutePathTestScenarioObjectMap.get(form.getUniqueNameReadable());
			} else {
				testForm = new TestScenarioObject(form.getUniqueNameReadable());
				testForm.setAbsoluteGenericPath(form.getUniqueNameReadable());
				this.testScenario.setTestScenarioForm(testForm);
				absolutePathTestScenarioObjectMap.put(form.getUniqueNameReadable(), testForm);
			}
			// Get the categories
			List<TreeObject> categories = form.getChildren();
			if (categories != null) {
				for (TreeObject category : categories) {
					TestScenarioObject testCategory = getTestScenarioObject(testForm, category.getUniqueNameReadable(),
							false);
					backgroundAccordion.addTab(createCategoryContent((Category) category, testCategory),
							category.getName());
				}
			}
			setContent(backgroundAccordion);
			setHeight(100.0f, Unit.PERCENTAGE);
		} else {
			setCaption("");
			setContent(null);
		}
	}

	private Component createCategoryContent(Category category, TestScenarioObject testCategory)
			throws NotValidChildException {
		// Create the category panels
		VerticalLayout categoryBackground = new VerticalLayout();
		// Put category children variables
		List<TreeObject> categoryChildren = category.getChildren();
		if (categoryChildren != null) {
			FormLayout categoryFormLayout = new FormLayout();

			for (TreeObject categoryChild : categoryChildren) {
				int categoryVerticalLayoutChildIndex = 0;
				if (categoryChild instanceof Group) {
					if (((Group) categoryChild).isRepeatable()) {
						createRepeatedGroupContent((Group) categoryChild, testCategory, categoryBackground,
								categoryVerticalLayoutChildIndex);
					} else {
						categoryBackground.addComponent(createGroupContent((Group) categoryChild, testCategory,
								categoryChild.getUniqueNameReadable()));
						categoryVerticalLayoutChildIndex++;
					}
				} else if (categoryChild instanceof Question) {
					Field field = getFormLayoutField((Question) categoryChild, testCategory,
							category.getUniqueNameReadable());
					if (field != null) {
						categoryFormLayout.addComponent(field);
					}
					categoryVerticalLayoutChildIndex++;
				}
			}
			// Add the form to the category background
			categoryBackground.addComponent(categoryFormLayout);
			categoryBackground.setMargin(true);
		}
		return categoryBackground;
	}

	private Component createGroupContent(Group group, TestScenarioObject testScenarioObjectParent,
			String groupIdentifier) throws NotValidChildException {

		TestScenarioObject testgroup = getTestScenarioObject(testScenarioObjectParent, groupIdentifier, false);

		// Create the group panels
		Panel groupPanel = new Panel(group.getSimpleAsciiName());
		VerticalLayout groupBackground = new VerticalLayout();
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {
			FormLayout groupFormLayout = new FormLayout();

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof Group) {
					createGroupContent((Group) groupChild, testgroup, groupChild.getUniqueNameReadable());

				} else if (groupChild instanceof Question) {
					Field field = getFormLayoutField((Question) groupChild, testgroup, groupIdentifier);
					if (field != null) {
						groupFormLayout.addComponent(field);
					}
				}
			}
			// Add the form to the category background
			groupBackground.addComponent(groupFormLayout);
			groupBackground.setMargin(true);
			groupPanel.setContent(groupBackground);
		}
		return groupPanel;
	}

	private void createRepeatedGroupContent(Group group, TestScenarioObject testScenarioObjectParent,
			VerticalLayout parentVerticalLayout, Integer categoryVerticalLayoutIndex) throws NotValidChildException {
		int groupIndex = 0;

		// Creation/Retrieve of the container for the repeated groups
		TestScenarioRepeatedGroupContainer repeatedGroupContainerObject;
		if (absolutePathTestScenarioObjectMap.containsKey(group.getUniqueNameReadable())) {
			TestScenarioObject genericTestObject = (TestScenarioObject) absolutePathTestScenarioObjectMap.get(group
					.getUniqueNameReadable());
			if (genericTestObject instanceof TestScenarioRepeatedGroupContainer) {
				repeatedGroupContainerObject = (TestScenarioRepeatedGroupContainer) genericTestObject;
			} else {
				// The group was previously not repeatable and now it is
				repeatedGroupContainerObject = new TestScenarioRepeatedGroupContainer(group.getName());
				testScenarioObjectParent.addChild(repeatedGroupContainerObject);
				repeatedGroupContainerObject.setAbsoluteGenericPath(group.getUniqueNameReadable());
				absolutePathTestScenarioObjectMap.put(group.getUniqueNameReadable(), repeatedGroupContainerObject);
			}
		} else {
			repeatedGroupContainerObject = new TestScenarioRepeatedGroupContainer(group.getUniqueNameReadable());
			testScenarioObjectParent.addChild(repeatedGroupContainerObject);
			repeatedGroupContainerObject.setAbsoluteGenericPath(group.getUniqueNameReadable());
			absolutePathTestScenarioObjectMap.put(group.getUniqueNameReadable(), repeatedGroupContainerObject);
		}
		if (repeatedGroupContainerObject.getChildren().isEmpty()) {
			// Creation of the first child in the container
			String repeatedGroupIdentifier = group.getUniqueNameReadable() + group.getName() + groupIndex;
			TestScenarioRepeatedGroup repeatedGroupObject = new TestScenarioRepeatedGroup(repeatedGroupIdentifier);
			repeatedGroupContainerObject.addChild(repeatedGroupObject);
			repeatedGroupObject.setAbsoluteGenericPath(repeatedGroupIdentifier);
			absolutePathTestScenarioObjectMap.put(repeatedGroupIdentifier, repeatedGroupObject);
			parentVerticalLayout.addComponent(createGroupContent(group, repeatedGroupObject, repeatedGroupIdentifier));
			groupIndex++;
			categoryVerticalLayoutIndex++;
		} else {
			for (TreeObject genericGroupObject : repeatedGroupContainerObject.getChildren()) {
				TestScenarioRepeatedGroup repeatedGroupObject = (TestScenarioRepeatedGroup) genericGroupObject;
				parentVerticalLayout.addComponent(createGroupContent(group, repeatedGroupObject,
						repeatedGroupObject.getAbsoluteGenericPath()));
				groupIndex++;
				categoryVerticalLayoutIndex++;
			}
		}
		parentVerticalLayout.addComponent(createRepeatableGroupsButton(parentVerticalLayout, group,
				categoryVerticalLayoutIndex, repeatedGroupContainerObject, groupIndex));
	}

	/**
	 * Creates the repeated groups
	 * 
	 * @param background
	 * @param group
	 * @param categorVerticalLayoutChildIndex
	 *            : index to know where to put the new group generated inside
	 *            the background container
	 * @param testScenarioObjectParent
	 * @param groupIndex
	 *            : index to know what name assign to the repeated group
	 * @return
	 */
	private Button createRepeatableGroupsButton(final VerticalLayout background, final Group group,
			final Integer categorVerticalLayoutChildIndex, final TestScenarioObject testScenarioObjectParent,
			final Integer groupIndex) {
		Button newGroup = new Button("+ " + group.getName());
		newGroup.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3058038173905239509L;
			private int layoutIndex = categorVerticalLayoutChildIndex;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					// Create a new repeated group
					String repeatedGroupIdentifier = group.getUniqueNameReadable() + group.getName() + groupIndex;
					TestScenarioRepeatedGroup repeatedGroupObject = new TestScenarioRepeatedGroup(
							repeatedGroupIdentifier);
					testScenarioObjectParent.addChild(repeatedGroupObject);
					repeatedGroupObject.setAbsoluteGenericPath(repeatedGroupIdentifier);
					absolutePathTestScenarioObjectMap.put(repeatedGroupIdentifier, repeatedGroupObject);
					// Add it to the background
					background.addComponent(createGroupContent(group, repeatedGroupObject, repeatedGroupIdentifier),
							layoutIndex);
					layoutIndex++;
				} catch (NotValidChildException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
		return newGroup;
	}

	private Field<Field> getFormLayoutField(Question question, TestScenarioObject testScenarioObject,
			String parentIdentifier) throws NotValidChildException {
		TestAnswer testAnswer = null;
		Field field = null;
		String questionIdentifier = question.getUniqueNameReadable();
		TestScenarioObject testObject = getTestScenarioObject(testScenarioObject, questionIdentifier, true);
		if (testObject instanceof TestScenarioQuestionAnswer) {
			TestScenarioQuestionAnswer testQuestionAnswer = (TestScenarioQuestionAnswer) testObject;
			testAnswer = testQuestionAnswer.getTestAnswer();

			switch (question.getAnswerType()) {
			case RADIO:
				field = new ComboBox(question.getName());
				for (TreeObject answer : question.getChildren()) {
					((ComboBox) field).addItem(answer.getName());
				}
				if (testAnswer == null) {
					testAnswer = new TestAnswerRadioButton();
					testQuestionAnswer.setTestAnswer(testAnswer);
				} else {
					if (checkQuestionTestAnswerIntegrity(TestAnswerRadioButton.class, testAnswer)) {
						if (testAnswer.getValue() != null) {
							((ComboBox) field).select(testAnswer.getValue());
						}
					} else {
						testAnswer = new TestAnswerRadioButton();
						testQuestionAnswer.setTestAnswer(testAnswer);
					}
				}
				break;
			case MULTI_CHECKBOX:
				field = new ListSelect(question.getName());
				((ListSelect) field).setMultiSelect(true);
				((ListSelect) field).setRows(4);
				for (TreeObject answer : question.getChildren()) {
					((ListSelect) field).addItem(answer.getName());
				}
				if (testAnswer == null) {
					testAnswer = new TestAnswerMultiCheckBox();
					testQuestionAnswer.setTestAnswer(testAnswer);
				} else {
					if (testAnswer.getValue() != null) {
						if (checkQuestionTestAnswerIntegrity(TestAnswerMultiCheckBox.class, testAnswer)) {
							Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
							for (String value : values) {
								((ListSelect) field).select(value);
							}
						} else {
							testAnswer = new TestAnswerMultiCheckBox();
							testQuestionAnswer.setTestAnswer(testAnswer);
						}
					}
				}
				break;
			case INPUT:
				switch (question.getAnswerFormat()) {
				case TEXT:
					field = new TextField(question.getName());
					((TextField) field).setInputPrompt("TEXT");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputText();
						testQuestionAnswer.setTestAnswer(testAnswer);
					} else {
						if (checkQuestionTestAnswerIntegrity(TestAnswerInputText.class, testAnswer)) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputText();
							testQuestionAnswer.setTestAnswer(testAnswer);
						}
					}
					break;
				case POSTAL_CODE:
					field = new TextField(question.getName());
					((TextField) field).setInputPrompt("0000AA");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputPostalCode();
						testQuestionAnswer.setTestAnswer(testAnswer);
					} else {
						if (checkQuestionTestAnswerIntegrity(TestAnswerInputPostalCode.class, testAnswer)) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputPostalCode();
							testQuestionAnswer.setTestAnswer(testAnswer);
						}
					}
					break;
				case NUMBER:
					field = new TextField(question.getName());
					((TextField) field).setInputPrompt("1.234");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputNumber();
						testQuestionAnswer.setTestAnswer(testAnswer);
					} else {
						if (checkQuestionTestAnswerIntegrity(TestAnswerInputNumber.class, testAnswer)) {
							if (testAnswer.getValue() != null) {
								((TextField) field).setValue(testAnswer.getValue().toString());
							}
						} else {
							testAnswer = new TestAnswerInputNumber();
							testQuestionAnswer.setTestAnswer(testAnswer);
						}
					}
					((TextField) field).addValidator(new RegexpValidator(NUMBER_FIELD_VALIDATOR_REGEX, ServerTranslate
							.translate(LanguageCodes.INPUT_DATA_FORMAT_INCORRECT_ERROR)));
					break;
				case DATE:
					field = new PopupDateField(question.getName());
					((PopupDateField) field).setInputPrompt("dd/mm/yy");
					if (testAnswer == null) {
						testAnswer = new TestAnswerInputDate();
						testQuestionAnswer.setTestAnswer(testAnswer);
					} else {
						if (checkQuestionTestAnswerIntegrity(TestAnswerInputDate.class, testAnswer)) {
							if (testAnswer.getValue() != null) {
								Timestamp value = ((TestAnswerInputDate) testAnswer).getValue();
								((PopupDateField) field).setValue(new Date(value.getTime()));
							}
						} else {
							testAnswer = new TestAnswerInputDate();
							testQuestionAnswer.setTestAnswer(testAnswer);
						}
					}
					break;
				}
			}

			if (field != null) {
				((AbstractComponent) field).setImmediate(true);
				field.addValueChangeListener(new FieldValueChangeListener((AbstractField<?>) field));
				// Add the value to a map to be consulted later
				fieldQuestionMap.put(field, testQuestionAnswer);
			}
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
			TestScenarioQuestionAnswer questionAnswer = fieldQuestionMap.get(field);
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

	private boolean checkQuestionTestAnswerIntegrity(Class<?> answerType, TestAnswer answer) {
		if (answerType.isInstance(answer)) {
			return true;
		} else {
			return false;
		}
	}

	public TestScenario getTestScenario() {
		return testScenario;
	}

	public Form getForm() {
		return form;
	}

	/**
	 * Creates the map that stores the test scenario objects information
	 */
	private void createTestScenarioObjectAbsolutePathNameMap() {
		absolutePathTestScenarioObjectMap = new HashMap<String, TreeObject>();
		TestScenarioObject testForm = testScenario.getTestScenarioForm();
		if (testForm != null) {
			fillAbsolutePathTestScenarioObjectMap(testForm);
		}
	}

	private void fillAbsolutePathTestScenarioObjectMap(TestScenarioObject testScenarioObject) {
		absolutePathTestScenarioObjectMap.put(testScenarioObject.getAbsoluteGenericPath(), testScenarioObject);
		for (TreeObject child : testScenarioObject.getChildren()) {
			fillAbsolutePathTestScenarioObjectMap((TestScenarioObject) child);
		}
	}

	/**
	 * Returns the test scenario object or a new test scenario object if not
	 * found
	 * 
	 * @param testScenarioObjectParent
	 *            : parent of the object searched
	 * @param absolutePathName
	 *            : absolute name path of the object searched
	 * @return
	 * @throws NotValidChildException
	 */
	private TestScenarioObject getTestScenarioObject(TestScenarioObject testScenarioObjectParent,
			String absolutePathName, boolean question) throws NotValidChildException {
		TestScenarioObject testScenarioObject;
		if (absolutePathTestScenarioObjectMap.containsKey(absolutePathName)) {
			testScenarioObject = (TestScenarioObject) absolutePathTestScenarioObjectMap.get(absolutePathName);
			System.out.println("TREE OBJECT RETRIEVED: " + testScenarioObject.getAbsoluteGenericPath());
		} else {
			if (question) {
				testScenarioObject = new TestScenarioQuestionAnswer(absolutePathName);
			} else {
				testScenarioObject = new TestScenarioObject(absolutePathName);
			}
			try {
				testScenarioObjectParent.addChild(testScenarioObject);
				testScenarioObject.setAbsoluteGenericPath(absolutePathName);
				absolutePathTestScenarioObjectMap.put(absolutePathName, testScenarioObject);
				System.out.println("TREE OBJECT CREATED: " + testScenarioObject.getAbsoluteGenericPath());
			} catch (NotValidChildException e) {
				if (testScenarioObjectParent instanceof TestScenarioRepeatedGroupContainer) {
					// The user changed the property repeatable of the group in
					// the form structure
					// We have to create a new parent
					testScenarioObjectParent = new TestScenarioObject(testScenarioObjectParent.getAbsoluteGenericPath());
					absolutePathTestScenarioObjectMap.put(testScenarioObjectParent.getAbsoluteGenericPath(),
							testScenarioObjectParent);
					getTestScenarioObject(testScenarioObjectParent, absolutePathName, question);
					System.out.println("TREE OBJECT MODIFIED: " + testScenarioObjectParent.getAbsoluteGenericPath());
				}
				// throw new NotValidChildException(e.getMessage());
			}

		}
		return testScenarioObject;
	}
}
