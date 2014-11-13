package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedGroup;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
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
import com.biit.abcd.core.drools.utils.DateUtils;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class ExpressionsTest extends KidsFormCreator {
	private final static String YEARS = "years";
	private final static String MONTHS = "months";
	private final static String DAYS = "days";
	private final static String DATE = "date";
	private final static String BMI = "bmi";
	private final static String IF_RESULT = "ifResult";
	private final static String MIN = "min";
	private final static String MAX = "max";
	private final static String AVG = "avg";
	private final static String PMT = "pmt";
	private final static String BREAKFAST_QUESTION = "breakfast";

	@Test(groups = { "rules" })
	public void testDateExpressions() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			ParseException, QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException,
			BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Create the table and form diagram
		createDateExpressions();
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Kid's birthdate in the parsed form
		Date birthdate = sdf.parse("2007-09-01");

		// Check years
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(YEARS),
				DateUtils.returnYearsDistanceFromDate(birthdate));
		// Check months
		Assert.assertEquals(
				((SubmittedCategory) droolsForm.getSubmittedForm().getCategory("Algemeen")).getVariableValue(MONTHS),
				DateUtils.returnMonthsDistanceFromDate(birthdate));
		// Check days
		Assert.assertEquals(((SubmittedGroup) droolsForm.getSubmittedForm().getCategory("Lifestyle")
				.getGroup("voeding")).getVariableValue(DAYS), DateUtils.returnDaysDistanceFromDate(birthdate));
		// Check date
		Assert.assertEquals(
				((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Lifestyle").getGroup("voeding")
						.getQuestion("fruit")).getVariableValue(DATE), birthdate);
	}

	@Test(groups = { "rules" })
	public void testComplexMathematicalExpression() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			QuestionDoesNotExistException, CategoryDoesNotExistException, ExpressionInvalidException,
			RuleInvalidException, IOException, RuleNotImplementedException, DocumentException,
			CategoryNameWithoutTranslation, ActionNotImplementedException, NotCompatibleTypeException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// Mathematical expression
		CustomVariable bmiCustomVariable = new CustomVariable(getForm(), BMI, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("bmiCalculation", new ExpressionValueCustomVariable(getForm(),
				bmiCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionSymbol(
				AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject("weight")),
				new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionSymbol(
						AvailableSymbol.LEFT_BRACKET), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueTreeObjectReference(getTreeObject("height")), new ExpressionOperatorMath(
						AvailableOperator.DIVISION), new ExpressionValueNumber(100.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET), new ExpressionSymbol(AvailableSymbol.PILCROW),
				new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new ExpressionSymbol(
						AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject("height")),
				new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionValueNumber(100.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueNumber(25.), new ExpressionOperatorMath(AvailableOperator.MINUS),
				new ExpressionValueNumber(50.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check bmi
		Double height = ((Double) ((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("height")).getAnswer());
		Double weight = ((Double) ((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("weight")).getAnswer());
		Double bmi = (weight / ((height / 100) * (height / 100))) + (25 - 50);
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(BMI), bmi);
	}

	@Test(groups = { "rules" })
	public void testIfOperatorWithoutGenerics() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, NotValidTypeInVariableData,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// If expression
		CustomVariable ifResultCustomVariable = new CustomVariable(getForm(), IF_RESULT, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionFunction(AvailableFunction.IF),
				new ExpressionValueTreeObjectReference(getTreeObject("weight")), new ExpressionOperatorLogic(
						AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueCustomVariable(getForm(), ifResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.1),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueCustomVariable(getForm(),
						ifResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(IF_RESULT), 7.1);
	}

	@Test(groups = { "rules" })
	public void testMinOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, QuestionDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// MIN expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), MIN, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("minExpression", new ExpressionValueCustomVariable(getForm(),
				pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGlobalConstant(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(1000), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = ((Double) ((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("heightFather")).getAnswer());
		Double thirdVal = 1000.0;
		Double minVal = Math.min(Math.min(firstVal, secondVal), thirdVal);
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(MIN), minVal);
	}

	@Test(groups = { "rules" })
	public void testMaxOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, QuestionDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException {
		// Restart the form to avoid test cross references
		initForm();
		// MAX expression
		CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), MAX, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		ExpressionChain expression = new ExpressionChain("maxExpression", new ExpressionValueCustomVariable(getForm(),
				pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MAX), new ExpressionValueGlobalConstant(
						getGlobalVariableNumber()), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(getTreeObject("heightFather")), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(1000), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = ((Double) ((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("heightFather")).getAnswer());
		Double thirdVal = 1000.0;
		Double maxVal = Math.max(Math.max(firstVal, secondVal), thirdVal);
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(MAX), maxVal);
	}

	@Test(groups = { "rules" })
	public void testAvgOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, QuestionDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException {
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
						AvailableSymbol.COMMA), new ExpressionValueNumber(1000), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Double firstVal = (Double) getGlobalVariableValue(getGlobalVariableNumber());
		Double secondVal = ((Double) ((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("heightFather")).getAnswer());
		Double thirdVal = 1000.0;
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(AVG),
				(firstVal + secondVal + thirdVal) / 3.0);
	}

	@Test(groups = { "rules" })
	public void testPmtOperator() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, BetweenFunctionInvalidException {
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
						AvailableSymbol.COMMA), new ExpressionValueNumber(1000), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression);
		getForm().addDiagram(createExpressionsDiagram());
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check result
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(PMT), 21000.0);
	}

	private void createDateExpressions() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create custom variables
		// Assign to form
		CustomVariable yearsCustomVariable = new CustomVariable(getForm(), YEARS, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		// Assign to category
		CustomVariable monthsCustomVariable = new CustomVariable(getForm(), MONTHS, CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		// Assign to group
		CustomVariable daysCustomVariable = new CustomVariable(getForm(), DAYS, CustomVariableType.NUMBER,
				CustomVariableScope.GROUP);
		// Assign to question
		CustomVariable dateCustomVariable = new CustomVariable(getForm(), DATE, CustomVariableType.DATE,
				CustomVariableScope.QUESTION);

		// Assign a date(years) to a custom variable
		ExpressionChain expression1 = new ExpressionChain("YearsAssignation", new ExpressionValueCustomVariable(
				getForm(), yearsCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.YEARS));
		getForm().getExpressionChains().add(expression1);

		// Assign a date(months) to a custom variable
		ExpressionChain expression2 = new ExpressionChain("MonthsAssignation", new ExpressionValueCustomVariable(
				getTreeObject("Algemeen"), monthsCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject("birthdate"),
				QuestionDateUnit.MONTHS));
		getForm().getExpressionChains().add(expression2);

		// Assign a date(days) to a custom variable
		ExpressionChain expression3 = new ExpressionChain("DaysAssignation", new ExpressionValueCustomVariable(
				getTreeObject("voeding"), daysCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(
						getTreeObject("birthdate"), QuestionDateUnit.DAYS));
		getForm().getExpressionChains().add(expression3);

		// Assign a date(date) to a custom variable
		ExpressionChain expression4 = new ExpressionChain("DateAssignation", new ExpressionValueCustomVariable(
				getTreeObject("fruit"), dateCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.DATE));
		getForm().getExpressionChains().add(expression4);

		getForm().addDiagram(createExpressionsDiagram());
	}
}
