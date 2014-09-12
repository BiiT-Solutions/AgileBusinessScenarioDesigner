package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;


public class FormVariablesTest {

	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private final static String QUESTION_ANSWER_VAR = "questionAnswer";
	private final static String QUESTION_ANSWER_EQUALS = "questionAnswerEquals";
	private final static String QUESTION_NUMBER_EQUALS = "questionAnswerNumber";
	private final static String BMI = "bmi";



	private ISubmittedForm submittedForm;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		submittedForm = new SubmittedForm(APP, FORM);
//		String xmlFile = readFile("./src/test/resources/kidScreen.xml", StandardCharsets.UTF_8);
//		orbeonImporter.readXml(xmlFile, submittedForm);
//		Assert.assertNotNull(submittedForm);
//		Assert.assertFalse(submittedForm.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
//		String xmlStructure = readFile("./src/test/resources/kidScreen.xhtml", StandardCharsets.UTF_8);
//		OrbeonCategoryTranslator.getInstance().readXml(submittedForm, xmlStructure);
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleLoadAndExecution() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, QuestionDoesNotExistException, RuleNotImplementedException,
			GroupDoesNotExistException, InvalidAnswerFormatException {
//		FormToDroolsExporter formDrools = new FormToDroolsExporter();
//		createFormRelations();
//		// Simple table two conditions one action
//		formDrools.parse(createKidsFormSimpleTable());
//		formDrools.runDroolsRules(submittedForm);
//		SubmittedForm subForm = (SubmittedForm) submittedForm;
//		
//		Assert.assertEquals("QuestionEqualsAnswerWorking", subForm.getVariableValue(QUESTION_ANSWER_EQUALS));
//		System.out.println(subForm.getVariableValue(QUESTION_ANSWER_EQUALS));

	}

