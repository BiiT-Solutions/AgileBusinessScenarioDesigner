package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.Group;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.DateUtils;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionDateUnit;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class NewExpressionsTest extends TestFormCreator {
	private final static String YEARS = "years";
	private final static String MONTHS = "months";
	private final static String DAYS = "days";
	private final static String DATE = "date";
	private final static String BMI = "bmi";

	@Test(groups = { "rules" })
	public void testExpressions() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException, InvalidAnswerFormatException, ActionNotImplementedException, ParseException,
			GroupDoesNotExistException, QuestionDoesNotExistException {

		// Restart the form to avoid test cross references
		initForm();
		// Create the table and form diagram
		createKidsFormSimpleExpressions();
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
				((Category) droolsForm.getSubmittedForm().getCategory("Algemeen")).getVariableValue(MONTHS),
				DateUtils.returnMonthsDistanceFromDate(birthdate));
		// Check days
		Assert.assertEquals(((Group) droolsForm.getSubmittedForm().getCategory("Lifestyle").getGroup("voeding"))
				.getVariableValue(DAYS), DateUtils.returnDaysDistanceFromDate(birthdate));
		// Check date
		Assert.assertEquals(((Question) droolsForm.getSubmittedForm().getCategory("Lifestyle").getGroup("voeding")
				.getQuestion("fruit")).getVariableValue(DATE), birthdate);
		
		// Check bmi
		Double height = ((Double)((Question) droolsForm.getSubmittedForm().getCategory("Algemeen").getQuestion("height")).getAnswer());
		Double weight = ((Double)((Question) droolsForm.getSubmittedForm().getCategory("Algemeen").getQuestion("weight")).getAnswer());
		Double bmi = weight/((height/100)*(height/100));
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(BMI), bmi);
	}

	private void createKidsFormSimpleExpressions() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create custom variables
		// Assign to form
		CustomVariable yearsCustomVariable = new CustomVariable(getForm(), YEARS, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable bmiCustomVariable = new CustomVariable(getForm(), BMI, CustomVariableType.NUMBER,
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
		ExpressionChain expression = new ExpressionChain("YearsAssignation", new ExpressionValueCustomVariable(
				getForm(), yearsCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.YEARS));
		getForm().getExpressionChain().add(expression);

		// Assign a date(months) to a custom variable
		expression = new ExpressionChain("MonthsAssignation", new ExpressionValueCustomVariable(
				getTreeObject("Algemeen"), monthsCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(getTreeObject("birthdate"),
				QuestionDateUnit.MONTHS));
		getForm().getExpressionChain().add(expression);

		// Assign a date(days) to a custom variable
		expression = new ExpressionChain("DaysAssignation", new ExpressionValueCustomVariable(getTreeObject("voeding"),
				daysCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.DAYS));
		getForm().getExpressionChain().add(expression);

		// Assign a date(date) to a custom variable
		expression = new ExpressionChain("DaysAssignation", new ExpressionValueCustomVariable(getTreeObject("fruit"),
				dateCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("birthdate"), QuestionDateUnit.DATE));
		getForm().getExpressionChain().add(expression);

		// Mathematical expression
		expression = new ExpressionChain("bmiCalculation", new ExpressionValueCustomVariable(getForm(),
				bmiCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("weight")), new ExpressionOperatorMath(
						AvailableOperator.DIVISION), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(
						getTreeObject("height")), new ExpressionOperatorMath(AvailableOperator.DIVISION),
				new ExpressionValueNumber(100.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new ExpressionSymbol(
						AvailableSymbol.LEFT_BRACKET), new ExpressionValueTreeObjectReference(getTreeObject("height")),
				new ExpressionOperatorMath(AvailableOperator.DIVISION), new ExpressionValueNumber(100.),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(createExpressionsSubdiagram());
	}

	private Diagram createExpressionsSubdiagram() {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : getForm().getExpressionChain()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramCalculation diagramExpression = new DiagramCalculation();
			diagramExpression.setFormExpression(expressionChain);
			diagramExpression.setJointjsId(IdGenerator.createId());
			diagramExpression.setType(DiagramObjectType.CALCULATION);
			Node nodeRule = new Node(diagramExpression.getJointjsId());

			DiagramSink diagramSink = new DiagramSink();
			diagramSink.setJointjsId(IdGenerator.createId());
			diagramSink.setType(DiagramObjectType.SINK);
			Node nodeSink = new Node(diagramSink.getJointjsId());

			DiagramLink diagramLinkSourceRule = new DiagramLink(nodeSource, nodeRule);
			diagramLinkSourceRule.setJointjsId(IdGenerator.createId());
			diagramLinkSourceRule.setType(DiagramObjectType.LINK);
			DiagramLink diagramLinkRuleSink = new DiagramLink(nodeRule, nodeSink);
			diagramLinkRuleSink.setJointjsId(IdGenerator.createId());
			diagramLinkRuleSink.setType(DiagramObjectType.LINK);

			subDiagram.addDiagramObject(diagramSource);
			subDiagram.addDiagramObject(diagramExpression);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}
}
