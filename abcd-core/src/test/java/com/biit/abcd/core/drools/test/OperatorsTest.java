package com.biit.abcd.core.drools.test;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.*;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.*;
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
import org.dom4j.DocumentException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class OperatorsTest extends KidsFormCreator {
	private static final String CUSTOM_VARIABLE_RESULT = "customVariableResult";
	private static final String CUSTOM_VARIABLE_RESULT_VALUE = "ok";
	private final static String BREAKFAST_QUESTION = "breakfast";
	private final static String FRUIT_AMOUNT_QUESTION = "fruitAmount";
	private final static String VEGETABLES_QUESTION = "vegetables";
	private final static String VEGETABLES_AMOUNT_QUESTION = "vegetablesAmount";
	private static final String CUSTOM_VARIABLE_TO_COMPARE = "customVariableToCompare";
	private static final String TEST_EXPRESSION_NAME = "testExpression";
	private final static String BIRTHDATE_QUESTION = "birthdate";
	private final static String IF_RESULT = "ifResult";
	private final static String BMI = "bmi";
	private final static String MIN = "min";
	private final static String MAX = "max";
	private final static String AVG = "avg";
	private final static String PMT = "pmt";
	private final static String CONCAT = "concat";
	private static final Double OR_RESULT_VALUE = 11.;
	private static final String BETWEEN_CUSTOM_VARIABLE = "betweenCustomVariable";

	@Test(groups = { "droolsOperators" })
	public void mathematicalOperatorsTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		try {
			// Create a new form
			Form form = createForm();
			// Mathematical expression
			CustomVariable bmiCustomVariable = new CustomVariable(form, BMI, CustomVariableType.NUMBER, CustomVariableScope.FORM);
			ExpressionChain expression = new ExpressionChain("bmiCalculation", new ExpressionValueCustomVariable(form, bmiCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),

					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject(form, "weight")),
					new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionSymbol(
							AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject(form, "height")), new ExpressionOperatorMath(
							AvailableOperator.DIVISION), new ExpressionValueNumber(100.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.PILCROW), new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new ExpressionSymbol(
							AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject(form, "height")), new ExpressionOperatorMath(
							AvailableOperator.DIVISION), new ExpressionValueNumber(100.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorMath(
							AvailableOperator.PLUS), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionValueNumber(25.),
					new ExpressionOperatorMath(AvailableOperator.MINUS), new ExpressionValueNumber(50.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChains().add(expression);
			form.addDiagram(createExpressionsDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check bmi
			Double height = Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")
					.getChild(ISubmittedQuestion.class, "height")).getAnswers().iterator().next());
			Double weight = Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")
					.getChild(ISubmittedQuestion.class, "weight")).getAnswers().iterator().next());
			Double bmi = (weight / ((height / 100) * (height / 100))) + (25 - 50);
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(BMI), bmi);
		} catch (NumberFormatException | FieldTooLongException | CharacterNotAllowedException | NotValidChildException | InvalidAnswerFormatException
				| NotValidTypeInVariableData | ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void minOperatorTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// MIN expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, MIN, CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("minExpression", new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGlobalVariable(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getTreeObject(form,
						"heightFather")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = Double.parseDouble(((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")
				.getChild(ISubmittedQuestion.class, "heightFather")).getAnswers().iterator().next());
		Double thirdVal = 1000.0;
		Double minVal = Math.min(Math.min(firstVal, secondVal), thirdVal);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(MIN), minVal);
	}

	@Test(groups = { "droolsOperators" })
	public void maxOperatorTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// MAX expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, MAX, CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("maxExpression", new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MAX), new ExpressionValueGlobalVariable(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getTreeObject(form,
						"heightFather")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = Double.parseDouble(((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")
				.getChild(ISubmittedQuestion.class, "heightFather")).getAnswers().iterator().next());
		Double thirdVal = 1000.0;
		Double maxVal = Math.max(Math.max(firstVal, secondVal), thirdVal);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(MAX), maxVal);
	}

	@Test(groups = { "droolsOperators" })
	public void avgOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException, RuleNotImplementedException, DocumentException,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, QuestionDoesNotExistException,
			CategoryDoesNotExistException, BetweenFunctionInvalidException, ElementIsReadOnly, DroolsRuleGenerationException, DroolsRuleExecutionException,
			DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// AVG expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, AVG, CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("avgExpression", new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.AVG), new ExpressionValueGlobalVariable(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getTreeObject(form,
						"heightFather")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = (Double.parseDouble(((SubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")
				.getChild(ISubmittedQuestion.class, "heightFather")).getAnswers().iterator().next()));
		Double thirdVal = 1000.0;
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(AVG), (firstVal + secondVal + thirdVal) / 3.0);
	}

	@Test(groups = { "droolsOperators" })
	public void concatenateStringTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT),
				new ExpressionValueString("Marco"), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueString("Polo"), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check new string
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CONCAT), "MarcoPolo");
	}

	@Test(groups = { "droolsOperators" })
	public void concatenateQuestionValueTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "name")), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "healthExtra")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check new string
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CONCAT), "JanGood!");
	}

	@Test(groups = { "droolsOperators" }, expectedExceptions = { NotCompatibleTypeException.class })
	public void concatenateInvalidQuestionValueTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "name")), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "weight")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		createAndRunDroolsRules(form);
	}

	@Test(groups = { "droolsOperators" })
	public void concatenateVariables() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);
		CustomVariable firstCustomVariable = new CustomVariable(form, "first", CustomVariableType.STRING, CustomVariableScope.FORM, "abc");
		CustomVariable secondCustomVariable = new CustomVariable(form, "second", CustomVariableType.STRING, CustomVariableScope.FORM, "def");

		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT), new ExpressionValueCustomVariable(
						form, firstCustomVariable), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueCustomVariable(form, secondCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check new string
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CONCAT), "abcdef");
	}

	@Test(groups = { "droolsOperators" })
	public void concatenateGlobalVariables() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();

		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);

		Timestamp validToFuture = Timestamp.valueOf("2028-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");

		List<GlobalVariable> variables = new ArrayList<>();

		GlobalVariable globalVariableText1 = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText1.setName("Global1");
		globalVariableText1.addVariableData("Hello", validFromFuture, validToFuture);
		variables.add(globalVariableText1);

		GlobalVariable globalVariableText2 = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText2.setName("Global2");
		globalVariableText2.addVariableData("World!", validFromFuture, validToFuture);
		variables.add(globalVariableText2);

		setGlobalVariables(variables);

		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT), new ExpressionValueGlobalVariable(
						globalVariableText1), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueGlobalVariable(globalVariableText2),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check new string
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CONCAT), "HelloWorld!");
	}

	@Test(groups = { "droolsOperators" })
	public void concatenateMix() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException, FieldTooLongException,
			CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();

		// Concat expression
		CustomVariable stringCustomVariable = new CustomVariable(form, CONCAT, CustomVariableType.STRING, CustomVariableScope.FORM);

		Timestamp validToFuture = Timestamp.valueOf("2028-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");

		List<GlobalVariable> variables = new ArrayList<>();

		// Global variable
		GlobalVariable globalVariableText1 = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText1.setName("Global1");
		globalVariableText1.addVariableData("Hello", validFromFuture, validToFuture);
		variables.add(globalVariableText1);
		setGlobalVariables(variables);

		// Custom variable.
		CustomVariable firstCustomVariable = new CustomVariable(form, "first", CustomVariableType.STRING, CustomVariableScope.FORM, "abc");

		ExpressionChain expression = new ExpressionChain("concatExpression", new ExpressionValueCustomVariable(form, stringCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.CONCAT), new ExpressionValueGlobalVariable(
						globalVariableText1), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueCustomVariable(form, firstCustomVariable),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getTreeObject(form, "name")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueString("Marco"), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check new string
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CONCAT), "HelloabcJanMarco");
	}

	@Test(groups = { "droolsOperators" })
	public void pmtOperatorTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException, RuleNotImplementedException, DocumentException,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException, ElementIsReadOnly {
		// Create a new form
		Form form = createForm();
		// PMT expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(form, PMT, CustomVariableType.NUMBER, CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("pmtExpression", new ExpressionValueCustomVariable(form, pmtResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.PMT), new ExpressionValueGlobalVariable(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getTreeObject(form,
						"heightFather")), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(new Double(1000)), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		try {
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(PMT), 21000.0);
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Test(groups = { "droolsOperators" }, enabled = false)
	public void ifOperatorWithoutGenericsTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// If expression
		CustomVariable ifResultCustomVariable = new CustomVariable(form, IF_RESULT, CustomVariableType.NUMBER, CustomVariableScope.FORM);

		Set<CustomVariable> customVariables = new HashSet<>();
		customVariables.add(ifResultCustomVariable);
		form.setCustomVariables(customVariables);

		final Set<ExpressionChain> expressions = new HashSet<>();
		ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionValueCustomVariable(form,
				ifResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.IF),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, ifResultCustomVariable), new ExpressionOperatorLogic(
				AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
				7.1), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		expressions.add(expression);
		form.setExpressionChains(expressions);

		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);

		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(IF_RESULT), 1.7);
	}

	@Test(groups = { "droolsOperators" }, enabled = false)
	public void ifOperatorWithGenericsTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		CustomVariable categoryCustomVariable = new CustomVariable(form, "catScore", CustomVariableType.NUMBER, CustomVariableScope.QUESTION);
		// If expression
		ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
				categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.IF),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, categoryCustomVariable), new ExpressionOperatorLogic(
						AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						7.1), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChains().add(expression);
		form.addDiagram(createExpressionsDiagram(form));
		// Create the rules and launch the engine
		createAndRunDroolsRules(form);
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorQuestionAnswerTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// IN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorQuestionInputNumberTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// IN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("inExpression", new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueNumber(3.0), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(4.0), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(5.0), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	/**
	 * Also tests the variable initialization to a default value (in this case
	 * 10)
	 * 
	 * @throws ElementIsReadOnly
	 * @throws NotValidTypeInVariableData
	 * @throws InvalidAnswerFormatException
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 * @throws DroolsRuleExecutionException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws DroolsRuleGenerationException
	 * @throws ActionNotImplementedException
	 * @throws InvalidRuleException
	 * @throws PrattParserException
	 * @throws DroolsRuleCreationException
	 * @throws PluginInvocationException
	 * @throws DateComparisonNotPossibleException
	 * @throws BetweenFunctionInvalidException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws ExpressionInvalidException
	 * @throws NotCompatibleTypeException
	 * @throws RuleNotImplementedException
	 * @throws InvalidExpressionException
	 */
	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableFormTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Create a simple form custom variable
		CustomVariable formNumberCustomVariable = new CustomVariable(form, CUSTOM_VARIABLE_TO_COMPARE, CustomVariableType.NUMBER, CustomVariableScope.FORM,
				"10");
		// IN rule
		createInRule(form, new ExpressionValueCustomVariable(form, formNumberCustomVariable));
		// Create the diagram
		createDiagram(form);
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableCategoryTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Create a simple form custom variable
		createCategoryNumberCustomVariableExpression(form, (Category) getTreeObject(form, CATEGORY_NAME), CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		createInRule(form, getCategoryExpressionValueCustomVariable(form));

		// Create the diagram
		createDiagram(form);
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedCategory) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class,
						((Category) getTreeObject(form, CATEGORY_NAME)).getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableGroupTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Create a custom variable and the expression containing it
		createGroupNumberCustomVariableExpression(form, (Group) getTreeObject(form, GROUP_NAME), CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		createInRule(form, getGroupExpressionValueCustomVariable((Group) getTreeObject(form, GROUP_NAME)));
		// Create the diagram
		createDiagram(form);
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedGroup) droolsForm.getChild(ISubmittedCategory.class, CATEGORY_LIFESTYLE).getChild(ISubmittedGroup.class,
						((Group) getTreeObject(form, GROUP_NAME)).getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void inOperatorCustomVariableQuestionTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Create a simple form custom variable
		createQuestionNumberCustomVariableExpression(form, ((Question) getTreeObject(form, QUESTION_NAME)), CUSTOM_VARIABLE_TO_COMPARE);
		// IN rule
		createInRule(form, getQuestionExpressionValueCustomVariable(((Question) getTreeObject(form, QUESTION_NAME))));
		// Create the diagram
		createDiagram(form);
		// Create the drools rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);
		// Check result
		Assert.assertEquals(
				((DroolsSubmittedQuestion) droolsForm.getChild(ISubmittedCategory.class, CATEGORY_NAME).getChild(ISubmittedQuestion.class,
						((Question) getTreeObject(form, QUESTION_NAME)).getName())).getVariableValue(CUSTOM_VARIABLE_TO_COMPARE), 10.);
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	public Rule createInRule(Form form, ExpressionValueCustomVariable expressionValueCustomVariable) {
		Rule rule = new Rule();
		ExpressionChain condition = new ExpressionChain("inCvExpression", expressionValueCustomVariable, new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueNumber(5.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(10.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(15.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
		rule.setActions(action);
		// Add the rule to the form
		form.getRules().add(rule);
		// Create the node rule
		createRuleNode(rule);
		return rule;
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionNumberValuesTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenNumberExpression", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(6.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorCustomVariableNumberValuesTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Set a value to check
		createFormNumberCustomVariableExpression(form, BETWEEN_CUSTOM_VARIABLE);
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenNumberExpression", getFormNumberExpressionValueCustomVariable(form), new ExpressionFunction(
				AvailableFunction.BETWEEN), new ExpressionValueNumber(2.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(11.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorCustomVariableStringValuesTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Set a value to check
		createFormTextCustomVariableExpression(form, BETWEEN_CUSTOM_VARIABLE);
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenNumberExpression", getFormTextExpressionValueCustomVariable(form), new ExpressionFunction(
				AvailableFunction.BETWEEN), new ExpressionValueString("a"), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueString("z"),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionStringValuesTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData,
			ElementIsReadOnly, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenStringExpression", new ExpressionValueTreeObjectReference(getTreeObject(form, QUESTION_NAME)),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueString("A"), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueString("z"), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionDateValuesTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenDateExpression",
				new ExpressionValueTreeObjectReference(getTreeObject(form, BIRTHDATE_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueTimestamp(new Timestamp(0)), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTimestamp(new Timestamp(
						new Date().getTime())), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionDateYearsValuesTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// TODO !!!
		// System.out.println("\n\n\nAQUI ESTÁ:");
		// Create a new form
		Form form = createForm();
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);

		CustomVariable customVariableDateLower = new CustomVariable(form, "DateLower", CustomVariableType.DATE, CustomVariableScope.FORM, "01/01/2010");
		CustomVariable customVariableDateHigher = new CustomVariable(form, "DateHigher", CustomVariableType.DATE, CustomVariableScope.FORM, "01/01/1950");

		ExpressionChain condition = new ExpressionChain("betweenDateExpression", new ExpressionValueTreeObjectReference(
				getTreeObject(form, BIRTHDATE_QUESTION), QuestionDateUnit.YEARS), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueCustomVariable(form, customVariableDateLower, QuestionDateUnit.YEARS), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueCustomVariable(form, customVariableDateHigher, QuestionDateUnit.YEARS), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void betweenOperatorQuestionInputsTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// BETWEEN rule
		Rule rule = new Rule();
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain condition = new ExpressionChain("betweenExpression", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueTreeObjectReference(getTreeObject(form,
				FRUIT_AMOUNT_QUESTION)), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.), new ExpressionSymbol(
				AvailableSymbol.RIGHT_BRACKET));
		rule.setConditions(condition);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
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
		Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), CUSTOM_VARIABLE_RESULT_VALUE);
	}

	@Test(groups = { "droolsOperators" })
	public void testAndOperator() throws ExpressionInvalidException, InvalidRuleException, IOException, RuleNotImplementedException, DocumentException,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData, BetweenFunctionInvalidException, ElementIsReadOnly,
			DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Expression one
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three
		ExpressionChain expressionThree = new ExpressionChain("expressionThree", new ExpressionValueTreeObjectReference(
				getTreeObject(form, VEGETABLES_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
				getAnswer(form, VEGETABLES_QUESTION, "d")));

		// Merge with AND
		ExpressionChain conditions = new ExpressionChain(expressionOne, new ExpressionOperatorLogic(AvailableOperator.AND), expressionTwo,
				new ExpressionOperatorLogic(AvailableOperator.AND), expressionThree);
		// Creat a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("andTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testOrOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException, RuleNotImplementedException, DocumentException,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException, ElementIsReadOnly,
			DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();

		// Expression one (true)
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "b")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two (true)
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three (false)
		ExpressionChain expressionThree = new ExpressionChain("expressionThree", new ExpressionValueTreeObjectReference(
				getTreeObject(form, VEGETABLES_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
				getAnswer(form, VEGETABLES_QUESTION, "d")));

		// Merge with OR
		ExpressionChain conditions = new ExpressionChain(expressionOne, new ExpressionOperatorLogic(AvailableOperator.OR), expressionTwo,
				new ExpressionOperatorLogic(AvailableOperator.OR), expressionThree);
		// Create a a simple action
		createFormNumberCustomVariableExpression(form, CUSTOM_VARIABLE_RESULT);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, getFormNumberCustomVariable()), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(form, getFormNumberCustomVariable()), new ExpressionOperatorMath(
				AvailableOperator.PLUS), new ExpressionValueNumber(1.));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("orTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT), OR_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotOperator() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Expression one (false)
		ExpressionChain condition = new ExpressionChain("expressionOne", new ExpressionFunction(AvailableFunction.NOT), new ExpressionSymbol(
				AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)), new ExpressionFunction(
				AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")), new ExpressionSymbol(
				AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

		// Create a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("notTest", condition, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testNotAndCombinationOperator() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Expression one (false)
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two (true)
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three (false)
		ExpressionChain expressionThree = new ExpressionChain("expressionThree", new ExpressionValueTreeObjectReference(
				getTreeObject(form, VEGETABLES_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
				getAnswer(form, VEGETABLES_QUESTION, "d")));

		// Merge NOT with AND
		ExpressionChain conditions = new ExpressionChain(new ExpressionFunction(AvailableFunction.NOT), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				expressionOne, new ExpressionOperatorLogic(AvailableOperator.AND), expressionThree, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorLogic(AvailableOperator.AND), expressionTwo);
		// Create a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("notAndTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void andOrCombinationTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Create a new form
		Form form = createForm();
		// Expression one (false)
		ExpressionChain expressionOne = new ExpressionChain("expressionOne", new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)),
				new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression two (true)
		ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Expression three (false)
		ExpressionChain expressionThree = new ExpressionChain("expressionThree", new ExpressionValueTreeObjectReference(
				getTreeObject(form, VEGETABLES_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(
				getAnswer(form, VEGETABLES_QUESTION, "a")));
		// Expression four (true)
		ExpressionChain expressionFour = new ExpressionChain("expressionFour", new ExpressionValueTreeObjectReference(getTreeObject(form,
				VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueNumber(7.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

		// Merge
		ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionOne, new ExpressionOperatorLogic(
				AvailableOperator.OR), expressionTwo, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.AND),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionThree, new ExpressionOperatorLogic(AvailableOperator.OR), expressionFour,
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Create a a simple action
		CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
		ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

		// Create the drools rules and launch the engine
		DroolsForm droolsForm = launchRule(form, new Rule("andOrTest", conditions, action));
		if (droolsForm != null) {
			// Check result
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
					CUSTOM_VARIABLE_RESULT_VALUE);
		}
	}

	@Test(groups = { "droolsOperators" })
	public void andOrBracketsCombinationTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		try {
			// Create a new form
			Form form = createForm();
			// Expression one (false)
			ExpressionChain expressionOne = new ExpressionChain("expressionOne",
					new ExpressionValueTreeObjectReference(getTreeObject(form, BREAKFAST_QUESTION)), new ExpressionFunction(AvailableFunction.IN),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "a")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "c")), new ExpressionSymbol(AvailableSymbol.COMMA),
					new ExpressionValueTreeObjectReference(getAnswer(form, BREAKFAST_QUESTION, "d")), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression two (true)
			ExpressionChain expressionTwo = new ExpressionChain("expressionTwo", new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.0), new ExpressionSymbol(
					AvailableSymbol.COMMA), new ExpressionValueNumber(6.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Expression three (false)
			ExpressionChain expressionThree = new ExpressionChain("expressionThree", new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
					VEGETABLES_QUESTION, "a")));
			// Expression four (true)
			ExpressionChain expressionFour = new ExpressionChain("expressionFour", new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.0), new ExpressionSymbol(
					AvailableSymbol.COMMA), new ExpressionValueNumber(7.0), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

			// Merge
			ExpressionChain conditions = new ExpressionChain(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionOne, new ExpressionOperatorLogic(
					AvailableOperator.OR), expressionTwo, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
					AvailableOperator.AND), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), expressionThree, new ExpressionOperatorLogic(
					AvailableOperator.OR), expressionFour, new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
					AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));

			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("andOrTest", conditions, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (CharacterNotAllowedException | FieldTooLongException | NotValidChildException | InvalidAnswerFormatException | NotValidTypeInVariableData
				| ElementIsReadOnly e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsOperators" })
	public void testGreaterThanOperator() {
		try {
			// Create a new form
			Form form = createForm();
			// Create condition
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.GREATER_THAN), new ExpressionValueNumber(1.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.GREATER_EQUALS), new ExpressionValueNumber(5.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(7.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.LESS_EQUALS), new ExpressionValueNumber(5.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(5.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(getTreeObject(form,
					VEGETABLES_AMOUNT_QUESTION)), new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS), new ExpressionValueNumber(3.0));
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(
					getTreeObject(form, BIRTHDATE_QUESTION), QuestionDateUnit.DATE), new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS),
					new ExpressionValueSystemDate());
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
			ExpressionChain condition = new ExpressionChain(TEST_EXPRESSION_NAME, new ExpressionValueTreeObjectReference(
					getTreeObject(form, BIRTHDATE_QUESTION), QuestionDateUnit.YEARS), new ExpressionOperatorLogic(AvailableOperator.NOT_EQUALS),
					new ExpressionValueNumber(2014.));
			runConditionInRuleAndTestResult(form, condition);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private void runConditionInRuleAndTestResult(Form form, ExpressionChain condition) {
		try {
			// Create a a simple action
			CustomVariable customVariableResult = new CustomVariable(form, CUSTOM_VARIABLE_RESULT, CustomVariableType.STRING, CustomVariableScope.FORM);
			ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, customVariableResult), new ExpressionOperatorMath(
					AvailableOperator.ASSIGNATION), new ExpressionValueString(CUSTOM_VARIABLE_RESULT_VALUE));
			// Create the drools rules and launch the engine
			DroolsForm droolsForm = launchRule(form, new Rule("logicComparatorsTest", condition, action));
			if (droolsForm != null) {
				// Check result
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(CUSTOM_VARIABLE_RESULT),
						CUSTOM_VARIABLE_RESULT_VALUE);
			}
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private DroolsForm launchRule(Form form, Rule rule) throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
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
