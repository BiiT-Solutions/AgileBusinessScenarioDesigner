package com.biit.abcd.core.drools.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.utils.DroolsDateUtils;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

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

	@Test(groups = { "droolsExpressions" })
	public void yearsDateExpressionTest() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		Form form = initFormAndVariables();
		// Assign a date(years) to a custom variable
		ExpressionChain expression = new ExpressionChain("YearsAssignation",
				new ExpressionValueCustomVariable(form, yearsCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"), QuestionDateUnit.YEARS));

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
	public void monthsDateExpressionTest()
			throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, ParseException {
		Form form = initFormAndVariables();
		// Assign a date(months) to a custom variable
		ExpressionChain expression = new ExpressionChain("MonthsAssignation",
				new ExpressionValueCustomVariable(getTreeObject(form, "Algemeen"), monthsCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"), QuestionDateUnit.MONTHS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check months
			Assert.assertEquals(
					((DroolsSubmittedCategory) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class,
							"Algemeen")).getVariableValue(MONTHS),
					DroolsDateUtils.returnMonthsDistanceFromDate(birthdate));
		}
	}

	@Test(groups = { "droolsExpressions" })
	public void daysDateExpressionTest() throws ParseException, FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		Form form = initFormAndVariables();
		// Assign a date(days) to a custom variable
		ExpressionChain expression = new ExpressionChain("DaysAssignation",
				new ExpressionValueCustomVariable(getTreeObject(form, "voeding"), daysCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"), QuestionDateUnit.DAYS));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check days
			Assert.assertEquals(((DroolsSubmittedGroup) droolsForm.getDroolsSubmittedForm()
					.getChild(ISubmittedCategory.class, "Lifestyle").getChild(ISubmittedGroup.class, "voeding"))
							.getVariableValue(DAYS),
					DroolsDateUtils.returnDaysDistanceFromDate(birthdate));
		}
	}

	@Test(groups = { "droolsExpressions" })
	public void testDateExpression() throws ParseException, FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		Form form = initFormAndVariables();
		// Assign a date(date) to a custom variable
		ExpressionChain expression = new ExpressionChain("DateAssignation",
				new ExpressionValueCustomVariable(getTreeObject(form, "fruit"), dateCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "birthdate"), QuestionDateUnit.DATE));
		// Launch the expression
		DroolsForm droolsForm = launchEngineWithExpression(form, expression);
		if (droolsForm != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			// Kid's birthdate in the parsed form
			Date birthdate = sdf.parse("2007-09-01");
			// Check date
			Assert.assertEquals(((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm()
					.getChild(ISubmittedCategory.class, "Lifestyle").getChild(ISubmittedGroup.class, "voeding")
					.getChild(ISubmittedQuestion.class, "fruit")).getVariableValue(DATE), birthdate);
		}
	}

	private Form initFormAndVariables() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		Form form = createForm();
		// Create custom variables
		setYearsCustomVariable(new CustomVariable(form, YEARS, CustomVariableType.NUMBER, CustomVariableScope.FORM));
		setMonthsCustomVariable(
				new CustomVariable(form, MONTHS, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY));
		setDaysCustomVariable(new CustomVariable(form, DAYS, CustomVariableType.NUMBER, CustomVariableScope.GROUP));
		setDateCustomVariable(new CustomVariable(form, DATE, CustomVariableType.DATE, CustomVariableScope.QUESTION));
		return form;
	}

	private DroolsForm launchEngineWithExpression(Form form, ExpressionChain expression) {
		// Add the expression to the form
		form.getExpressionChains().add(expression);
		// Create the node rule
		createExpressionNode(expression);
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
