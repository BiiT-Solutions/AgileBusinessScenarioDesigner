package com.biit.abcd.core.drools.test;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.*;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.utils.DroolsDateUtils;
import com.biit.form.exceptions.*;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.dom4j.DocumentException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class ExpressionsTest extends KidsFormCreator {
	private final static String FORM_TEXT = "formText";
	private final static String CATEGORY_TEXT = "categoryText";
	private final static String TEXT_SAMPLE = "This is a string";
	private final static String YEARS = "years";
	private final static String MONTHS = "months";
	private final static String DAYS = "days";
	private final static String DATE = "date";
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private CustomVariable yearsCustomVariable = null;
	private CustomVariable formTextCustomVariable = null;
	private CustomVariable categoryTextCustomVariable = null;
	private CustomVariable monthsCustomVariable = null;
	private CustomVariable daysCustomVariable = null;
	private CustomVariable dateCustomVariable = null;

	@Test(groups = { "droolsExpressions" })
	public void yearsDateExpressionTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		Form form = initFormAndVariables();
		// Assign a date(years) to a custom variable
		ExpressionChain expression = new ExpressionChain("YearsAssignation", new ExpressionValueCustomVariable(form, yearsCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"),
						QuestionDateUnit.YEARS));

		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				// Kid's birthdate in the parsed form
				Date birthdate = sdf.parse("2007-09-01");
				// Check years
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(YEARS),
						DroolsDateUtils.returnYearsDistanceFromDate(birthdate));
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	@Test(groups = { "droolsExpressions" })
	public void monthsDateExpressionTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, ParseException, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		Form form = initFormAndVariables();
		// Assign a date(months) to a custom variable
		ExpressionChain expression = new ExpressionChain("MonthsAssignation", new ExpressionValueCustomVariable(getTreeObject(form, "Algemeen"),
				monthsCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject(form,
				"birthdate"), QuestionDateUnit.MONTHS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check months
			Assert.assertEquals(
					((DroolsSubmittedCategory) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Algemeen")).getVariableValue(MONTHS),
					DroolsDateUtils.returnMonthsDistanceFromDate(birthdate));
		}
	}

	@Test(groups = { "droolsExpressions" })
	public void daysDateExpressionTest() throws ParseException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		Form form = initFormAndVariables();
		// Assign a date(days) to a custom variable
		ExpressionChain expression = new ExpressionChain("DaysAssignation", new ExpressionValueCustomVariable(getTreeObject(form, KidsFormCreator.GROUP_NAME),
				daysCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject(form,
				"birthdate"), QuestionDateUnit.DAYS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check days
			Assert.assertEquals(
					((DroolsSubmittedGroup) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, KidsFormCreator.CATEGORY_LIFESTYLE)
							.getChild(ISubmittedGroup.class, KidsFormCreator.GROUP_NAME)).getVariableValue(DAYS),
					DroolsDateUtils.returnDaysDistanceFromDate(birthdate));
		}
	}

	@Test(groups = { "droolsExpressions" })
	public void testDateExpression() throws ParseException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		Form form = initFormAndVariables();
		// Assign a date(date) to a custom variable
		ExpressionChain expression = new ExpressionChain("DateAssignation",
				new ExpressionValueCustomVariable(getTreeObject(form, "fruit"), dateCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"), QuestionDateUnit.DATE));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check date
			Assert.assertEquals(
					((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Lifestyle")
							.getChild(ISubmittedGroup.class, "voeding").getChild(ISubmittedQuestion.class, "fruit")).getVariableValue(DATE), birthdate);
		}
	}

	@Test(groups = { "droolsExpressions" })
	private void assignTextVariableToOtherVariable() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException, ChildrenNotFoundException {
		// Generate the form
		Form form = initFormAndVariables();
		List<ExpressionChain> expressions = new ArrayList<>();
		ExpressionChain expression1 = new ExpressionChain("AssignStrings1", new ExpressionValueCustomVariable(form.getChild(0), categoryTextCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(TEXT_SAMPLE));
		expressions.add(expression1);
		// Assign variable1 to variable2
		ExpressionChain expression2 = new ExpressionChain("AssignStrings2", new ExpressionValueCustomVariable(form, formTextCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(form.getChild(0), categoryTextCustomVariable));
		expressions.add(expression2);
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpressions(form, expressions);
		if (droolsForm != null) {
			try {
				// Check final string
				Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(FORM_TEXT), TEXT_SAMPLE);
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	private Form initFormAndVariables() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross-references
		Form form = createForm();
		// Create custom variables
		setYearsCustomVariable(new CustomVariable(form, YEARS, CustomVariableType.NUMBER, CustomVariableScope.FORM));
		setFormTextCustomVariable(new CustomVariable(form, FORM_TEXT, CustomVariableType.STRING, CustomVariableScope.FORM));
		setCategoryTextCustomVariable(new CustomVariable(form, CATEGORY_TEXT, CustomVariableType.STRING, CustomVariableScope.CATEGORY));
		setMonthsCustomVariable(new CustomVariable(form, MONTHS, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY));
		setDaysCustomVariable(new CustomVariable(form, DAYS, CustomVariableType.NUMBER, CustomVariableScope.GROUP));
		setDateCustomVariable(new CustomVariable(form, DATE, CustomVariableType.DATE, CustomVariableScope.QUESTION));
		return form;
	}

	private DroolsForm launchEngineWithExpression(Form form, ExpressionChain expression) throws DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Add the expression to the form
		form.getExpressionChains().add(expression);
		// Create the node rule
		createExpressionNode(expression);
		// Create the diagram
		createDiagram(form);
		// Create the rules and launch the engine
		return createAndRunDroolsRules(form);
	}

	private DroolsForm launchEngineWithExpressions(Form form, List<ExpressionChain> expressions) throws DroolsRuleGenerationException, DocumentException,
			IOException, DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException, InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {

		for (ExpressionChain expression : expressions) {
			// Add the expression to the form
			form.getExpressionChains().add(expression);
			// Create the node rule
			createExpressionNode(expression);

		}
		// Create the diagram
		createDiagram(form);
		// Create the rules and launch the engine
		return createAndRunDroolsRules(form);
	}

	public CustomVariable getYearsCustomVariable() {
		return yearsCustomVariable;
	}

	public void setYearsCustomVariable(CustomVariable yearsCustomVariable) {
		this.yearsCustomVariable = yearsCustomVariable;
	}

	public void setFormTextCustomVariable(CustomVariable formTextCustomVariable) {
		this.formTextCustomVariable = formTextCustomVariable;
	}

	public void setCategoryTextCustomVariable(CustomVariable categoryTextCustomVariable) {
		this.categoryTextCustomVariable = categoryTextCustomVariable;
	}

	public CustomVariable getMonthsCustomVariable() {
		return monthsCustomVariable;
	}

	public void setMonthsCustomVariable(CustomVariable monthsCustomVariable) {
		this.monthsCustomVariable = monthsCustomVariable;
	}

	public CustomVariable getDaysCustomVariable() {
		return daysCustomVariable;
	}

	public void setDaysCustomVariable(CustomVariable daysCustomVariable) {
		this.daysCustomVariable = daysCustomVariable;
	}

	public CustomVariable getDateCustomVariable() {
		return dateCustomVariable;
	}

	public void setDateCustomVariable(CustomVariable dateCustomVariable) {
		this.dateCustomVariable = dateCustomVariable;
	}
}
