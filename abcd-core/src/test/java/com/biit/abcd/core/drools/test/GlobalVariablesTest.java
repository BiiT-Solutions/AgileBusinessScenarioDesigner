package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.AnswerFormat;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Tests the correct creation of the global variables <br>
 * It also test the introduction of the variables in the drools engine
 */
public class GlobalVariablesTest extends TestFormCreator {
	private final static String GLOBAL_VALUE = "globalValue";
	private GlobalVariable globalVariableNumber = null;

	@Test(groups = { "rules" })
	public void testGlobVarsInDroolsEngine() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ExpressionInvalidException, RuleInvalidException,
			IOException, RuleNotImplementedException, ActionNotImplementedException, DocumentException,
			CategoryNameWithoutTranslation {

		// Create the form and the variables
		initForm();
		createGlobalvariables();
		createFormWithGlobalVariables();
		// Generate the rules
		FormToDroolsExporter formDrools = new FormToDroolsExporter();
		formDrools.generateDroolRules(getForm(), getGlobalVariables());
		// Create the rules and launch the engine
		DroolsForm submittedForm = createAndRunDroolsRules();
		
		// Check if the operation was correct
		Assert.assertEquals(submittedForm.getSubmittedForm().getVariableValue(GLOBAL_VALUE), 55.4/(21.0/100.0));
	}

	private void createGlobalvariables() throws NotValidTypeInVariableData, FieldTooLongException {
		List<GlobalVariable> globalVarList = new ArrayList<GlobalVariable>();
		Timestamp validFrom = Timestamp.valueOf("2007-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");
		Timestamp validToPast = Timestamp.valueOf("2008-09-23 0:0:0.0");
		Timestamp validToFuture = Timestamp.valueOf("2018-09-23 0:0:0.0");

		// Should get the second value
		globalVariableNumber = new GlobalVariable(AnswerFormat.NUMBER);
		globalVariableNumber.setName("IVA");
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

	private void createFormWithGlobalVariables() {
		CustomVariable auxCustomVariable = new CustomVariable(getForm(), GLOBAL_VALUE, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		// Mathematical expression
		ExpressionChain expression = new ExpressionChain("testCalculation", new ExpressionValueCustomVariable(
				getForm(), auxCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueTreeObjectReference(getTreeObject("weight")), new ExpressionOperatorMath(
						AvailableOperator.DIVISION), new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
				new ExpressionValueGlobalConstant(globalVariableNumber), new ExpressionOperatorMath(
						AvailableOperator.DIVISION), new ExpressionValueNumber(100.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
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