	private void createFormRelations() throws NotValidChildException, FieldTooLongException,
			InvalidAnswerFormatException {

//		form.setName("KidsScreen");
//		algemeen.setName("Algemeen");
//		birthdate.setName("birthdate");
//		gender.setName("gender");
//		male.setName("M");
//		female.setName("F");
//		height.setName("height");
//		heightFather.setName("heightFather");
//		heightMother.setName("heightMother");
//		weight.setName("weight");
//		gezondheid.setName("Gezondheid");
//		health.setName("health");
//		yes.setName("Y");
//		no.setName("N");
//		lifestyle.setName("Lifestyle");
//		voeding.setName("voeding");
//		breakfast.setName("breakfast");
//		breakfastA.setName("a");
//		breakfastB.setName("b");
//		breakfastC.setName("c");
//		breakfastD.setName("d");
//		breakfastE.setName("e");
//		fruit.setName("fruit");
//		fruitA.setName("a");
//		fruitB.setName("b");
//		fruitC.setName("c");
//		fruitD.setName("d");
//		fruitE.setName("e");
//		fruitAmount.setName("fruitAmount");
//		vegetables.setName("vegetables");
//		vegetablesA.setName("a");
//		vegetablesB.setName("b");
//		vegetablesC.setName("c");
//		vegetablesD.setName("d");
//		vegetablesE.setName("e");
//		vegetablesAmount.setName("vegetablesAmount");
//		drinks.setName("drinks");
//		drinksA.setName("a");
//		drinksB.setName("b");
//		drinksC.setName("c");
//		drinksD.setName("d");
//
//		form.addChild(algemeen);
//
//		birthdate.setAnswerType(AnswerType.INPUT);
//		birthdate.setAnswerFormat(AnswerFormat.DATE);
//		algemeen.addChild(birthdate);
//
//		birthdate.setAnswerType(AnswerType.RADIO);
//		gender.addChild(male);
//		gender.addChild(female);
//		algemeen.addChild(gender);
//
//		height.setAnswerType(AnswerType.INPUT);
//		height.setAnswerFormat(AnswerFormat.TEXT);
//		algemeen.addChild(height);
//
//		heightFather.setAnswerType(AnswerType.INPUT);
//		heightFather.setAnswerFormat(AnswerFormat.TEXT);
//		algemeen.addChild(heightFather);
//
//		heightMother.setAnswerType(AnswerType.INPUT);
//		heightMother.setAnswerFormat(AnswerFormat.TEXT);
//		algemeen.addChild(heightMother);
//
//		weight.setAnswerType(AnswerType.INPUT);
//		weight.setAnswerFormat(AnswerFormat.TEXT);
//		algemeen.addChild(weight);
//
//		form.addChild(gezondheid);
//
//		health.setAnswerType(AnswerType.RADIO);
//		health.addChild(yes);
//		health.addChild(no);
//		gezondheid.addChild(health);
//
//		form.addChild(lifestyle);
//
//		lifestyle.addChild(voeding);
//
//		breakfast.setAnswerType(AnswerType.RADIO);
//		breakfast.addChild(breakfastA);
//		breakfast.addChild(breakfastB);
//		breakfast.addChild(breakfastC);
//		breakfast.addChild(breakfastD);
//		breakfast.addChild(breakfastE);
//		voeding.addChild(breakfast);
//
//		fruit.setAnswerType(AnswerType.RADIO);
//		fruit.addChild(fruitA);
//		fruit.addChild(fruitB);
//		fruit.addChild(fruitC);
//		fruit.addChild(fruitD);
//		fruit.addChild(fruitE);
//		voeding.addChild(fruit);
//
//		fruitAmount.setAnswerType(AnswerType.INPUT);
//		fruitAmount.setAnswerFormat(AnswerFormat.TEXT);
//		voeding.addChild(fruitAmount);
//
//		vegetables.setAnswerType(AnswerType.RADIO);
//		vegetables.addChild(vegetablesA);
//		vegetables.addChild(vegetablesB);
//		vegetables.addChild(vegetablesC);
//		vegetables.addChild(vegetablesD);
//		vegetables.addChild(vegetablesE);
//		voeding.addChild(vegetables);
//
//		voeding.addChild(vegetablesAmount);
//
//		drinks.setAnswerType(AnswerType.RADIO);
//		drinks.addChild(drinksA);
//		drinks.addChild(drinksB);
//		drinks.addChild(drinksC);
//		drinks.addChild(drinksD);
//		voeding.addChild(drinks);
	}

//	private Form createKidsFormSimpleTable() throws FieldTooLongException, NotValidChildException,
//			InvalidAnswerFormatException {

//		// Create custom variables
//		CustomVariable formQuestionAnswer = new CustomVariable(form, QUESTION_ANSWER_EQUALS, CustomVariableType.STRING,
//				CustomVariableScope.FORM);
//		CustomVariable formQuestionScore = new CustomVariable(form, QUESTION_NUMBER_EQUALS, CustomVariableType.STRING,
//				CustomVariableScope.FORM);
//		CustomVariable questionScore = new CustomVariable(form, QUESTION_ANSWER_VAR, CustomVariableType.NUMBER,
//				CustomVariableScope.QUESTION);
//
//		// Create the tableRule
//		// Only with one conditions colum
//		TableRule tableRule = new TableRule("TestTable");
//
//		// Question == Answer
//		TableRuleRow ruleRow = new TableRuleRow();
//		ruleRow.addCondition(new ExpressionValueTreeObjectReference(breakfast));
//		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
//				new ExpressionValueTreeObjectReference(breakfastB)));
//		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, formQuestionAnswer),
//				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("QuestionEqualsAnswerWorking")));
//		tableRule.getRules().add(ruleRow);
//		
//		// Assign value to a question
//		ruleRow = new TableRuleRow();
//		ruleRow.addCondition(new ExpressionValueTreeObjectReference(breakfast));
//		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
//				new ExpressionValueTreeObjectReference(breakfastB)));
//		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(breakfast, questionScore),
//				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(5.)));
//		tableRule.getRules().add(ruleRow);
//		
//		// Question.score == NumberValue
//		ruleRow = new TableRuleRow();
//		ruleRow.addCondition(new ExpressionValueCustomVariable(breakfast, questionScore));
//		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
//				new ExpressionValueTreeObjectReference(breakfastB)));
//		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, formQuestionScore),
//				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("QuestionEqualsScore")));
//		tableRule.getRules().add(ruleRow);
//
//		// Creation of a simple diagram to load the table rule
//		form.addDiagram(this.createSimpleDiagram(tableRule));
//
//		return form;
//	}

	private Diagram createSimpleDiagram(TableRule tableRule) {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramTable diagramTableRuleNode = new DiagramTable();
		diagramTableRuleNode.setTable(tableRule);
		diagramTableRuleNode.setJointjsId(IdGenerator.createId());
		diagramTableRuleNode.setType(DiagramObjectType.TABLE);
		Node nodeTable = new Node(diagramTableRuleNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startTable = new DiagramLink(nodeSource, nodeTable);
		startTable.setJointjsId(IdGenerator.createId());
		startTable.setType(DiagramObjectType.LINK);
		DiagramLink tableEnd = new DiagramLink(nodeTable, nodeSink);
		tableEnd.setJointjsId(IdGenerator.createId());
		tableEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramTableRuleNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startTable);
		mainDiagram.addDiagramObject(tableEnd);

		return mainDiagram;
	}
}