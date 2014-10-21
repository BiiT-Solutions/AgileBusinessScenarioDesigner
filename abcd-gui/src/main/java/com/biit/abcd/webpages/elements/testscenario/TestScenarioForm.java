package com.biit.abcd.webpages.elements.testscenario;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputPostalCode;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputText;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerRadioButton;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.abcd.persistence.utils.IdGenerator;
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
	private Map<Field, Question> fieldQuestionMap = null;
	private static final String NUMBER_FIELD_VALIDATOR_REGEX = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private Accordion backgroundAccordion = null;

	public TestScenarioForm() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) {
		createContent(form, testScenario);
	}

	/**
	 * Creates the content for the complete test scenario definition
	 * 
	 * @param form
	 * @param testScenario
	 */
	private void createContent(Form form, TestScenario testScenario) {
		if ((form != null) && (testScenario != null)) {
			this.form = form;
			this.testScenario = testScenario;
			fieldQuestionMap = new HashMap<Field, Question>();
			backgroundAccordion = new Accordion();
			setCaption(form.getName());
			backgroundAccordion.setSizeFull();
			backgroundAccordion.setStyleName(Runo.ACCORDION_LIGHT);
			// Get the categories
			List<TreeObject> categories = form.getChildren();
			if (categories != null) {
				for (TreeObject category : categories) {
					backgroundAccordion.addTab(createCategoryContent((Category) category),
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

	/**
	 * Used in test scenario of categories
	 * 
	 * @param testScenario
	 * @param category
	 * @throws NotValidChildException
	 */
	public void createCategoryContent(TestScenario testScenario, Category category) throws NotValidChildException {
		Set<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(category);
		Set<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm().getCustomVariables();
		form = new Form();
		// We have to generate a copy to avoid structure issues
		// TODO Generate a category copy
		form.addChild(category);
		form.setCustomVariables(customVariables);
		form.addDiagram(createRulesDiagram(assignedRules));

		this.testScenario = testScenario;
		fieldQuestionMap = new HashMap<Field, Question>();
		setContent(createCategoryContent(category));
		addStyleName(Runo.PANEL_LIGHT);
		setHeight(100.0f, Unit.PERCENTAGE);
	}

	private Component createCategoryContent(Category category) {
		// Create the category panels
		VerticalLayout categoryBackground = new VerticalLayout();
		// Put category children variables
		List<TreeObject> categoryChildren = category.getChildren();
		if (categoryChildren != null) {
			FormLayout categoryFormLayout = new FormLayout();

			for (TreeObject categoryChild : categoryChildren) {
				if (categoryChild instanceof Group) {
					// Create the group panels
					Panel groupPanel = new Panel(categoryChild.getName());
					VerticalLayout groupBackground = new VerticalLayout();
					List<TreeObject> groupChildren = categoryChild.getChildren();
					if (groupChildren != null) {
						FormLayout groupFormLayout = new FormLayout();

						for (TreeObject groupChild : groupChildren) {
							if (groupChild instanceof Group) {
								createNestedGroupVariables((Group) groupChild, categoryBackground);

							} else if (groupChild instanceof Question) {
								groupFormLayout.addComponent(getFormLayoutField((Question) groupChild));
							}
						}
						// Add the form to the category background
						groupBackground.addComponent(groupFormLayout);
						groupBackground.setMargin(true);
						groupPanel.setContent(groupBackground);
						categoryBackground.addComponent(groupPanel);
					}
				} else if (categoryChild instanceof Question) {
					categoryFormLayout.addComponent(getFormLayoutField((Question) categoryChild));
				}
			}
			// Add the form to the category background
			categoryBackground.addComponent(categoryFormLayout);
			categoryBackground.setMargin(true);
//			categoryPanel.setContent(categoryBackground);
		}
		return categoryBackground;
	}

	private void createNestedGroupVariables(TreeObject group, VerticalLayout parentLayout) {
		// Create the group panels
		Panel groupPanel = new Panel(group.getName());
		VerticalLayout groupBackground = new VerticalLayout();
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {
			FormLayout groupFormLayout = new FormLayout();

			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof Group) {
					createNestedGroupVariables((Group) groupChild, groupBackground);

				} else if (groupChild instanceof Question) {
					groupFormLayout.addComponent(getFormLayoutField((Question) groupChild));
				}
			}
			// Add the form to the category background
			groupBackground.addComponent(groupFormLayout);
			groupBackground.setMargin(true);
			groupPanel.setContent(groupBackground);
			parentLayout.addComponent(groupPanel);
		}
	}

	private Field<Field> getFormLayoutField(Question question) {
		TestAnswer testAnswer = null;
		if (testScenario.containsQuestion(question)) {
			testAnswer = testScenario.getTestAnswer(question);
		}

		Field field = null;
		switch (question.getAnswerType()) {
		case RADIO:
			field = new ComboBox(question.getName());
			for (TreeObject answer : question.getChildren()) {
				((ComboBox) field).addItem(answer.getName());
			}
			if (testAnswer == null) {
				testAnswer = new TestAnswerRadioButton();
				testScenario.addData(question, testAnswer);
			} else {
				if (checkQuestionTestAnswerIntegrity(TestAnswerRadioButton.class, testAnswer)) {
					if (testAnswer.getValue() != null) {
						((ComboBox) field).select(testAnswer.getValue());
					}
				} else {
					testAnswer = new TestAnswerRadioButton();
					testScenario.addData(question, testAnswer);
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
				testScenario.addData(question, testAnswer);
			} else {
				if (testAnswer.getValue() != null) {
					if (checkQuestionTestAnswerIntegrity(TestAnswerMultiCheckBox.class, testAnswer)) {
						Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
						for (String value : values) {
							((ListSelect) field).select(value);
						}
					} else {
						testAnswer = new TestAnswerMultiCheckBox();
						testScenario.addData(question, testAnswer);
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
					testScenario.addData(question, testAnswer);
				} else {
					if (checkQuestionTestAnswerIntegrity(TestAnswerInputText.class, testAnswer)) {
						if (testAnswer.getValue() != null) {
							((TextField) field).setValue(testAnswer.getValue().toString());
						}
					} else {
						testAnswer = new TestAnswerInputText();
						testScenario.addData(question, testAnswer);
					}
				}
				break;
			case POSTAL_CODE:
				field = new TextField(question.getName());
				((TextField) field).setInputPrompt("0000AA");
				if (testAnswer == null) {
					testAnswer = new TestAnswerInputPostalCode();
					testScenario.addData(question, testAnswer);
				} else {
					if (checkQuestionTestAnswerIntegrity(TestAnswerInputPostalCode.class, testAnswer)) {
						if (testAnswer.getValue() != null) {
							((TextField) field).setValue(testAnswer.getValue().toString());
						}
					} else {
						testAnswer = new TestAnswerInputPostalCode();
						testScenario.addData(question, testAnswer);
					}
				}
				break;
			case NUMBER:
				field = new TextField(question.getName());
				((TextField) field).setInputPrompt("1.234");
				if (testAnswer == null) {
					testAnswer = new TestAnswerInputNumber();
					testScenario.addData(question, testAnswer);
				} else {
					if (checkQuestionTestAnswerIntegrity(TestAnswerInputNumber.class, testAnswer)) {
						if (testAnswer.getValue() != null) {
							((TextField) field).setValue(testAnswer.getValue().toString());
						}
					} else {
						testAnswer = new TestAnswerInputNumber();
						testScenario.addData(question, testAnswer);
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
					testScenario.addData(question, testAnswer);
				} else {
					if (checkQuestionTestAnswerIntegrity(TestAnswerInputDate.class, testAnswer)) {
						if (testAnswer.getValue() != null) {
							Timestamp value = ((TestAnswerInputDate) testAnswer).getValue();
							((PopupDateField) field).setValue(new Date(value.getTime()));
						}
					} else {
						testAnswer = new TestAnswerInputDate();
						testScenario.addData(question, testAnswer);
					}
				}
				break;
			}
		}
		if (field != null) {
			((AbstractComponent) field).setImmediate(true);
			field.addValueChangeListener(new FieldValueChangeListener((AbstractField<?>) field));
			// Add the value to a map to be consulted later
			fieldQuestionMap.put(field, question);
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
			Question question = fieldQuestionMap.get(field);
			if (question.getAnswerType().equals(AnswerType.INPUT)
					&& question.getAnswerFormat().equals(AnswerFormat.NUMBER)) {
				try {
					Double value = Double.parseDouble(field.getValue().toString());
					testScenario.getTestAnswer(question).setValue(value);
				} catch (NumberFormatException | NullPointerException e) {
				}
			} else {
				testScenario.getTestAnswer(question).setValue(field.getValue());
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

	private Diagram createRulesDiagram(Set<Rule> rules) {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramChild subDiagramExpressionNode = new DiagramChild();
		subDiagramExpressionNode.setDiagram(createRulesSubdiagram(rules));
		subDiagramExpressionNode.setJointjsId(IdGenerator.createId());
		subDiagramExpressionNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeTable = new Node(subDiagramExpressionNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startExpression = new DiagramLink(nodeSource, nodeTable);
		startExpression.setJointjsId(IdGenerator.createId());
		startExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionEnd = new DiagramLink(nodeTable, nodeSink);
		expressionEnd.setJointjsId(IdGenerator.createId());
		expressionEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(subDiagramExpressionNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startExpression);
		mainDiagram.addDiagramObject(expressionEnd);

		return mainDiagram;
	}

	private Diagram createRulesSubdiagram(Set<Rule> rules) {
		Diagram subDiagram = new Diagram("rulesDiagram");
		for (Rule rule : rules) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramRule diagramRule = new DiagramRule();
			diagramRule.setRule(rule);
			diagramRule.setJointjsId(IdGenerator.createId());
			diagramRule.setType(DiagramObjectType.RULE);
			Node nodeRule = new Node(diagramRule.getJointjsId());

			DiagramSink diagramSink = new DiagramSink();
			diagramSink.setJointjsId(IdGenerator.createId());
			diagramSink.setType(DiagramObjectType.SINK);
			Node nodeSink = new Node(diagramSink.getJointjsId());

			DiagramLink diagramLinkSourceRule = new DiagramLink(nodeSource, nodeRule);
			diagramLinkSourceRule.setJointjsId(IdGenerator.createId());
			diagramLinkSourceRule.setType(DiagramObjectType.LINK);
			DiagramLink diagramLinkRuleSink = new DiagramLink(nodeRule, nodeSink);
			diagramLinkRuleSink.setJointjsId(IdGenerator.createId());
			diagramLinkRuleSink.setType(DiagramObjectType.LINK);

			subDiagram.addDiagramObject(diagramSource);
			subDiagram.addDiagramObject(diagramRule);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}
}
