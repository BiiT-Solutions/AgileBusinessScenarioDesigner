package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
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
import com.biit.drools.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.form.submitted.exceptions.CategoryDoesNotExistException;
import com.biit.form.submitted.exceptions.QuestionDoesNotExistException;
import com.biit.form.submitted.implementation.SubmittedQuestion;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class OperatorsTest extends KidsFormCreator {
	private static final String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE = "ok";
	private final static String QUESTION_NAME = "name";
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

	@Test(groups = { "droolsOperators" })
	public void mathematicalOperatorsTest()
			throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException {
		try {
			// Create a new form
			Form form = createForm();
			// Mathematical expression
			CustomVariable bmiCustomVariable = new CustomVariable(form, BMI, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("bmiCalculation",
					new ExpressionValueCustomVariable(form, bmiCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),

			new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueTreeObjectReference(getTreeObject(form, "weight")),
					new ExpressionOperatorMath(AvailableOperator.DIVISION),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueTreeObjectReference(getTreeObject(form, "height")),
					new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionValueNumber(100.),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.PILCROW),
					new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					new ExpressionValueTreeObjectReference(getTreeObject(form, "height")),
					new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionValueNumber(100.),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionOperatorMath(AvailableOperator.PLUS),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionValueNumber(25.),
					new ExpressionOperatorMath(AvailableOperator.MINUS), new ExpressionValueNumber(50.),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChains().add(expression);
			form.addDiagram(createExpressionsDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check bmi
			Double height = Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm()
					.getChild(ISubmittedCategory.class, "Algemeen").getChild(ISubmittedQuestion.class, "height"))
							.getAnswers().iterator().next());
			Double weight = Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm()
					.getChild(ISubmittedCategory.class, "Algemeen").getChild(ISubmittedQuestion.class, "weight"))
							.getAnswers().iterator().next());
			Double bmi = (weight / ((height / 100) * (height / 100))) + (25 - 50);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(BMI), bmi);
		} catch (NumberFormatException | FieldTooLongException | CharacterNotAllowedException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void minOperatorTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException,
			DocumentException, IOException, DroolsRuleExecutionException {
		// Create a new form
		Form form = createForm();
		// MIN expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, MIN, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("minExpression",
				new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGlobalConstant(getGlobalVariableNumber()),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "heightFather")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = Double.parseDouble(((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm()
				.getChild(ISubmittedCategory.class, "Algemeen").getChild(ISubmittedQuestion.class, "heightFather"))
						.getAnswers().iterator().next());
		Double thirdVal = 1000.0;
		Double minVal = Math.min(Math.min(firstVal, secondVal), thirdVal);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(MIN), minVal);
	}

	@Test(groups = { "droolsOperators" })
	public void maxOperatorTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException,
			DocumentException, IOException, DroolsRuleExecutionException {
		// Create a new form
		Form form = createForm();
		// MAX expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, MAX, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("maxExpression",
				new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MAX),
				new ExpressionValueGlobalConstant(getGlobalVariableNumber()),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "heightFather")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = Double.parseDouble(((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm()
				.getChild(ISubmittedCategory.class, "Algemeen").getChild(ISubmittedQuestion.class, "heightFather"))
						.getAnswers().iterator().next());
		Double thirdVal = 1000.0;
		Double maxVal = Math.max(Math.max(firstVal, secondVal), thirdVal);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(MAX), maxVal);
	}

	@Test(groups = { "droolsOperators" })
	public void avgOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException,
			ElementIsReadOnly {
		// Create a new form
		Form form = createForm();
		// AVG expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, AVG, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("avgExpression",
				new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.AVG),
				new ExpressionValueGlobalConstant(getGlobalVariableNumber()),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "heightFather")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		try {
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
			Double secondVal = (Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm()
					.getChild(ISubmittedCategory.class, "Algemeen").getChild(ISubmittedQuestion.class, "heightFather"))
							.getAnswers().iterator().next()));
			Double thirdVal = 1000.0;
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(AVG),
					(firstVal + secondVal + thirdVal) / 3.0);
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void pmtOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, ElementIsReadOnly {
		// Create a new form
		Form form = createForm();
		// PMT expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, PMT, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("pmtExpression",
				new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.PMT),
				new ExpressionValueGlobalConstant(getGlobalVariableNumber()),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "heightFather")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		try {
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(PMT),
					21000.0);
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void ifOperatorWithoutGenericsTest() {
		try {
			// Create a new form
			Form form = createForm();
			// If expression
			CustomVariable ifResultCustomVariable = new CustomVariable(form, IF_RESULT, CustomVariableType.NUMBER,
					CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("ifExpression",
					new ExpressionValueCustomVariable(form, ifResultCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.IF),
					new ExpressionValueCustomVariable(form, ifResultCustomVariable),
					new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.1),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(1.7),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChains().add(expression);
			form.addDiagram(createExpressionsDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);

			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(IF_RESULT),
					1.7);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void ifOperatorWithGenericsTest() {
		try {
			// Create a new form
			Form form = createForm();
			CustomVariable categoryCustomVariable = new CustomVariable(form, "catScore", CustomVariableType.NUMBER,
					CustomVariableScope.QUESTION);
			// If expression
			ExpressionChain expression = new ExpressionChain("ifExpression",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
							categoryCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.IF),
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
							categoryCustomVariable),
					new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.1),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(1.7),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChains().add(expression);
			form.addDiagram(createExpressionsDiagram(form));
			// Create the rules and launch the engine
			createAndRunDroolsRules(form);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorQuestionAnswerTest() {
		try {
			// Create a new form
			Form form = createForm();
			// IN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("inExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
					new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorQuestionInputNumberTest() {
		try {
			// Create a new form
			Form form = createForm();
			// IN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("inExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
					new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(3.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(4.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(5.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Also tests the variable initialization to a default value (in this case
	 * 10)
	 */
	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableFormTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Create a simple form custom variable
			CustomVariable formNumberCustomVariable = new CustomVariable(form, CUSTOM_VARIABLE_TO_COMPARE,
					CustomVariableType.NUMBER, CustomVariableScope.FORM, "10");
			// IN rule
			createInRule(form, new ExpressionValueCustomVariable(form, formNumberCustomVariable));
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableCategoryTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Create a simple form custom variable
			createCategoryNumberCustomVariableExpression(form, (Category) form.getChild("/" + CATEGORY_NAME),
					CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(form, getCategoryExpressionValueCustomVariable(form));
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(
					((DroolsSubmittedCategory) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class,
							form.getChild("/" + CATEGORY_NAME).getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE),
					10.);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableGroupTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Create a custom variable and the expression containing it
			createGroupNumberCustomVariableExpression(form, (Group) getTreeObject(form, GROUP_NAME),
					CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(form, getGroupExpressionValueCustomVariable((Group) getTreeObject(form, GROUP_NAME)));
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(
					((DroolsSubmittedGroup) droolsForm.getChild(ISubmittedCategory.class, CATEGORY_LIFESTYLE)
							.getChild(ISubmittedGroup.class, ((Group) getTreeObject(form, GROUP_NAME)).getName()))
									.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE),
					10.);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableQuestionTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Create a simple form custom variable
			createQuestionNumberCustomVariableExpression(form, ((Question) getTreeObject(form, QUESTION_NAME)),
					CUSTOM_VARIABLE_TO_COMPARE);
			// IN rule
			createInRule(form,
					getQuestionExpressionValueCustomVariable(((Question) getTreeObject(form, QUESTION_NAME))));
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedQuestion) droolsForm
					.getChild(ISubmittedCategory.class, CATEGORY_LIFESTYLE)
					.getChild(ISubmittedGroup.class, ((Group) getTreeObject(form, GROUP_NAME)).getName())
					.getChild(ISubmittedQuestion.class, ((Question) getTreeObject(form, QUESTION_NAME)).getName()))
							.getVariableValue(CUSTOM_VARIABLE_TO_COMPARE),
					10.);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	public void createInRule(Form form, ExpressionValueCustomVariable expressionValueCustomVariable) {
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", expressionValueCustomVariable,
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(5.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(10.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(15.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);
		// Add the rule to the form
		form.getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionNumberValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorCustomVariableNumberValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Set a value to check
			createFormNumberCustomVariableExpression(form, BETWEEN_CUSTOM_VARIABLE);
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					getFormNumberExpressionValueCustomVariable(form), new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueNumber(2.), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueNumber(11.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorCustomVariableStringValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// Set a value to check
			createFormTextCustomVariableExpression(form, BETWEEN_CUSTOM_VARIABLE);
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenNumberExpression",
					getFormTextExpressionValueCustomVariable(form), new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueString("a"), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueString("z"), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionStringValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenStringExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, QUESTION_NAME)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueString("A"),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueString("z"),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			System.out.println("#####################################");
			System.out.println(condition);
			System.out.println(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionDateValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenDateExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BIRTHDATE_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueTimestamp(new Timestamp(0)),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTimestamp(new Timestamp(new Date().getTime())),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionDateYearsValuesTest() {
		try {
			// Create a new form
			Form form = createForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);

			CustomVariable customVariableDateLower = new CustomVariable(form, "DateLower", CustomVariableType.DATE,
					CustomVariableScope.FORM, "01/01/2010");
			CustomVariable customVariableDateHigher = new CustomVariable(form, "DateHigher", CustomVariableType.DATE,
					CustomVariableScope.FORM, "01/01/1950");

			ExpressionChain condition = new ExpressionChain("betweenDateExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BIRTHDATE_QUESTION),
							QuestionDateUnit.YEARS),
					new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueCustomVariable(form, customVariableDateLower, QuestionDateUnit.YEARS),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueCustomVariable(form, customVariableDateHigher, QuestionDateUnit.YEARS),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionInputsTest() {
		try {
			// Create a new form
			Form form = createForm();
			// BETWEEN rule
			Rule rule = new Rule();
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain condition = new ExpressionChain("betweenExpression",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN),
					new ExpressionValueTreeObjectReference(getTreeObject(form, FRUIT_AMOUNT_QUESTION)),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			rule.setConditions(condition);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			rule.setActions(action);

			// Add the rule to the form
			form.getRules().add(rule);
			// Create the node rule
			createRuleNode(rule);
			// Create the diagram
			createDiagram(form);
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testAndOperator()
			throws ExpressionInvalidException, InvalidRuleException, IOException, RuleNotImplementedException,
			DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, BetweenFunctionInvalidException,
			ElementIsReadOnly, DroolsRuleGenerationException, DroolsRuleExecutionException {
		// Create a new form
		Form form = createForm();
		// Expression one
		ExpressionChain expressionOne = new ExpressionChain("expressionOne",
				new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
				new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three
		ExpressionChain expressionThree = new ExpressionChain("expressionThree",
				new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_QUESTION)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer(form, VEGETABLES_QUESTION, "d")));

		// Merge with AND
		ExpressionChain conditions = new ExpressionChain(expressionOne,
				new ExpressionOperatorLogic(AvailableOperator.AND), expressionTwo,
				new ExpressionOperatorLogic(AvailableOperator.AND), expressionThree);
		// Creat a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("andTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testOrOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException,
			IOException, RuleNotImplementedException, DocumentException, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, ElementIsReadOnly, DroolsRuleGenerationException,
			DroolsRuleExecutionException {
		// Create a new form
		Form form = createForm();

		// Expression one (true)
		ExpressionChain expressionOne = new ExpressionChain("expressionOne",
				new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two (true)
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
				new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three (false)
		ExpressionChain expressionThree = new ExpressionChain("expressionThree",
				new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_QUESTION)),
				new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer(form, VEGETABLES_QUESTION, "d")));

		// Merge with OR
		ExpressionChain conditions = new ExpressionChain(expressionOne,
				new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
				new ExpressionOperatorLogic(AvailableOperator.OR), expressionThree);
		// Create a a simple action
		createFormNumberCustomVariableExpression(form, CUSTOM_VARIABLE_RESULT);
		ExpressionChain action = new ExpressionChain(
				new ExpressionValueCustomVariable(form, getFormNumberCustomVariable()),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(form, getFormNumberCustomVariable()),
				new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueNumber(1.));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("orTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), OR_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotOperator() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException,
			DocumentException, IOException, DroolsRuleExecutionException {
		// Create a new form
		Form form = createForm();
		// Expression one (false)
		ExpressionChain condition = new ExpressionChain("expressionOne", new ExpressionFunction(AvailableFunction.NOT),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

		// Create a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
				CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("notTest", condition, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
					.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotAndCombinationOperator()
			throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException {
		try {
			// Create a new form
			Form form = createForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
					new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS),
					new ExpressionValueTreeObjectReference(getAnswer(form, VEGETABLES_QUESTION, "d")));

			// Merge NOT with AND
			ExpressionChain conditions = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionOne,
					new ExpressionOperatorLogic(AvailableOperator.AND), expressionThree,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionOperatorLogic(AvailableOperator.AND), expressionTwo);
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("notAndTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
						.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void andOrCombinationTest()
			throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException {
		try {
			// Create a new form
			Form form = createForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
					new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS),
					new ExpressionValueTreeObjectReference(getAnswer(form, VEGETABLES_QUESTION, "a")));
			// Expression four (true)
			ExpressionChain expressionFour = new ExpressionChain("expressionFour",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

			// Merge
			ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionOne, new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionOperatorLogic(AvailableOperator.AND),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionThree,
					new ExpressionOperatorLogic(AvailableOperator.OR), expressionFour,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("andOrTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
						.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void andOrBracketsCombinationTest()
			throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException {
		try {
			// Create a new form
			Form form = createForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
					new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
					new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS),
					new ExpressionValueTreeObjectReference(getAnswer(form, VEGETABLES_QUESTION, "a")));
			// Expression four (true)
			ExpressionChain expressionFour = new ExpressionChain("expressionFour",
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0),
					new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(7.0),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

			// Merge
			ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
					expressionOne, new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionOperatorLogic(AvailableOperator.AND),
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionThree,
					new ExpressionOperatorLogic(AvailableOperator.OR), expressionFour,
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("andOrTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
						.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException
				| InvalidAnswerFormatException | NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testGreaterThanOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.GREATER_THAN), new ExpressionValueNumber(1.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testGreaterEqualsOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.GREATER_EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testLessThanOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(7.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testLessEqualsOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.LESS_EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testEqualsOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(5.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotEqualsOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, VEGETABLES_AMOUNT_QUESTION)),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueNumber(3.0));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotEqualsDateOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, BIRTHDATE_QUESTION),
							QuestionDateUnit.DATE),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueSystemDate());
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotEqualsDateYearOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME,
					new ExpressionValueTreeObjectReference(getTreeObject(form, BIRTHDATE_QUESTION),
							QuestionDateUnit.YEARS),
					new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueNumber(2014.));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private void runConditionInRuleAndTestResult(Form form, ExpressionChain condition) {
		try {
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT,
					CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("logicComparatorsTest", condition, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm())
						.getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private DroolsForm launchRule(Form form, Rule rule)
			throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException {
		// add the rule to the form
		form.getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		// Create the diagram
		createDiagram(form);
		// Create the drools rules and launch the engine
		return createAndRunDroolsRules(form);
	}

}
