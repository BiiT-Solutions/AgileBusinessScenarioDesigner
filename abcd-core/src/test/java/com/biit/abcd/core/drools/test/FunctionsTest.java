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
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.OrbeonCategoryTranslator;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class FunctionsTest {
	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private final static Charset baseCharset = StandardCharsets.UTF_8;

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	public void readXml() throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/dhszwTest.xml", baseCharset);
		this.orbeonImporter.readXml(xmlFile, this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		String xmlStructure = readFile("./src/test/resources/dhszwTest.xhtml", baseCharset);
		OrbeonCategoryTranslator.getInstance().readXml(this.form, xmlStructure);
	}

	@Test(groups = { "rules" })
	public void testExpressions() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException, InvalidAnswerFormatException {
		// // Load the rules
		// FormToDroolsExporter formDrools = new FormToDroolsExporter();
		// Form vaadinForm = this.createDhszwForm();
		// formDrools.generateDroolRules(vaadinForm);
		// // Load the submitted form
		// this.readXml();
		// this.translateFormCategories();
		// formDrools.runDroolsRules(this.form);
		//
		// // Check the results of the drools execution
		// com.biit.abcd.core.drools.facts.inputform.Category testCat1 =
		// (com.biit.abcd.core.drools.facts.inputform.Category) this.form
		// .getCategory("Financiën");
		// com.biit.abcd.core.drools.facts.inputform.Category testCat2 =
		// (com.biit.abcd.core.drools.facts.inputform.Category) this.form
		// .getCategory("Justitie");
		//
		// Assert.assertEquals(185.26, testCat1.getNumberVariableValue("cScore"));
		// Assert.assertEquals(3226.72, testCat2.getNumberVariableValue("cScore"));
	}

	/**
	 * Create the form structure. Creates to simple assignation rules in the table rule and one expression with max func
	 * Form used to create the drools rules
	 * 
	 * @return
	 * @throws NotValidChildException
	 * @throws NotValidOperatorInExpression
	 * @throws ChildrenNotFoundException
	 * @throws FieldTooLongException
	 * @throws IOException
	 * @throws InvalidAnswerFormatException
	 * @throws CharacterNotAllowedException
	 */
	private Form createDhszwForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException, InvalidAnswerFormatException,
			CharacterNotAllowedException {

		// Create the form
		Form form = new Form("DhszwForm");

		Category categoryFin = null;
		Category categoryJus = null;

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarForm = new CustomVariable(form, "fScore", CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Question father = new Question("fatherHeight");
		father.setAnswerType(AnswerType.INPUT);
		father.setAnswerFormat(AnswerFormat.NUMBER);

		Question mother = new Question("motherHeight");
		mother.setAnswerType(AnswerType.INPUT);
		mother.setAnswerFormat(AnswerFormat.NUMBER);

		Question weight = new Question("weight");
		weight.setAnswerType(AnswerType.INPUT);
		weight.setAnswerFormat(AnswerFormat.NUMBER);

		Question height = new Question("height");
		height.setAnswerType(AnswerType.INPUT);
		height.setAnswerFormat(AnswerFormat.NUMBER);

		// Create the tableRule
		TableRule tableRule = new TableRule("BaseTable");

		String lastCategory = "";
		Category category = null;
		String lastQuestion = "";
		Question question = null;
		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/baseTable"),
				StandardCharsets.UTF_8)) {
			// [0] = category, [1] = question, [2] = answer, [3] = value
			String[] lineSplit = line.split("\t");
			if (!lastCategory.equals(lineSplit[0])) {
				// Create a category
				category = new Category(lineSplit[0]);
				if (lineSplit[0].equals("Financiën")) {
					categoryFin = category;
				} else if (lineSplit[0].equals("Justitie")) {
					categoryJus = category;
				}
				form.addChild(category);
				lastCategory = lineSplit[0];
			}
			if (!lastQuestion.equals(lineSplit[1])) {
				// Create a question
				question = new Question(lineSplit[1]);
				category.addChild(question);
				lastQuestion = lineSplit[1];
			}
			Answer answer = new Answer(lineSplit[2]);
			question.addChild(answer);
			question.setAnswerType(AnswerType.INPUT);

			tableRule.getRules().add(
					new TableRuleRow(new ExpressionValueTreeObjectReference(question), new ExpressionChain(
							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
							new ExpressionValueCustomVariable(question, customVarQuestion), new ExpressionOperatorMath(
									AvailableOperator.ASSIGNATION), new ExpressionValueNumber(Double
									.parseDouble(lineSplit[3])))));
		}

		categoryFin.addChild(father);
		categoryFin.addChild(mother);
		categoryFin.addChild(weight);
		categoryFin.addChild(height);

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule);

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

		// PMT EXPRESSION
		DiagramCalculation diagramPmtExpressionNode = new DiagramCalculation();
		diagramPmtExpressionNode.setFormExpression(new ExpressionChain("PMT_Expression",
				new ExpressionValueCustomVariable(categoryJus, customVarCategory), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.PMT),
				new ExpressionValueNumber(10), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						36), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(100000),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		diagramPmtExpressionNode.setJointjsId(IdGenerator.createId());
		diagramPmtExpressionNode.setType(DiagramObjectType.CALCULATION);
		Node nodeExpression = new Node(diagramPmtExpressionNode.getJointjsId());

		// --------------------------------------------------------------------------------------
		// DO NOT DELETE THIS COMMENTS !!!

		// // MATH EXPRESSION
		// DiagramCalculation diagramOperationsExpressionNode = new
		// DiagramCalculation();
		// diagramOperationsExpressionNode.setFormExpression(new
		// ExpressionChain("Math_Expression",
		// new ExpressionValueCustomVariable(category, customVarCategory), new
		// ExpressionOperatorMath(
		// AvailableOperator.ASSIGNATION), new ExpressionValueNumber(10.), new
		// ExpressionOperatorMath(
		// AvailableOperator.PLUS), new
		// ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
		// new ExpressionValueTreeObjectReference(mother), new
		// ExpressionOperatorMath(
		// AvailableOperator.MULTIPLICATION), new ExpressionValueNumber(0.6),
		// new ExpressionSymbol(
		// AvailableSymbol.RIGHT_BRACKET), new
		// ExpressionOperatorMath(AvailableOperator.PLUS),
		// new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new
		// ExpressionValueTreeObjectReference(father),
		// new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new
		// ExpressionValueNumber(0.4),
		// new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// diagramOperationsExpressionNode.setJointjsId(IdGenerator.createId());
		// diagramOperationsExpressionNode.setType(DiagramObjectType.CALCULATION);
		// Node nodeOperationsExpression = new
		// Node(diagramOperationsExpressionNode.getJointjsId());

		// // MATH EXPRESSION
		// DiagramCalculation diagramOperationsExpressionNode = new
		// DiagramCalculation();
		// diagramOperationsExpressionNode.setFormExpression(new
		// ExpressionChain("Math_Form_Expression",
		// new ExpressionValueCustomVariable(form, customVarForm), new
		// ExpressionOperatorMath(
		// AvailableOperator.ASSIGNATION), new ExpressionValueNumber(44.5), new
		// ExpressionOperatorMath(
		// AvailableOperator.PLUS), new
		// ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
		// new ExpressionValueNumber(0.376), new ExpressionOperatorMath(
		// AvailableOperator.MULTIPLICATION), new
		// ExpressionValueTreeObjectReference(mother), new ExpressionSymbol(
		// AvailableSymbol.RIGHT_BRACKET), new
		// ExpressionOperatorMath(AvailableOperator.PLUS),
		// new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET), new
		// ExpressionValueTreeObjectReference(father),
		// new ExpressionOperatorMath(AvailableOperator.MULTIPLICATION), new
		// ExpressionValueNumber(0.411),
		// new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// diagramOperationsExpressionNode.setJointjsId(IdGenerator.createId());
		// diagramOperationsExpressionNode.setType(DiagramObjectType.CALCULATION);
		// Node nodeOperationsExpression = new
		// Node(diagramOperationsExpressionNode.getJointjsId());

		// // MATH EXPRESSION
		// DiagramCalculation diagramOperationsExpressionNode = new
		// DiagramCalculation();
		// diagramOperationsExpressionNode.setFormExpression(new
		// ExpressionChain("BMI_Form_Expression",
		// new ExpressionValueCustomVariable(form, customVarForm), new
		// ExpressionOperatorMath(
		// AvailableOperator.ASSIGNATION), new
		// ExpressionValueTreeObjectReference(weight),
		// new ExpressionOperatorMath(AvailableOperator.DIVISION), new
		// ExpressionSymbol(
		// AvailableSymbol.LEFT_BRACKET), new
		// ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
		// new ExpressionValueTreeObjectReference(height), new
		// ExpressionOperatorMath(AvailableOperator.DIVISION),
		// new ExpressionValueNumber(100.), new
		// ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
		// new ExpressionOperatorMath(AvailableOperator.PLUS), new
		// ExpressionSymbol(AvailableSymbol.LEFT_BRACKET),
		// new ExpressionValueTreeObjectReference(height), new
		// ExpressionOperatorMath(AvailableOperator.DIVISION),
		// new ExpressionValueNumber(100.), new
		// ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
		// new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// diagramOperationsExpressionNode.setJointjsId(IdGenerator.createId());
		// diagramOperationsExpressionNode.setType(DiagramObjectType.CALCULATION);
		// Node nodeOperationsExpression = new
		// Node(diagramOperationsExpressionNode.getJointjsId());

		// MATH EXPRESSION
		DiagramCalculation diagramOperationsExpressionNode = new DiagramCalculation();
		diagramOperationsExpressionNode.setFormExpression(new ExpressionChain("BMI_Form_Expression",
				new ExpressionValueCustomVariable(form, customVarForm), new ExpressionOperatorMath(
						AvailableOperator.ASSIGNATION), new ExpressionValueTreeObjectReference(weight),
				new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueTreeObjectReference(height)));
		diagramOperationsExpressionNode.setJointjsId(IdGenerator.createId());
		diagramOperationsExpressionNode.setType(DiagramObjectType.CALCULATION);
		Node nodeOperationsExpression = new Node(diagramOperationsExpressionNode.getJointjsId());

		// PMT RULE
		DiagramRule diagramPmtRuleNode = new DiagramRule();
		diagramPmtRuleNode.setRule(new Rule("PMT_Rule", new ExpressionChain(new ExpressionValueString("1")),
				new ExpressionChain(new ExpressionValueCustomVariable(categoryFin, customVarCategory),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
								AvailableFunction.PMT), new ExpressionValueNumber(10), new ExpressionSymbol(
								AvailableSymbol.COMMA), new ExpressionValueNumber(72), new ExpressionSymbol(
								AvailableSymbol.COMMA), new ExpressionValueNumber(10000), new ExpressionSymbol(
								AvailableSymbol.RIGHT_BRACKET))));
		diagramPmtRuleNode.setJointjsId(IdGenerator.createId());
		diagramPmtRuleNode.setType(DiagramObjectType.RULE);
		Node nodeRule = new Node(diagramPmtRuleNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startTable = new DiagramLink(nodeSource, nodeTable);
		startTable.setJointjsId(IdGenerator.createId());
		startTable.setType(DiagramObjectType.LINK);
		DiagramLink tableExpression = new DiagramLink(nodeTable, nodeOperationsExpression);
		tableExpression.setJointjsId(IdGenerator.createId());
		tableExpression.setType(DiagramObjectType.LINK);
		DiagramLink operationsExpression = new DiagramLink(nodeOperationsExpression, nodeExpression);
		operationsExpression.setJointjsId(IdGenerator.createId());
		operationsExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionRule = new DiagramLink(nodeExpression, nodeRule);
		tableExpression.setJointjsId(IdGenerator.createId());
		tableExpression.setType(DiagramObjectType.LINK);
		DiagramLink subdiagramEnd = new DiagramLink(nodeRule, nodeSink);
		subdiagramEnd.setJointjsId(IdGenerator.createId());
		subdiagramEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramTableRuleNode);
		mainDiagram.addDiagramObject(diagramOperationsExpressionNode);
		mainDiagram.addDiagramObject(diagramPmtExpressionNode);
		mainDiagram.addDiagramObject(diagramPmtRuleNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startTable);
		mainDiagram.addDiagramObject(tableExpression);
		mainDiagram.addDiagramObject(operationsExpression);
		mainDiagram.addDiagramObject(expressionRule);
		mainDiagram.addDiagramObject(subdiagramEnd);

		form.addDiagram(mainDiagram);

		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
