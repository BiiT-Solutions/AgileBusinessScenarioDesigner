package com.biit.abcd.core.drools.test;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.persistence.entity.*;
import com.biit.abcd.persistence.entity.diagram.*;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedFormElement;
import com.biit.form.submitted.exceptions.CategoryDoesNotExistException;
import com.biit.form.submitted.exceptions.GroupDoesNotExistException;
import com.biit.form.submitted.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class MultiSelectAnswerTest extends KidsFormCreator {

	private static final String FORM_NAME = "MultiSelectAnswer";
	private static final Integer FORM_VERSION = 1;
	private static final String CATEGORY_NAME = "category1";
	private static final String QUESTION_NAME = "question1";
	private static final String ANSWER1_NAME = "answer1";
	private static final String ANSWER2_NAME = "answer2";
	private static final String ANSWER3_NAME = "answer3";
	private static final String CATEGORY_VARIABLE_NAME = "score";
	private final static Double MULTI_SELECT_VALUE = 3.0;
	protected Form testForm = null;

	@Override
	public Form createForm() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
		Form form = new Form(FORM_NAME);
		form.setVersion(FORM_VERSION);
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question question = new Question(QUESTION_NAME);
		question.setAnswerType(AnswerType.MULTI_CHECKBOX);
		category.addChild(question);

		Answer answer1 = new Answer(ANSWER1_NAME);
		Answer answer2 = new Answer(ANSWER2_NAME);
		Answer answer3 = new Answer(ANSWER3_NAME);
		question.addChild(answer1);
		question.addChild(answer2);
		question.addChild(answer3);

		return form;
	}

	private Form createFormOnlyOneAnswer() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
		Form form = new Form(FORM_NAME);
		form.setVersion(FORM_VERSION);
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question question = new Question(QUESTION_NAME);
		question.setAnswerType(AnswerType.MULTI_CHECKBOX);
		category.addChild(question);

		Answer answer1 = new Answer(ANSWER1_NAME);
		question.addChild(answer1);

		return form;
	}

	@Test(groups = { "multiSelectAnswer" })
	private void testQuestionOneAnswerTableRule() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException,
			RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException, ElementIsReadOnly,
			DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create a simple form
		Form form = createFormOnlyOneAnswer();
		// Create the table and diagram
		createQuestionAnswerTableRule(form);
		// Create the rules and launch the engine
		ISubmittedForm submittedForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertNotNull(submittedForm);
		Assert.assertEquals(1.0, ((ISubmittedFormElement) ((DroolsSubmittedForm) ((DroolsForm) submittedForm).getDroolsSubmittedForm()).getChild(
				ISubmittedCategory.class, CATEGORY_NAME)).getVariableValue(CATEGORY_VARIABLE_NAME));
	}

	@Test(groups = { "multiSelectAnswer" })
	private void testQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException,
			RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException, ElementIsReadOnly,
			DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create a simple form
		Form form = createForm();
		// Create the table and diagram
		createQuestionAnswerTableRule(form);
		// Create the rules and launch the engine
		ISubmittedForm submittedForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertNotNull(submittedForm);

		Assert.assertEquals(MULTI_SELECT_VALUE, ((ISubmittedFormElement) ((DroolsSubmittedForm) ((DroolsForm) submittedForm).getDroolsSubmittedForm())
				.getChild(ISubmittedCategory.class, CATEGORY_NAME)).getVariableValue(CATEGORY_VARIABLE_NAME));
	}

	private void createQuestionAnswerTableRule(Form form) throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {

		CustomVariable categoryVariable = new CustomVariable(form, CATEGORY_VARIABLE_NAME, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY, "0.0");

		ExpressionChain commonAction = new ExpressionChain(new ExpressionValueCustomVariable((Category) getTreeObject(form, CATEGORY_NAME), categoryVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable((Category) getTreeObject(form, CATEGORY_NAME),
						categoryVariable), new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueNumber(1.0));

		// Create the tableRule
		// Only with one conditions column
		TableRule tableRule = new TableRule("TestTable");
		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();

		if (getTreeObject(form, ANSWER1_NAME) != null) {
			ruleRow.addCondition(new ExpressionValueTreeObjectReference(((Question) getTreeObject(form, QUESTION_NAME))));
			ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER1_NAME))));
			ruleRow.setAction(commonAction);
			tableRule.getRules().add(ruleRow);
		}

		if (getTreeObject(form, ANSWER2_NAME) != null) {
			ruleRow = new TableRuleRow();
			ruleRow.addCondition(new ExpressionValueTreeObjectReference(((Question) getTreeObject(form, QUESTION_NAME))));
			ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER2_NAME))));
			ruleRow.setAction(commonAction);
			tableRule.getRules().add(ruleRow);
		}

		if (getTreeObject(form, ANSWER3_NAME) != null) {
			ruleRow = new TableRuleRow();
			ruleRow.addCondition(new ExpressionValueTreeObjectReference(((Question) getTreeObject(form, QUESTION_NAME))));
			ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER3_NAME))));
			ruleRow.setAction(commonAction);
			tableRule.getRules().add(ruleRow);
		}

		// Add the table rule
		form.getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		form.addDiagram(createSimpleTableDiagram(tableRule));
	}

	private Diagram createSimpleTableDiagram(TableRule tableRule) {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramTable diagramTableRuleNode = new DiagramTable();
		diagramTableRuleNode.setTable(tableRule);
		diagramTableRuleNode.setJointjsId(IdGenerator.createId());
		diagramTableRuleNode.setType(DiagramObjectType.TABLE);
		Node nodeTable = new Node(diagramTableRuleNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startTable = new DiagramLink(nodeSource, nodeTable);
		startTable.setJointjsId(IdGenerator.createId());
		startTable.setType(DiagramObjectType.LINK);
		DiagramLink tableEnd = new DiagramLink(nodeTable, nodeSink);
		tableEnd.setJointjsId(IdGenerator.createId());
		tableEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramTableRuleNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startTable);
		mainDiagram.addDiagramObject(tableEnd);

		return mainDiagram;
	}
}
