package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionUnit;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.OrbeonCategoryTranslator;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class TableRuleTest {

	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private final static String BMI_TEXT_VARIABLE_NAME = "BmiClassification";
	private final static String BMI_VARIABLE_NAME = "BMI";

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		form = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/kidScreen.xml", StandardCharsets.UTF_8);
		orbeonImporter.readXml(xmlFile, form);
		Assert.assertNotNull(form);
		Assert.assertFalse(form.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		String xmlStructure = readFile("./src/test/resources/kidScreen.xhtml", StandardCharsets.UTF_8);
		OrbeonCategoryTranslator.getInstance().readXml(form, xmlStructure);
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleLoadAndExecution() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, QuestionDoesNotExistException, RuleNotImplementedException,
			GroupDoesNotExistException, InvalidAnswerFormatException {
		FormToDroolsExporter formDrools = new FormToDroolsExporter();

		// Simple table two conditions one action
		formDrools.parse(createKidsFormSimpleTable());
		formDrools.runDroolsRules(form);
		SubmittedForm subForm = (SubmittedForm) form;
		Assert.assertEquals("overweight", subForm.getVariableValue(BMI_VARIABLE_NAME));

	}

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleSeveralConditionsLoadAndExecution() throws ExpressionInvalidException,
			NotValidChildException, NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException,
			FieldTooLongException, IOException, CategoryDoesNotExistException, QuestionDoesNotExistException,
			RuleNotImplementedException, GroupDoesNotExistException, InvalidAnswerFormatException {
		
		FormToDroolsExporter formDrools = new FormToDroolsExporter();
		// Simple table two conditions one action
		formDrools.parse(createKidsFormAdvancedTable());
		formDrools.runDroolsRules(form);
		SubmittedForm subForm = (SubmittedForm) form;
		Assert.assertEquals("overweight", subForm.getVariableValue(BMI_VARIABLE_NAME));
		// FormToDroolsExporter formDrools = new FormToDroolsExporter();
		// Form vaadinForm = this.createRuleTestSeveralConditionsForm();
		// formDrools.parse(vaadinForm);
		// formDrools.runDroolsRules(this.form);
		//
		// ICategory testCat1 = this.form.getCategory("Sociaal netwerk");
		// IGroup testGroup1 = testCat1.getGroup("Sociaal");
		// com.biit.abcd.core.drools.facts.inputform.Question testQuestion1 =
		// (com.biit.abcd.core.drools.facts.inputform.Question) testGroup1
		// .getQuestion("Familie");
		//
		// Assert.assertEquals(5.0,
		// testQuestion1.getNumberVariableValue("qScore"));
	}

	// @Test(groups = { "rules" }, dependsOnMethods = {
	// "translateFormCategories" })
	// public void testTableRuleSpecialConditions() throws
	// ExpressionInvalidException, NotValidChildException,
	// NotValidOperatorInExpression, ChildrenNotFoundException,
	// RuleInvalidException, FieldTooLongException,
	// IOException, CategoryDoesNotExistException,
	// QuestionDoesNotExistException, InvalidAnswerFormatException,
	// RuleNotImplementedException, GroupDoesNotExistException {
	// FormToDroolsExporter formDrools = new FormToDroolsExporter();
	// Form vaadinForm = this.createRuleTestSpecialConditionsForm();
	// formDrools.parse(vaadinForm);
	// formDrools.runDroolsRules(this.form);
	//
	// ICategory testCat1 = this.form.getCategory("Sociaal netwerk");
	// IGroup testGroup1 = testCat1.getGroup("Sociaal");
	// com.biit.abcd.core.drools.facts.inputform.Question testQuestion1 =
	// (com.biit.abcd.core.drools.facts.inputform.Question) testGroup1
	// .getQuestion("Familie");
	// com.biit.abcd.core.drools.facts.inputform.Category testCat2 =
	// (com.biit.abcd.core.drools.facts.inputform.Category) this.form
	// .getCategory("Persoonsgegevens");
	// Assert.assertEquals(5.0, testQuestion1.getNumberVariableValue("qScore"));
	// Assert.assertEquals(36.0, testCat2.getNumberVariableValue("cScore"));
	// }

//	/**
//	 * Create the form structure. Creates to simple assignation rules in the
//	 * table rule and one expression with max func Form used to create the
//	 * drools rules
//	 * 
//	 * @return
//	 * @throws NotValidChildException
//	 * @throws NotValidOperatorInExpression
//	 * @throws ChildrenNotFoundException
//	 * @throws FieldTooLongException
//	 * @throws IOException
//	 */
//	private Form createRuleTestForm() throws NotValidChildException, NotValidOperatorInExpression,
//			ChildrenNotFoundException, FieldTooLongException, IOException {
//
//		// Create the form
//		Form form = new Form("TestForm");
//
//		// Create the custom variables
//		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
//				CustomVariableScope.QUESTION);
//
//		// Create the tableRule
//		// Only with one conditions colum
//		TableRule tableRule = new TableRule("BaseTable");
//
//		String lastCategory = "";
//		Category category = null;
//		String lastGroup = "";
//		Group group = null;
//		String lastQuestion = "";
//		Question question = null;
//		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/baseTable"),
//				StandardCharsets.UTF_8)) {
//			// [0] = category, [1] = group, [2] = question, [3] = answer, [4] =
//			// value
//			String[] lineSplit = line.split("\t");
//			if (!lastCategory.equals(lineSplit[0])) {
//				// Create a category
//				category = new Category(lineSplit[0]);
//				form.addChild(category);
//				lastCategory = lineSplit[0];
//			}
//			if (!lastGroup.equals(lineSplit[1])) {
//				// Create a group
//				group = new Group(lineSplit[1]);
//				category.addChild(group);
//				lastGroup = lineSplit[1];
//			}
//			if (!lastQuestion.equals(lineSplit[2])) {
//				// Create a question
//				question = new Question(lineSplit[2]);
//				group.addChild(question);
//				lastQuestion = lineSplit[2];
//			}
//			Answer answer = new Answer(lineSplit[3]);
//			question.addChild(answer);
//			question.setAnswerType(AnswerType.INPUT);
//
//			tableRule.getRules().add(
//					new TableRuleRow(new ExpressionValueTreeObjectReference(question), new ExpressionChain(
//							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
//							new ExpressionValueCustomVariable(question, customVarQuestion), new ExpressionOperatorMath(
//									AvailableOperator.ASSIGNATION), new ExpressionValueNumber(Double
//									.parseDouble(lineSplit[4])))));
//		}
//
//		// Add the rows and the table to the form
//		form.getTableRules().add(tableRule);
//		// Creation of a simple diagram to load the table rule
//		form.addDiagram(this.createSimpleDiagram(tableRule));
//
//		return form;
//	}

	private Form createRuleTestSeveralConditionsForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException {

		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category("Sociaal netwerk");
		form.addChild(category);

		Question question1 = new Question("Sociaal.Familie");
		Answer answer1Question1 = new Answer("Sociaal.Familie.Regelmatig");
		question1.addChild(answer1Question1);
		category.addChild(question1);

		Question question2 = new Question("Sociaal.VriendenS");
		Answer answer1Question2 = new Answer("Sociaal.VriendenS.RegelmatigSteun-");
		question2.addChild(answer1Question2);
		category.addChild(question2);

		Question question3 = new Question("Sociaal.Beleving");
		Answer answer1Question3 = new Answer("Sociaal.Beleving.TrekTerug");
		question3.addChild(answer1Question3);
		category.addChild(question3);

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("BaseTable");
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(question1));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(answer1Question1)));
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(question2));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(answer1Question2)));
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(question3));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(answer1Question3)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(5.)));
		tableRule.getRules().add(ruleRow);

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleDiagram(tableRule));

		return form;
	}

	private Form createRuleTestSpecialConditionsForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException, InvalidAnswerFormatException {

		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category("Sociaal netwerk");
		form.addChild(category);
		Category category2 = new Category("Persoonsgegevens");
		form.addChild(category2);

		Question question1 = new Question("Sociaal.Familie");
		Answer answer1Question1 = new Answer("Sociaal.Familie.Regelmatig");
		question1.addChild(answer1Question1);
		category.addChild(question1);

		Question question2 = new Question("Sociaal.VriendenS");
		Answer answer1Question2 = new Answer("Sociaal.VriendenS.RegelmatigSteun-");
		question2.addChild(answer1Question2);
		category.addChild(question2);

		Question question3 = new Question("Sociaal.Beleving");
		Answer answer1Question3 = new Answer("Sociaal.Beleving.TrekTerug");
		question3.addChild(answer1Question3);
		category.addChild(question3);

		Question birthDate = new Question("Aanvrager.Geboortedatum");
		birthDate.setAnswerType(AnswerType.INPUT);
		birthDate.setAnswerFormat(AnswerFormat.DATE);
		category2.addChild(birthDate);

		Question birthDate2 = new Question("Aanvrager.Geboortedatumm");
		birthDate2.setAnswerType(AnswerType.INPUT);
		birthDate2.setAnswerFormat(AnswerFormat.DATE);
		category2.addChild(birthDate2);

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("BaseTable");

		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(category, customVarCategory));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.GREATER_EQUALS),
				new ExpressionValueNumber(2.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(5.)));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(category, customVarCategory));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.LESS_EQUALS),
				new ExpressionValueNumber(2.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(4.)));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(category, customVarCategory));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.GREATER_THAN),
				new ExpressionValueNumber(2.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.)));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(category, customVarCategory));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
				new ExpressionValueNumber(2.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(2.)));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueCustomVariable(category, customVarCategory));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(2.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(question1, customVarQuestion),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(1.)));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthDate, QuestionUnit.DATE));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.LESS_EQUALS),
				new ExpressionValueTreeObjectReference(birthDate2, QuestionUnit.DATE)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(category2, customVarCategory),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(36.)));
		tableRule.getRules().add(ruleRow);

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleDiagram(tableRule));

		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private Form createKidsFormSimpleTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {
		Form form = new Form("KidsScreen");

		Category algemeen = new Category("Algemeen");
		form.addChild(algemeen);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		algemeen.addChild(birthdate);

		// Create custom variables
		CustomVariable bmiClass = new CustomVariable(form, BMI_VARIABLE_NAME, CustomVariableType.STRING,
				CustomVariableScope.FORM);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("BaseTable");

		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(5.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("underweight")));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(6.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("normal")));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(7.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("overweight")));
		tableRule.getRules().add(ruleRow);

		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleDiagram(tableRule));

		return form;
	}

	private Form createKidsFormAdvancedTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {
		Form form = new Form("KidsScreen");

		Category algemeen = new Category("Algemeen");
		form.addChild(algemeen);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		algemeen.addChild(birthdate);

		Question gender = new Question("gender");
		birthdate.setAnswerType(AnswerType.RADIO);
		Answer male = new Answer("M");
		gender.addChild(male);
		Answer female = new Answer("F");
		gender.addChild(female);
		algemeen.addChild(gender);

		Question height = new Question("height");
		height.setAnswerType(AnswerType.INPUT);
		height.setAnswerFormat(AnswerFormat.TEXT);
		algemeen.addChild(height);

		Question heightFather = new Question("heightFather");
		heightFather.setAnswerType(AnswerType.INPUT);
		heightFather.setAnswerFormat(AnswerFormat.TEXT);
		algemeen.addChild(heightFather);

		Question heightMother = new Question("heightMother");
		heightMother.setAnswerType(AnswerType.INPUT);
		heightMother.setAnswerFormat(AnswerFormat.TEXT);
		algemeen.addChild(heightMother);

		Question weight = new Question("weight");
		weight.setAnswerType(AnswerType.INPUT);
		weight.setAnswerFormat(AnswerFormat.TEXT);
		algemeen.addChild(weight);

		Category gezondheid = new Category("Gezondheid");
		form.addChild(gezondheid);

		Question health = new Question("health");
		health.setAnswerType(AnswerType.RADIO);
		Answer yes = new Answer("Y");
		health.addChild(yes);
		Answer no = new Answer("N");
		health.addChild(no);
		gezondheid.addChild(health);

		Category lifestyle = new Category("Lifestyle");
		form.addChild(lifestyle);

		Group voeding = new Group("voeding");
		lifestyle.addChild(voeding);

		Question breakfast = new Question("breakfast");
		breakfast.setAnswerType(AnswerType.RADIO);
		Answer a = new Answer("a");
		breakfast.addChild(a);
		Answer b = new Answer("b");
		breakfast.addChild(b);
		Answer c = new Answer("c");
		breakfast.addChild(c);
		Answer d = new Answer("d");
		breakfast.addChild(d);
		Answer e = new Answer("e");
		breakfast.addChild(e);
		voeding.addChild(breakfast);

		Question fruit = new Question("fruit");
		fruit.setAnswerType(AnswerType.RADIO);
		a = new Answer("a");
		fruit.addChild(a);
		b = new Answer("b");
		fruit.addChild(b);
		c = new Answer("c");
		fruit.addChild(c);
		d = new Answer("d");
		fruit.addChild(d);
		e = new Answer("e");
		fruit.addChild(e);
		voeding.addChild(fruit);

		Question fruitAmount = new Question("fruitAmount");
		fruitAmount.setAnswerType(AnswerType.INPUT);
		fruitAmount.setAnswerFormat(AnswerFormat.TEXT);
		voeding.addChild(fruitAmount);

		Question vegetables = new Question("vegetables");
		vegetables.setAnswerType(AnswerType.RADIO);
		a = new Answer("a");
		vegetables.addChild(a);
		b = new Answer("b");
		vegetables.addChild(b);
		c = new Answer("c");
		vegetables.addChild(c);
		d = new Answer("d");
		vegetables.addChild(d);
		e = new Answer("e");
		vegetables.addChild(e);
		voeding.addChild(vegetables);

		Question vegetablesAmount = new Question("vegetablesAmount");
		vegetablesAmount.setAnswerType(AnswerType.INPUT);
		vegetablesAmount.setAnswerFormat(AnswerFormat.TEXT);
		voeding.addChild(vegetablesAmount);

		Question drinks = new Question("drinks");
		drinks.setAnswerType(AnswerType.RADIO);
		a = new Answer("a");
		drinks.addChild(a);
		b = new Answer("b");
		drinks.addChild(b);
		c = new Answer("c");
		drinks.addChild(c);
		d = new Answer("d");
		drinks.addChild(d);
		voeding.addChild(drinks);

		// Create custom variables
		CustomVariable bmiClass = new CustomVariable(form, BMI_VARIABLE_NAME, CustomVariableType.STRING,
				CustomVariableScope.FORM);
		CustomVariable bmiValue = new CustomVariable(form, BMI_VARIABLE_NAME, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("BaseTable");

		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(5.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("underweight")));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(6.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("normal")));
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(birthdate, QuestionUnit.YEARS));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueNumber(7.)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, bmiClass),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("overweight")));
		tableRule.getRules().add(ruleRow);

		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleDiagram(tableRule));

		return form;
	}

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
