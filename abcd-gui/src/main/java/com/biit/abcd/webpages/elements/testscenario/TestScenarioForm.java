package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

@SuppressWarnings("rawtypes")
public class TestScenarioForm extends Panel {
	private static final long serialVersionUID = -3526986076061463631L;
	private Form form;
	private TestScenario testScenario;
//	private Map<Field, QuestionIndex> fieldQuestionMap = null;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private Accordion backgroundAccordion = null;
	private static final Integer GROUP_NOT_REPEATABLE = 0;

	public TestScenarioForm() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) {
//		createContent(form, testScenario);
	}

//	/**
//	 * Creates the content for the complete test scenario definition
//	 * 
//	 * @param form
//	 * @param testScenario
//	 */
//	private void createContent(Form form, TestScenario testScenario) {
//		if ((form != null) && (testScenario != null)) {
//			this.form = form;
//			this.testScenario = testScenario;
//			fieldQuestionMap = new HashMap<Field, QuestionIndex>();
//			backgroundAccordion = new Accordion();
//			setCaption(form.getName());
//			backgroundAccordion.setSizeFull();
//			backgroundAccordion.setStyleName(Runo.ACCORDION_LIGHT);
//			// Get the categories
//			List<TreeObject> categories = form.getChildren();
//			if (categories != null) {
//				for (TreeObject category : categories) {
//					backgroundAccordion.addTab(createCategoryContent((Category) category), category.getName());
//				}
//			}
//			setContent(backgroundAccordion);
//			setHeight(100.0f, Unit.PERCENTAGE);
//		} else {
//			setCaption("");
//			setContent(null);
//		}
//	}
//
//	private Component createCategoryContent(Category category) {
//		// Create the category panels
//		VerticalLayout categoryBackground = new VerticalLayout();
//		// Put category children variables
//		List<TreeObject> categoryChildren = category.getChildren();
//		if (categoryChildren != null) {
//			FormLayout categoryFormLayout = new FormLayout();
//
//			for (TreeObject categoryChild : categoryChildren) {
//				int categoryVerticalLayoutChildIndex = 0;
//				if (categoryChild instanceof Group) {
//					categoryBackground.addComponent(createGroupContent((Group) categoryChild));
//					categoryVerticalLayoutChildIndex++;
//					if (((Group) categoryChild).isRepeatable()) {
//						
//						
//						testScenario.containsQuestion(question)) {
//							if (repeatedGroupIndex < testScenario.getTestAnswerList(question).size()
//						
//						
//						categoryBackground.addComponent(createRepeatableGroupsButton(categoryBackground,
//								(Group) categoryChild, categoryVerticalLayoutChildIndex));
//										
//					}
//				} else if (categoryChild instanceof Question) {
//					categoryFormLayout.addComponent(getFormLayoutField((Question) categoryChild, GROUP_NOT_REPEATABLE));
//					categoryVerticalLayoutChildIndex++;
//				}
//			}
//			// Add the form to the category background
//			categoryBackground.addComponent(categoryFormLayout);
//			categoryBackground.setMargin(true);
//		}
//		return categoryBackground;
//	}
//
//	private TreeObject getDirectChildrenQuestion(Group group) {
//		for (TreeObject treeObject : group.getChildren()) {
//			if (treeObject instanceof Question) {
//				return treeObject;
//			}
//		}
//		return null;
//	}
//
//	private Component createGroupContent(Group group) {
//		// Create the group panels
//		Panel groupPanel = new Panel(group.getName());
//		VerticalLayout groupBackground = new VerticalLayout();
//		List<TreeObject> groupChildren = group.getChildren();
//		if (groupChildren != null) {
//			FormLayout groupFormLayout = new FormLayout();
//
//			for (TreeObject groupChild : groupChildren) {
//				if (groupChild instanceof Group) {
//					createNestedGroupVariables((Group) groupChild, groupBackground);
//
//				} else if (groupChild instanceof Question) {
//					groupFormLayout.addComponent(getFormLayoutField((Question) groupChild, GROUP_NOT_REPEATABLE));
//				}
//			}
//			// Add the form to the category background
//			groupBackground.addComponent(groupFormLayout);
//			groupBackground.setMargin(true);
//			groupPanel.setContent(groupBackground);
//		}
//		return groupPanel;
//	}
//
//	/**
//	 * Creates the repeated groups
//	 * 
//	 * @param group
//	 * @param repeatedGroupIndex
//	 *            : internal index to know the number of repeated groups
//	 * @return
//	 */
//	private Component createRepeatedGroupContent(Group group, Integer repeatedGroupIndex) {
//		// Create the group panels
//		Panel groupPanel = new Panel(group.getName());
//		VerticalLayout groupBackground = new VerticalLayout();
//		List<TreeObject> groupChildren = group.getChildren();
//		if (groupChildren != null) {
//			FormLayout groupFormLayout = new FormLayout();
//
//			for (TreeObject groupChild : groupChildren) {
//				if (groupChild instanceof Group) {
//					createNestedGroupVariables((Group) groupChild, groupBackground);
//
//				} else if (groupChild instanceof Question) {
//					groupFormLayout.addComponent(getFormLayoutField((Question) groupChild, repeatedGroupIndex));
//				}
//			}
//			// Add the form to the category background
//			groupBackground.addComponent(groupFormLayout);
//			groupBackground.setMargin(true);
//			groupPanel.setContent(groupBackground);
//		}
//		return groupPanel;
//	}
//
//	/**
//	 * Creates the repeated groups
//	 * 
//	 * @param background
//	 * @param group
//	 * @param categorVerticalLayoutChildIndex
//	 *            : index to know where to put the new group generated inside
//	 *            the background container
//	 * @return
//	 */
//	private Button createRepeatableGroupsButton(final VerticalLayout background, final Group group,
//			final Integer categorVerticalLayoutChildIndex) {
//		Button newGroup = new Button("+");
//		newGroup.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = -3058038173905239509L;
//			private int repeatedGroupIndex = 0;
//			private int layoutIndex = categorVerticalLayoutChildIndex;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				repeatedGroupIndex++;
//				background.addComponent(createRepeatedGroupContent(group, repeatedGroupIndex), layoutIndex);
//				layoutIndex++;
//			}
//		});
//		return newGroup;
//	}
//
//	private void createNestedGroupVariables(TreeObject group, VerticalLayout parentLayout) {
//		// Create the group panels
//		Panel groupPanel = new Panel(group.getName());
//		VerticalLayout groupBackground = new VerticalLayout();
//		List<TreeObject> groupChildren = group.getChildren();
//		if (groupChildren != null) {
//			FormLayout groupFormLayout = new FormLayout();
//
//			for (TreeObject groupChild : groupChildren) {
//				if (groupChild instanceof Group) {
//					createNestedGroupVariables((Group) groupChild, groupBackground);
//
//				} else if (groupChild instanceof Question) {
//					groupFormLayout.addComponent(getFormLayoutField((Question) groupChild, GROUP_NOT_REPEATABLE));
//				}
//			}
//			// Add the form to the category background
//			groupBackground.addComponent(groupFormLayout);
//			groupBackground.setMargin(true);
//			groupPanel.setContent(groupBackground);
//			parentLayout.addComponent(groupPanel);
//		}
//	}
//
//	private Field<Field> getFormLayoutField(Question question, int repeatedGroupIndex) {
//		TestAnswer testAnswer = null;
//		if (testScenario.containsQuestion(question)) {
//			if (repeatedGroupIndex < testScenario.getTestAnswerList(question).size()) {
//				testAnswer = testScenario.getTestAnswerList(question).getTestAnswer(repeatedGroupIndex);
//			}
//		}
//
//		Field field = null;
//		switch (question.getAnswerType()) {
//		case RADIO:
//			field = new ComboBox(question.getName());
//			for (TreeObject answer : question.getChildren()) {
//				((ComboBox) field).addItem(answer.getName());
//			}
//			if (testAnswer == null) {
//				testAnswer = new TestAnswerRadioButton();
//				testScenario.addData(question, testAnswer);
//			} else {
//				if (checkQuestionTestAnswerIntegrity(TestAnswerRadioButton.class, testAnswer)) {
//					if (testAnswer.getValue() != null) {
//						((ComboBox) field).select(testAnswer.getValue());
//					}
//				} else {
//					testAnswer = new TestAnswerRadioButton();
//					testScenario.addData(question, testAnswer);
//				}
//			}
//			break;
//		case MULTI_CHECKBOX:
//			field = new ListSelect(question.getName());
//			((ListSelect) field).setMultiSelect(true);
//			((ListSelect) field).setRows(4);
//			for (TreeObject answer : question.getChildren()) {
//				((ListSelect) field).addItem(answer.getName());
//			}
//			if (testAnswer == null) {
//				testAnswer = new TestAnswerMultiCheckBox();
//				testScenario.addData(question, testAnswer);
//			} else {
//				if (testAnswer.getValue() != null) {
//					if (checkQuestionTestAnswerIntegrity(TestAnswerMultiCheckBox.class, testAnswer)) {
//						Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
//						for (String value : values) {
//							((ListSelect) field).select(value);
//						}
//					} else {
//						testAnswer = new TestAnswerMultiCheckBox();
//						testScenario.addData(question, testAnswer);
//					}
//				}
//			}
//			break;
//		case INPUT:
//			switch (question.getAnswerFormat()) {
//			case TEXT:
//				field = new TextField(question.getName());
//				((TextField) field).setInputPrompt("TEXT");
//				if (testAnswer == null) {
//					testAnswer = new TestAnswerInputText();
//					testScenario.addData(question, testAnswer);
//				} else {
//					if (checkQuestionTestAnswerIntegrity(TestAnswerInputText.class, testAnswer)) {
//						if (testAnswer.getValue() != null) {
//							((TextField) field).setValue(testAnswer.getValue().toString());
//						}
//					} else {
//						testAnswer = new TestAnswerInputText();
//						testScenario.addData(question, testAnswer);
//					}
//				}
//				break;
//			case POSTAL_CODE:
//				field = new TextField(question.getName());
//				((TextField) field).setInputPrompt("0000AA");
//				if (testAnswer == null) {
//					testAnswer = new TestAnswerInputPostalCode();
//					testScenario.addData(question, testAnswer);
//				} else {
//					if (checkQuestionTestAnswerIntegrity(TestAnswerInputPostalCode.class, testAnswer)) {
//						if (testAnswer.getValue() != null) {
//							((TextField) field).setValue(testAnswer.getValue().toString());
//						}
//					} else {
//						testAnswer = new TestAnswerInputPostalCode();
//						testScenario.addData(question, testAnswer);
//					}
//				}
//				break;
//			case NUMBER:
//				field = new TextField(question.getName());
//				((TextField) field).setInputPrompt("1.234");
//				if (testAnswer == null) {
//					testAnswer = new TestAnswerInputNumber();
//					testScenario.addData(question, testAnswer);
//				} else {
//					if (checkQuestionTestAnswerIntegrity(TestAnswerInputNumber.class, testAnswer)) {
//						if (testAnswer.getValue() != null) {
//							((TextField) field).setValue(testAnswer.getValue().toString());
//						}
//					} else {
//						testAnswer = new TestAnswerInputNumber();
//						testScenario.addData(question, testAnswer);
//					}
//				}
//				((TextField) field).addValidator(new RegexpValidator(NUMBER_FIELD_VALIDATOR_REGEX, ServerTranslate
//						.translate(LanguageCodes.INPUT_DATA_FORMAT_INCORRECT_ERROR)));
//				break;
//			case DATE:
//				field = new PopupDateField(question.getName());
//				((PopupDateField) field).setInputPrompt("dd/mm/yy");
//				if (testAnswer == null) {
//					testAnswer = new TestAnswerInputDate();
//					testScenario.addData(question, testAnswer);
//				} else {
//					if (checkQuestionTestAnswerIntegrity(TestAnswerInputDate.class, testAnswer)) {
//						if (testAnswer.getValue() != null) {
//							Timestamp value = ((TestAnswerInputDate) testAnswer).getValue();
//							((PopupDateField) field).setValue(new Date(value.getTime()));
//						}
//					} else {
//						testAnswer = new TestAnswerInputDate();
//						testScenario.addData(question, testAnswer);
//					}
//				}
//				break;
//			}
//		}
//		if (field != null) {
//			((AbstractComponent) field).setImmediate(true);
//			field.addValueChangeListener(new FieldValueChangeListener((AbstractField<?>) field));
//			// Add the value to a map to be consulted later
//			fieldQuestionMap.put(field, new QuestionIndex(question, repeatedGroupIndex));
//		}
//		return field;
//	}
//
//	private class FieldValueChangeListener implements ValueChangeListener {
//		private static final long serialVersionUID = 2277281871213884287L;
//		AbstractField<?> field;
//
//		public FieldValueChangeListener(AbstractField<?> field) {
//			this.field = field;
//		}
//
//		@Override
//		public void valueChange(ValueChangeEvent event) {
//			if (field.isAttached() && field.isEnabled()) {
//				if ((field.getValidators() != null) && (!field.getValidators().isEmpty())) {
//					Collection<Validator> validators = field.getValidators();
//					for (Validator validator : validators) {
//						try {
//							validator.validate(field.getValue());
//							updateTestScenario(field);
//						} catch (InvalidValueException e) {
//							AbcdLogger.warning(this.getClass().getName(), e.toString());
//						}
//					}
//				} else {
//					updateTestScenario(field);
//				}
//			}
//		}
//	}
//
//	private void updateTestScenario(Field field) {
//		try {
//			QuestionIndex questionField = fieldQuestionMap.get(field);
//			if (questionField.getQuestion().getAnswerType().equals(AnswerType.INPUT)
//					&& questionField.getQuestion().getAnswerFormat().equals(AnswerFormat.NUMBER)) {
//				try {
//					Double value = Double.parseDouble(field.getValue().toString());
//					testScenario.getTestAnswerList(questionField.getQuestion())
//							.getTestAnswer(questionField.getQuestionIndex()).setValue(value);
//				} catch (NumberFormatException | NullPointerException e) {
//					AbcdLogger.errorMessage(this.getClass().getName(), e);
//				}
//			} else {
//				testScenario.getTestAnswerList(questionField.getQuestion())
//						.getTestAnswer(questionField.getQuestionIndex()).setValue(field.getValue());
//			}
//		} catch (NotValidAnswerValue | NullPointerException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private boolean checkQuestionTestAnswerIntegrity(Class<?> answerType, TestAnswer answer) {
//		if (answerType.isInstance(answer)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public TestScenario getTestScenario() {
//		return testScenario;
//	}
//
//	public Form getForm() {
//		return form;
//	}
//
//	/**
//	 * Stores the question and its last index<br>
//	 * Needed to know the relation with the test answer list.
//	 * 
//	 */
//	class QuestionIndex {
//		Question question = null;
//		int questionIndex = 0;
//
//		public QuestionIndex(Question question, int questionIndex) {
//			this.question = question;
//			this.questionIndex = questionIndex;
//		}
//
//		public Question getQuestion() {
//			return question;
//		}
//
//		public void setQuestion(Question question) {
//			this.question = question;
//		}
//
//		public int getQuestionIndex() {
//			return questionIndex;
//		}
//
//		public void setQuestionIndex(int questionIndex) {
//			this.questionIndex = questionIndex;
//		}
//	}
}
