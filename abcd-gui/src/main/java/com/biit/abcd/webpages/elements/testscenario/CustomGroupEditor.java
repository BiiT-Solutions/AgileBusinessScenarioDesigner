package com.biit.abcd.webpages.elements.testscenario;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.BaseGroup;
import com.biit.form.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class CustomGroupEditor extends CustomComponent {
	private static final long serialVersionUID = 3099517634702528173L;
	private FormLayout questionsLayout;
	private HorizontalLayout groupButtonsLayout;
	private Button copyRepeatableGroup, removeRepeatableGroup;
	private Map<Field, TestScenarioQuestion> fieldQuestionMap;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private Integer componentIndex;

	public CustomGroupEditor() {
		setCompositionRoot(generateContent());
	}

	public CustomGroupEditor(TreeObject treeObject, int componentIndex) {
		setCompositionRoot(generateContent());
		setContent(treeObject);
	}
	
	public Integer getComponentIndex() {
		return componentIndex;
	}

	public void setComponentIndex(Integer componentIndex) {
		this.componentIndex = componentIndex;
	}

	private Component generateContent() {
		fieldQuestionMap = new HashMap<Field, TestScenarioQuestion>();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		groupButtonsLayout = new HorizontalLayout();
		groupButtonsLayout.setHeight("25px");
		groupButtonsLayout.setWidth("100%");

		questionsLayout = new FormLayout();
		questionsLayout.setWidth("100%");
		questionsLayout.setHeight(null);

		rootLayout.addComponent(groupButtonsLayout);
		rootLayout.addComponent(questionsLayout);

		return rootLayout;
	}

	public void setContent(TreeObject treeObject) {
		createGroupHeader(treeObject);
		createQuestionsFormLayout(treeObject);
	}

	private void createGroupHeader(TreeObject treeObject) {
		Label groupName = new Label(treeObject.getName());
		groupName.setWidth("100%");
		groupButtonsLayout.addComponent(groupName);

		if ((treeObject instanceof Group) && (((Group) treeObject).isRepeatable())) {
			copyRepeatableGroup = new Button("+");
			removeRepeatableGroup = new Button("x");
			groupButtonsLayout.addComponent(copyRepeatableGroup);
			groupButtonsLayout.addComponent(removeRepeatableGroup);
			groupButtonsLayout.setComponentAlignment(copyRepeatableGroup, Alignment.MIDDLE_RIGHT);
			groupButtonsLayout.setComponentAlignment(removeRepeatableGroup, Alignment.MIDDLE_RIGHT);

			if (isLastRepeatableGroup(treeObject)) {
				removeRepeatableGroup.setEnabled(false);
			}
		}
		groupButtonsLayout.setExpandRatio(groupName, 1);

	}

	private void createQuestionsFormLayout(TreeObject treeObject) {
		if (treeObject instanceof BaseGroup) {
			List<TreeObject> questions = ((BaseGroup) treeObject).getChildren(Question.class);
			setQuestionLayoutFields(questions);
		}
	}

	public void addCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (copyRepeatableGroup != null) {
			copyRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (copyRepeatableGroup != null) {
			copyRepeatableGroup.removeClickListener(listener);
		}
	}

	public void addRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (removeRepeatableGroup != null) {
			removeRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (removeRepeatableGroup != null) {
			removeRepeatableGroup.removeClickListener(listener);
		}
	}

	private void setQuestionLayoutFields(List<TreeObject> questions) {
		for (TreeObject question : questions) {
			if (question instanceof TestScenarioQuestion) {
				TestScenarioQuestion testQuestion = (TestScenarioQuestion) question;
				questionsLayout.addComponent(getFormLayoutField(testQuestion));
			}
		}
	}

	private Field<Field> getFormLayoutField(Question question, TestScenarioQuestion testQuestion) {
		TestAnswer testAnswer = null;
		Field field = null;
		switch (question.getAnswerType()) {
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
			switch (question.getAnswerFormat()) {
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

	// public void removeSelected() {
	// if (testScenarioForm.getTreeTestTable() != null) {
	// TreeObject selected =
	// testScenarioForm.getTreeTestTable().getTreeObjectSelected();
	// if ((selected != null) && (selected.getParent() != null) &&
	// !isLastRepeatableGroup(selected)) {
	// try {
	// selected.remove();
	// removeElementFromUI(selected);
	// AbcdLogger.info(this.getClass().getName(), "User '"
	// + UserSessionHandler.getUser().getEmailAddress() + "' has removed a " +
	// selected.getClass()
	// + " from the Form, with 'Name: " + selected.getName() + "'.");
	// } catch (DependencyExistException e) {
	// // Forbid the remove action if exist dependency.
	// MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
	// LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);
	//
	// }
	// }
	// }
	// }
	//
	private boolean isLastRepeatableGroup(TreeObject treeObject) {
		if ((treeObject instanceof Group) && ((Group) treeObject).isRepeatable()) {
			TreeObject parent = treeObject.getParent();
			List<TreeObject> groups = parent.getChildren(Group.class);
			int repeatedGroups = 0;
			for (TreeObject group : groups) {
				// Repeated groups have the same name at the same level
				if (group.getName().equals(treeObject.getName())) {
					repeatedGroups++;
				}
			}
			if (repeatedGroups == 1) {
				// Forbid the remove action when is the last repeatable group
				// MessageManager.showWarning(LanguageCodes.TEST_SCENARIOS_WARNING_NO_REMOVE,
				// LanguageCodes.TEST_SCENARIOS_WARNING_NO_REMOVE_DESCRIPTION);
				return true;
			}
		}
		return false;
	}

	// private void removeElementFromUI(TreeObject element) {
	// for (TreeObject child : element.getChildren()) {
	// removeElementFromUI(child);
	// }
	// testScenarioForm.getTreeTestTable().removeItem(element);
	// }
	//
	// public void copySelected() {
	// if (testScenarioForm.getTreeTestTable() != null) {
	// TreeObject selected =
	// testScenarioForm.getTreeTestTable().getTreeObjectSelected();
	// if ((selected != null) && (selected.getParent() != null) && (selected
	// instanceof Group)
	// && ((Group) selected).isRepeatable()
	// && (testScenarioForm.getTreeTestTable().getRootElement() != null)) {
	// try {
	// TestScenarioForm testForm = (TestScenarioForm)
	// testScenarioForm.getTreeTestTable().getRootElement();
	// TreeObject newGroup = testForm.generateCopy(selected);
	//
	// TreeObject parent = selected.getParent();// Ancestor(Group.class);
	// // int childIndex = parent.getIndex(selected);
	// parent.addChild(newGroup);
	//
	// testScenarioForm.getTreeTestTable().setRootElement(testForm);
	//
	// AbcdLogger.info(this.getClass().getName(), "User '"
	// + UserSessionHandler.getUser().getEmailAddress()
	// + "' has copied the repeatable group with 'Name: " + selected.getName() +
	// "'.");
	// } catch (CharacterNotAllowedException | NotValidStorableObjectException |
	// NotValidChildException e) {
	// // Forbid the remove action if exist dependency.
	// MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
	// LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);
	//
	// }
	// }
	// }
	// }
}
