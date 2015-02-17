package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

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
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class OperatorsTest extends KidsFormCreator {
	private static final String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE = "ok";
	private final static String NAME_QUESTION = "name";
	private final static String BREAKFAST_QUESTION = "breakfast";
	private final static String FRUIT_AMOUNT_QUESTION = "fruitAmount";
	private final static String VEGETABLES_QUESTION = "vegetables";
	private final static String VEGETABLES_AMOUNT_QUESTION = "vegetablesAmount";
	private static final String CUSTOM_VARIABLE_TO_COMPARE = "customVariableToCompare";
	private static final String CATEGORY_LIFESTYLE = "Lifestyle";
	private static final String TEST_EXPRESSION_NAME = "testExpression";
	private final static String BIRTHDATE_QUESTION = "birthdate";
	private final static String IF_RESULT = "ifResult";
	private final static String BMI = "bmi";
	private final static String MIN = "min";
	private final static String MAX = "max";
	private final static String AVG = "avg";
	private final static String PMT = "pmt";
	private static final Double OR_RESULT_VALUE = 11.;
	private static final String BETWEEN_CUSTOM_VARIABLE = "betweenCustomVariable";

	@Test(groups = { "rules" })
	public void mathematicalOperatorsTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Mathematical expression
			CustomVariable bmiCustomVariable = new CustomVariable(getForm(), BMI, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("bmiCalculation", new ExpressionValueCustomVariable(
					getForm(), bmiCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),

			new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(
					getTreeObject("weight")), new ExpressionOperatorMath(AvailableOperator.DIVISION),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionSymbol(
							AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(
							getTreeObject("height")), new ExpressionOperatorMath(AvailableOperator.DIVISION),
					new ExpressionValueNumber(100.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.PILCROW), new ExpressionOperatorMath(
							AvailableOperator.MULTIPLICATION), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueTreeObjectReference(getTreeObject("height")), new ExpressionOperatorMath(
							AvailableOperator.DIVISION), new ExpressionValueNumber(100.), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorMath(
							AvailableOperator.PLUS), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueNumber(25.), new ExpressionOperatorMath(AvailableOperator.MINUS),
					new ExpressionValueNumber(50.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check bmi
			Double height = Double.parseDouble(((SubmittedQuestion) droolsForm.getSubmittedForm()
					.getChild(ICategory.class, "Algemeen").getChild(IQuestion.class, "height")).getAnswer());
			Double weight = Double.parseDouble(((SubmittedQuestion) droolsForm.getSubmittedForm()
					.getChild(ICategory.class, "Algemeen").getChild(IQuestion.class, "weight")).getAnswer());
			Double bmi = (weight / ((height / 100) * (height / 100))) + (25 - 50);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(BMI), bmi);
		} catch (NumberFormatException | FieldTooLongException | CharacterNotAllowedException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void minOperatorTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// MIN expression
			CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), MIN, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("minExpression", new ExpressionValueCustomVariable(
					getForm(), pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGlobalConstant(
							getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
			Double secondVal = Double.parseDouble(((SubmittedQuestion) droolsForm.getSubmittedForm()
					.getChild(ICategory.class, "Algemeen").getChild(IQuestion.class, "heightFather")).getAnswer());
			Double thirdVal = 1000.0;
			Double minVal = Math.min(Math.min(firstVal, secondVal), thirdVal);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(MIN), minVal);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void maxOperatorTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// MAX expression
			CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), MAX, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("maxExpression", new ExpressionValueCustomVariable(
					getForm(), pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.MAX), new ExpressionValueGlobalConstant(
							getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
			Double secondVal = Double.parseDouble(((SubmittedQuestion) droolsForm.getSubmittedForm()
					.getChild(ICategory.class, "Algemeen").getChild(IQuestion.class, "heightFather")).getAnswer());
			Double thirdVal = 1000.0;
			Double maxVal = Math.max(Math.max(firstVal, secondVal), thirdVal);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(MAX), maxVal);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void avgOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException,
			ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		// AVG expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), AVG, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("avgExpression", new ExpressionValueCustomVariable(getForm(),
				pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.AVG), new ExpressionValueGlobalConstant(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		try {
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
			Double secondVal = (Double.parseDouble(((SubmittedQuestion) droolsForm.getSubmittedForm()
					.getChild(ICategory.class, "Algemeen").getChild(IQuestion.class, "heightFather")).getAnswer()));
			Double thirdVal = 1000.0;
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(AVG),
					(firstVal + secondVal + thirdVal) / 3.0);
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "rules" })
	public void pmtOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		// PMT expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), PMT, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("pmtExpression", new ExpressionValueCustomVariable(getForm(),
				pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.PMT), new ExpressionValueGlobalConstant(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		try {
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(PMT), 21000.0);
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "rules" })
	public void ifOperatorWithoutGenericsTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// If expression
			CustomVariable ifResultCustomVariable = new CustomVariable(getForm(), IF_RESULT, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionValueCustomVariable(
					getForm(), ifResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.IF), new ExpressionValueCustomVariable(getForm(),
							ifResultCustomVariable), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
					new ExpressionValueNumber(56.), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(7.1), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();

			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(IF_RESULT), 1.7);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void ifOperatorWithGenericsTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			CustomVariable categoryCustomVariable = new CustomVariable(getForm(), "catScore",
					CustomVariableType.NUMBER, CustomVariableScope.QUESTION);
			// If expression
			ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionValueGenericCustomVariable(
					GenericTreeObjectType.QUESTION_CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(
					AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.IF),
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
							categoryCustomVariable), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
					new ExpressionValueNumber(56.), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(7.1), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			getForm().getExpressionChains().add(expression);
			getForm().addDiagram(createExpressionsDiagram());
			// Create the rules and launch the engine
			createAndRunDroolsRules();
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void inOperatorQuestionAnswerTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// IN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(
					getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "a")), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(
							BREAKFAST_QUESTION, "b")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void inOperatorQuestionInputNumberTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// IN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(
					getTreeObject("vegetablesAmount")), new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueNumber(3.0), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(4.0), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(5.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Also tests the variable initialization to a default value (in this case
	 * 10)
	 */
	@Test(groups = { "rules" })
	public void inOperatorCustomVariableFormTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create a simple form custom variable
			CustomVariable formNumberCustomVariable = new CustomVariable(getForm(), CUSTOM_VARIABLE_TO_COMPARE,
					CustomVariableType.NUMBER, CustomVariableScope.FORM, "10");
			// IN rule
			createInRule(new ExpressionValueCustomVariable(getForm(), formNumberCustomVariable));
			// Create the diagram
			createDiagram();
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Assert.assertEquals(
					((SubmittedForm) droolsForm.getSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void inOperatorCustomVariableCategoryTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create a simple form custom variable
			createCategoryNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(getCategoryExpressionValueCustomVariable());
			// Create the diagram
			createDiagram();
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Assert.assertEquals(
					((SubmittedCategory) droolsForm.getSubmittedForm().getChild(ICategory.class,
							getCategory().getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void inOperatorCustomVariableGroupTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create a custom variable and the expression containing it
			createGroupNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(getGroupExpressionValueCustomVariable());
			// Create the diagram
			createDiagram();
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Assert.assertEquals(
					((SubmittedGroup) droolsForm.getChild(ICategory.class, CATEGORY_LIFESTYLE).getChild(IGroup.class,
							getGroup().getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void inOperatorCustomVariableQuestionTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create a simple form custom variable
			createQuestionNumberCustomVariableExpression(CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(getQuestionExpressionValueCustomVariable());
			// Create the diagram
			createDiagram();
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules();
			// Check result
			Assert.assertEquals(
					((SubmittedQuestion) droolsForm.getChild(ICategory.class, CATEGORY_LIFESTYLE)
							.getChild(IGroup.class, getGroup().getName())
							.getChild(IQuestion.class, getQuestion().getName()))
							.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public void createInRule(ExpressionValueCustomVariable expressionValueCustomVariable) {
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", expressionValueCustomVariable,
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
	}

	@Test(groups = { "rules" })
	public void betweenOperatorQuestionNumberValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorCustomVariableNumberValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Set a value to check
			createFormNumberCustomVariableExpression(BETWEEN_CUSTOM_VARIABLE);
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					getFormNumberExpressionValueCustomVariable(), new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueNumber(2.), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(11.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorCustomVariableStringValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Set a value to check
			createFormTextCustomVariableExpression(BETWEEN_CUSTOM_VARIABLE);
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					getFormTextExpressionValueCustomVariable(), new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueString("a"), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueString("z"), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorQuestionStringValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenStringExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(NAME_QUESTION)), new ExpressionFunction(
							AvailableFunction.BETWEEN), new ExpressionValueString("A"), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueString("z"), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorQuestionDateValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenDateExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(BIRTHDATE_QUESTION)), new ExpressionFunction(
							AvailableFunction.BETWEEN), new ExpressionValueTimestamp(new Timestamp(0)),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTimestamp(new Timestamp(
							new Date().getTime())), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorQuestionDateYearsValuesTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);

			CustomVariable customVariableDateLower = new CustomVariable(getForm(), "DateLower",
					CustomVariableType.DATE, CustomVariableScope.FORM, "01/01/2010");
			CustomVariable customVariableDateHigher = new CustomVariable(getForm(), "DateHigher",
					CustomVariableType.DATE, CustomVariableScope.FORM, "01/01/1950");

			ExpressionChain condition = new ExpressionChain("betweenDateExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(BIRTHDATE_QUESTION), QuestionDateUnit.YEARS),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueCustomVariable(getForm(),
							customVariableDateLower, QuestionDateUnit.YEARS), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueCustomVariable(getForm(),
							customVariableDateHigher, QuestionDateUnit.YEARS), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void betweenOperatorQuestionInputsTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueTreeObjectReference(
							getTreeObject(FRUIT_AMOUNT_QUESTION)), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(6.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void testAndOperator() throws ExpressionInvalidException, InvalidRuleException, IOException,
			RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			BetweenFunctionInvalidException, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();
		// Expression one
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(
				getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "a")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
						"b")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
						getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(
				getTreeObject(VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(1.0), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three
		ExpressionChain expressionThree = new ExpressionChain("expressionThree",
				new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_QUESTION)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
						getAnswer(VEGETABLES_QUESTION, "d")));

		// Merge with AND
		ExpressionChain conditions = new ExpressionChain(expressionOne, new ExpressionOperatorLogic(
				AvailableOperator.AND), expressionTwo, new ExpressionOperatorLogic(AvailableOperator.AND),
				expressionThree);
		// Creat a a simple action
		CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(getForm(), customVariableResult), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(new Rule("andTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "rules" })
	public void testOrOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		initForm();

		// Expression one (true)
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(
				getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "a")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
						"b")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
						getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two (true)
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(
				getTreeObject(VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(1.0), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three (false)
		ExpressionChain expressionThree = new ExpressionChain("expressionThree",
				new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_QUESTION)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
						getAnswer(VEGETABLES_QUESTION, "d")));

		// Merge with OR
		ExpressionChain conditions = new ExpressionChain(expressionOne, new ExpressionOperatorLogic(
				AvailableOperator.OR), expressionTwo, new ExpressionOperatorLogic(AvailableOperator.OR),
				expressionThree);
		// Create a a simple action
		createFormNumberCustomVariableExpression(CUSTOM_VARIABLE_RESULT);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
				getFormNumberCustomVariable()), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(getForm(), getFormNumberCustomVariable()),
				new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueNumber(1.));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(new Rule("orTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT), OR_RESULT_VALUE);
		}
	}

	@Test(groups = { "rules" })
	public void testNotOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Expression one (false)
			ExpressionChain condition = new ExpressionChain("expressionOne", new ExpressionFunction(
					AvailableFunction.NOT), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueTreeObjectReference(getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(
							AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
							"a")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
							getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "d")), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(new Rule("notTest", condition, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	public void testNotAndCombinationOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(
							AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
							"a")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
							getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "d")), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
							getAnswer(VEGETABLES_QUESTION, "d")));

			// Merge NOT with AND
			ExpressionChain conditions = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionOne, new ExpressionOperatorLogic(
							AvailableOperator.AND), expressionThree,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
							AvailableOperator.AND), expressionTwo);
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(new Rule("notAndTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void andOrCombinationTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(
							AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
							"a")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
							getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "d")), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
							getAnswer(VEGETABLES_QUESTION, "a")));
			// Expression four (true)
			ExpressionChain expressionFour = new ExpressionChain("expressionFour",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.0), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));

			// Merge
			ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionOne, new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
							AvailableOperator.AND), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionThree, new ExpressionOperatorLogic(AvailableOperator.OR), expressionFour,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(new Rule("andOrTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void andOrBracketsCombinationTest() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(BREAKFAST_QUESTION)), new ExpressionFunction(
							AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION,
							"a")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
							getAnswer(BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(BREAKFAST_QUESTION, "d")), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
							getAnswer(VEGETABLES_QUESTION, "a")));
			// Expression four (true)
			ExpressionChain expressionFour = new ExpressionChain("expressionFour",
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.0), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));

			// Merge
			ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionOne, new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
							AvailableOperator.AND), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionThree, new ExpressionOperatorLogic(AvailableOperator.OR), expressionFour,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(new Rule("andOrTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testGreaterThanOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.GREATER_THAN), new ExpressionValueNumber(1.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testGreaterEqualsOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.GREATER_EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testLessThanOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(7.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testLessEqualsOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.LESS_EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testEqualsOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testNotEqualsOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueNumber(3.0));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testNotEqualsDateOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(BIRTHDATE_QUESTION), QuestionDateUnit.DATE),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueSystemDate());
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "rules" })
	public void testNotEqualsDateYearOperator() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(BIRTHDATE_QUESTION), QuestionDateUnit.YEARS),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueNumber(2014.));
			runConditionInRuleAndTestResult(condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private void runConditionInRuleAndTestResult(ExpressionChain condition) {
		try {
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(getForm(), CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(getForm(),
					customVariableResult), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(new Rule("logicComparatorsTest", condition, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private DroolsForm launchRule(Rule rule) {
		// add the rule to the form
		getForm().getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram();
		// Create the drools rules and launch the engine
		return createAndRunDroolsRules();
	}

}
