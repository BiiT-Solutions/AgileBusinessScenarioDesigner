package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class TableRuleTest extends KidsFormCreator {

	private final static String QUESTION_EQUALS_ANSWER = "works";
	private final static String QUESTION_NOT_EQUALS_ANSWER = "notWorks";
	private final static String GENDER_MALE = "male";
	private final static String GENDER_FEMALE = "female";
	private static final String GENDER_VARIABLE = "genderVariable";
	private static final String CUSTOM_VARIABLE = "customVariable";
	private static final String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE_ONE = "one";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE_TWO = "two";
	private static final Double CUSTOM_VARIABLE_NUMBER_VALUE_ONE = 10.;
	private static final Double CUSTOM_VARIABLE_NUMBER_VALUE_TWO = 15.;

	public TableRuleTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {
		super();
	}

	@Test(groups = { "rules" })
	private void testQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Create the table and diagram
		createQuestionAnswerTableRule();
		// Create the rules and launch the engine
		ISubmittedForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(GENDER_MALE, ((DroolsForm) droolsForm).getSubmittedForm().getVariableValue(GENDER_VARIABLE));
	}

	@Test(groups = { "rules" })
	private void testFormCustomVariableExpressionAnswerTableRule() throws FieldTooLongException,
			NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Create the table and diagram
		createFormCustomVariableAnswerTableRule();
		createDiagram();
		// Create the rules and launch the engine
		ISubmittedForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(CUSTOM_VARIABLE_RESULT_VALUE_ONE, ((DroolsForm) droolsForm).getSubmittedForm()
				.getVariableValue(CUSTOM_VARIABLE_RESULT));
	}

	@Test(groups = { "rules" })
	private void testCategoryCustomVariableExpressionAnswerTableRule() throws FieldTooLongException,
			NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, CategoryDoesNotExistException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Create the table and diagram
		createCategoryCustomVariableAnswerTableRule();
		createDiagram();
		// Create the rules and launch the engine
		ISubmittedForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(CUSTOM_VARIABLE_RESULT_VALUE_ONE, ((DroolsForm) droolsForm).getSubmittedForm()
				.getVariableValue(CUSTOM_VARIABLE_RESULT));
	}

	@Test(groups = { "rules" })
	private void testMultipleColumnsTableRule() throws ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Create the table and diagram
		createCategoryCustomVariableMultiColumnTableRule();
		createDiagram();
		// Create the rules and launch the engine
		ISubmittedForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(CUSTOM_VARIABLE_RESULT_VALUE_ONE, ((DroolsForm) droolsForm).getSubmittedForm()
				.getVariableValue(CUSTOM_VARIABLE_RESULT));
	}

	private void createQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		CustomVariable genderVariable = new CustomVariable(getForm(), GENDER_VARIABLE, CustomVariableType.STRING,
				CustomVariableScope.FORM);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");
		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("gender")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer("gender", "M"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), genderVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(GENDER_MALE)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("gender")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer("gender", "F"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), genderVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(GENDER_FEMALE)));
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		getForm().getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(createSimpleTableDiagram(tableRule));
	}

	private void createFormCustomVariableAnswerTableRule() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create a simple form custom variable
		createFormNumberCustomVariableExpression(CUSTOM_VARIABLE);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");
		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(getForm(), getFormNumberCustomVariable()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(CUSTOM_VARIABLE_NUMBER_VALUE_ONE)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_ONE)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(getForm(), getFormNumberCustomVariable()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(CUSTOM_VARIABLE_NUMBER_VALUE_TWO)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_TWO)));
		tableRule.getRules().add(ruleRow);
		// Add the table rule to the form
		getForm().getTableRules().add(tableRule);
		// Create the node with the table rule
		createTableRuleNode(tableRule);
	}

	private void createCategoryCustomVariableAnswerTableRule() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create a simple form custom variable
		createCategoryNumberCustomVariableExpression(CUSTOM_VARIABLE);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");
		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(getCategory(), getCategoryNumberCustomVariable()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(CUSTOM_VARIABLE_NUMBER_VALUE_ONE)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_ONE)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(getCategory(), getCategoryNumberCustomVariable()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(CUSTOM_VARIABLE_NUMBER_VALUE_TWO)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_TWO)));
		tableRule.getRules().add(ruleRow);
		// Add the table rule to the form
		getForm().getTableRules().add(tableRule);
		// Create the node with the table rule
		createTableRuleNode(tableRule);
	}

	private void createCategoryCustomVariableMultiColumnTableRule() {
		// Create custom variables
		createFormNumberCustomVariableExpression(CUSTOM_VARIABLE);
		createCategoryNumberCustomVariableExpression(CUSTOM_VARIABLE);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		// Create the tableRule
		// We test the combiantion of multiple columns
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");
		TableRuleRow ruleRow = new TableRuleRow();
		// Four columns test
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("gender")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer("gender", "M"))));
		ruleRow.addCondition(new ExpressionValueCustomVariable(getCategory(), getCategoryNumberCustomVariable()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(CUSTOM_VARIABLE_NUMBER_VALUE_ONE)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_ONE)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		// Two columns test
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("gender")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer("gender", "F"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getForm(), customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						CUSTOM_VARIABLE_RESULT_VALUE_TWO)));
		tableRule.getRules().add(ruleRow);
		// Add the table rule to the form
		getForm().getTableRules().add(tableRule);
		// Create the node with the table rule
		createTableRuleNode(tableRule);
	}

	private void createKidsFormSimpleConditionsTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		CustomVariable questionVariable = new CustomVariable(getForm(), "qVar", CustomVariableType.STRING,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");

		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "a"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "b"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		getForm().getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(this.createSimpleTableDiagram(tableRule));
	}

	// Test multiple condition columns and null values
	private void createKidsFormMultipleConditionsTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		CustomVariable questionVariable = new CustomVariable(getForm(), "qVar", CustomVariableType.STRING,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");

		// Question == Answer || null
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "a"))));
		// Null columns
		ruleRow.addCondition(new ExpressionValueTreeObjectReference());
		ruleRow.addCondition(new ExpressionChain());
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		// Question == Answer || Question == Answer
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "b"))));
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("fruit")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "d"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		// Question == Answer --> Action null
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "a"))));
		// Null action
		ruleRow.setAction(new ExpressionChain());
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		getForm().getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(this.createSimpleTableDiagram(tableRule));
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
