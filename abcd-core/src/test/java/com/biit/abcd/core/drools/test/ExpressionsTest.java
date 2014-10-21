package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.Group;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.DateUtils;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
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
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
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
	private final static String PMT = "pmt";
	private GlobalVariable globalVariableNumber = null;

	@Test(groups = { "rules" })
	public void testExpressions() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException, InvalidAnswerFormatException, ActionNotImplementedException, ParseException,
			GroupDoesNotExistException, QuestionDoesNotExistException, CharacterNotAllowedException,
			NotValidTypeInVariableData, NotCompatibleTypeException {

		// Restart the form to avoid test cross references
		initForm();
		createGlobalvariables();
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
		Double height = ((Double) ((Question) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("height")).getAnswer());
		Double weight = ((Double) ((Question) droolsForm.getSubmittedForm().getCategory("Algemeen")
				.getQuestion("weight")).getAnswer());
		Double bmi = weight / ((height / 100) * (height / 100));
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(BMI), bmi);

		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(IF_RESULT), 7.1);
		
		Assert.assertEquals(droolsForm.getSubmittedForm().getVariableValue(PMT), 21000.0);
	}

	private void createKidsFormSimpleExpressions() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create custom variables
		// Assign to form
		CustomVariable yearsCustomVariable = new CustomVariable(getForm(), YEARS, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable bmiCustomVariable = new CustomVariable(getForm(), BMI, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable ifResultCustomVariable = new CustomVariable(getForm(), IF_RESULT, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable pmtResultCustomVariable = new CustomVariable(getForm(), PMT, CustomVariableType.NUMBER,
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

		// Mathematical expression
		ExpressionChain expression5 = new ExpressionChain("bmiCalculation", new ExpressionValueCustomVariable(
				getForm(), bmiCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
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
		getForm().getExpressionChains().add(expression5);

		// If expression
		ExpressionChain expression6 = new ExpressionChain("ifExpression", new ExpressionFunction(AvailableFunction.IF),
				new ExpressionValueTreeObjectReference(getTreeObject("weight")), new ExpressionOperatorLogic(
						AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueCustomVariable(getForm(), ifResultCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.1),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueCustomVariable(getForm(),
						ifResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression6);

		// PMT expression
		ExpressionChain expression7 = new ExpressionChain("pmtExpression", new ExpressionValueCustomVariable(getForm(),
				pmtResultCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.PMT), new ExpressionValueGlobalConstant(globalVariableNumber),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
						getTreeObject("heightFather")), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueNumber(1000), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression7);

		getForm().addDiagram(createSimpleDiagram());
	}

	private void createGlobalvariables() throws NotValidTypeInVariableData, FieldTooLongException {
		List<GlobalVariable> globalVarList = new ArrayList<GlobalVariable>();
		Timestamp validFrom = Timestamp.valueOf("2007-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");
		Timestamp validToPast = Timestamp.valueOf("2008-09-23 0:0:0.0");
		Timestamp validToFuture = Timestamp.valueOf("2018-09-23 0:0:0.0");

		// Should get the second value
		globalVariableNumber = new GlobalVariable(AnswerFormat.NUMBER);
		globalVariableNumber.setName("Percentage");
		globalVariableNumber.addVariableData(19.0, validFrom, validToPast);
		globalVariableNumber.addVariableData(21.0, validToPast, null);
		// Should not represent this constant
		GlobalVariable globalVariableText = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText.setName("TestText");
		globalVariableText.addVariableData("Hello", validFromFuture, validToFuture);
		// Should get the value
		GlobalVariable globalVariablePostalCode = new GlobalVariable(AnswerFormat.POSTAL_CODE);
		globalVariablePostalCode.setName("TestPC");
		globalVariablePostalCode.addVariableData("Postal", validFrom, validToFuture);
		// Should enter a valid date as constant
		GlobalVariable globalVariableDate = new GlobalVariable(AnswerFormat.DATE);
		globalVariableDate.setName("TestDate");
		globalVariableDate.addVariableData(new Date(), validFrom, validToFuture);

		globalVarList.add(globalVariableNumber);
		globalVarList.add(globalVariableText);
		globalVarList.add(globalVariablePostalCode);
		globalVarList.add(globalVariableDate);

		setGlobalVariables(globalVarList);
	}

	private Diagram createSimpleDiagram() {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramChild subDiagramExpressionNode = new DiagramChild();
		subDiagramExpressionNode.setDiagram(createExpressionsSubdiagram());
		subDiagramExpressionNode.setJointjsId(IdGenerator.createId());
		subDiagramExpressionNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeTable = new Node(subDiagramExpressionNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startExpression = new DiagramLink(nodeSource, nodeTable);
		startExpression.setJointjsId(IdGenerator.createId());
		startExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionEnd = new DiagramLink(nodeTable, nodeSink);
		expressionEnd.setJointjsId(IdGenerator.createId());
		expressionEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(subDiagramExpressionNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startExpression);
		mainDiagram.addDiagramObject(expressionEnd);

		return mainDiagram;
	}

	private Diagram createExpressionsSubdiagram() {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : getForm().getExpressionChains()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramExpression diagramExpression = new DiagramExpression();
			diagramExpression.setExpression(expressionChain);
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
