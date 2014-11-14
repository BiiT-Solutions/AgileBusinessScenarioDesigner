package com.biit.abcd.core.drools.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedGroup;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.abcd.core.drools.utils.DateUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;

public class ExpressionsTest extends KidsFormCreator {
	private final static String YEARS = "years";
	private final static String MONTHS = "months";
	private final static String DAYS = "days";
	private final static String DATE = "date";
	private final static String DATE_FORMAT = "yyyy-MM-dd";
	private CustomVariable yearsCustomVariable = null;
	private CustomVariable monthsCustomVariable = null;
	private CustomVariable daysCustomVariable = null;
	private CustomVariable dateCustomVariable = null;

	@Test(groups = { "rules" })
	public void yearsDateExpressionTest() {
		initFormAndVariables();
		// Assign a date(years) to a custom variable
		ExpressionChain expression = new ExpressionChain("YearsAssignation", new ExpressionValueCustomVariable(
				getForm(), yearsCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.YEARS));

		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(expression);
		if (droolsForm != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				// Kid's birthdate in the parsed form
				Date birthdate = sdf.parse("2007-09-01");
				// Check years
				Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(YEARS),
						DateUtils.returnYearsDistanceFromDate(birthdate));
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	@Test(groups = { "rules" })
	public void monthsDateExpressionTest() {
		initFormAndVariables();
		// Assign a date(months) to a custom variable
		ExpressionChain expression = new ExpressionChain("MonthsAssignation", new ExpressionValueCustomVariable(
				getTreeObject("Algemeen"), monthsCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject("birthdate"),
				QuestionDateUnit.MONTHS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(expression);
		if (droolsForm != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				// Kid's birthdate in the parsed form
				Date birthdate = sdf.parse("2007-09-01");
				// Check months
				Assert.assertEquals(((SubmittedCategory) droolsForm.getSubmittedForm().getCategory("Algemeen"))
						.getVariableValue(MONTHS), DateUtils.returnMonthsDistanceFromDate(birthdate));
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	@Test(groups = { "rules" })
	public void daysDateExpressionTest() {
		initFormAndVariables();
		// Assign a date(days) to a custom variable
		ExpressionChain expression = new ExpressionChain("DaysAssignation", new ExpressionValueCustomVariable(
				getTreeObject("voeding"), daysCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(
						getTreeObject("birthdate"), QuestionDateUnit.DAYS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(expression);
		if (droolsForm != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				// Kid's birthdate in the parsed form
				Date birthdate = sdf.parse("2007-09-01");
				// Check days
				Assert.assertEquals(
						((SubmittedGroup) droolsForm.getSubmittedForm().getCategory("Lifestyle").getGroup("voeding"))
								.getVariableValue(DAYS), DateUtils.returnDaysDistanceFromDate(birthdate));
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	@Test(groups = { "rules" })
	public void testDateExpression() {
		initFormAndVariables();
		// Assign a date(date) to a custom variable
		ExpressionChain expression = new ExpressionChain("DateAssignation", new ExpressionValueCustomVariable(
				getTreeObject("fruit"), dateCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.DATE));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(expression);
		if (droolsForm != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				// Kid's birthdate in the parsed form
				Date birthdate = sdf.parse("2007-09-01");
				// Check date
				Assert.assertEquals(((SubmittedQuestion) droolsForm.getSubmittedForm().getCategory("Lifestyle")
						.getGroup("voeding").getQuestion("fruit")).getVariableValue(DATE), birthdate);
			} catch (Exception e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	private void initFormAndVariables() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create custom variables
			setYearsCustomVariable(new CustomVariable(getForm(), YEARS, CustomVariableType.NUMBER,
					CustomVariableScope.FORM));
			setMonthsCustomVariable(new CustomVariable(getForm(), MONTHS, CustomVariableType.NUMBER,
					CustomVariableScope.CATEGORY));
			setDaysCustomVariable(new CustomVariable(getForm(), DAYS, CustomVariableType.NUMBER,
					CustomVariableScope.GROUP));
			setDateCustomVariable(new CustomVariable(getForm(), DATE, CustomVariableType.DATE,
					CustomVariableScope.QUESTION));
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private DroolsForm launchEngineWithExpression(ExpressionChain expression) {
		try {
			// Add the expression to the form
			getForm().getExpressionChains().add(expression);
			// Create the node rule
			createExpressionNode(expression);
			// Create the diagram
			createDiagram();
			// Create the rules and launch the engine
			return createAndRunDroolsRules();
		} catch (Exception e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	public CustomVariable getYearsCustomVariable() {
		return yearsCustomVariable;
	}

	public void setYearsCustomVariable(CustomVariable yearsCustomVariable) {
		this.yearsCustomVariable = yearsCustomVariable;
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
