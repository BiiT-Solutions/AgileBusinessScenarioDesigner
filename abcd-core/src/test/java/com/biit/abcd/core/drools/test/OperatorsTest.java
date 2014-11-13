package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedGroup;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
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
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class OperatorsTest extends KidsFormCreator {
	private static final String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE = "ok";
	private final static String BREAKFAST_QUESTION = "breakfast";
	private static final String CUSTOM_VARIABLE_TO_COMPARE = "customVariableToCompare";
	private static final String CATEGORY_LIFESTYLE = "Lifestyle";

	@Test(groups = { "rules" })
	public void testInOperatorQuestionAnswer() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		// Restart the form to avoid test cross references
		initForm();
		// IN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(
				getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "a")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
						"b")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
						getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testInOperatorQuestionInputNumber() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		// Restart the form to avoid test cross references
		initForm();
		// IN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount")), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueNumber(3.0), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						4.0), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(5.0),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testInOperatorCustomVariableForm() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			CategoryDoesNotExistException {
		// Restart the form to avoid test cross references
		initForm();
		// Create a simple form custom variable
		createFormNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", getFormExpressionValueCustomVariable(),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(5.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(10.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(15.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((SubmittedForm) droolsForm.getSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testInOperatorCustomVariableCategory() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			CategoryDoesNotExistException {
		// Restart the form to avoid test cross references
		initForm();
		// Create a simple form custom variable
		createCategoryNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", getCategoryExpressionValueCustomVariable(),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(5.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(10.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(15.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(((SubmittedCategory) droolsForm.getCategory(getCategory().getName()))
				.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testInOperatorCustomVariableGroup() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			CategoryDoesNotExistException, GroupDoesNotExistException {
		// Restart the form to avoid test cross references
		initForm();
		// Create a custom variable and the expression containing it
		createGroupNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", getGroupExpressionValueCustomVariable(),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(5.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(10.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(15.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(
				((SubmittedGroup) droolsForm.getCategory(CATEGORY_LIFESTYLE).getGroup(getGroup().getName()))
						.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testInOperatorCustomVariableQuestion() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			CategoryDoesNotExistException, QuestionDoesNotExistException, GroupDoesNotExistException {
		// Restart the form to avoid test cross references
		initForm();
		// Create a simple form custom variable
		createQuestionNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", getQuestionExpressionValueCustomVariable(),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(5.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(10.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(15.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);

		// Add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		// Check result
		Assert.assertEquals(
				((SubmittedQuestion) droolsForm.getCategory(CATEGORY_LIFESTYLE).getGroup(getGroup().getName())
						.getQuestion(getQuestion().getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
				CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "rules" })
	public void testBetweenOperator() {
	}

	@Test(groups = { "rules" })
	public void testAndOperator() {
	}

	@Test(groups = { "rules" })
	public void testOrOperator() {
	}

	@Test(groups = { "rules" })
	public void testNotOperator() {
	}

	@Test(groups = { "rules" })
	public void testGreaterThanOperator() {
	}

	@Test(groups = { "rules" })
	public void testGreaterEqualsOperator() {
	}

	@Test(groups = { "rules" })
	public void testLessThanOperator() {
	}

	@Test(groups = { "rules" })
	public void testLessEqualsOperator() {
	}

	@Test(groups = { "rules" })
	public void testEqualsOperator() {
	}

	@Test(groups = { "rules" })
	public void testNotEqualsOperator() {
	}

}
