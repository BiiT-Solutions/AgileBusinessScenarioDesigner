package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryNameWithoutTranslation;
import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonCategoryTranslator;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class TableRuleTest {

	private final static String APP = "Application1";
	private final static String FORM = "Form1";

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/dhszwTest.xml", Charset.defaultCharset());
		this.orbeonImporter.readXml(xmlFile, this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		String xmlStructure = readFile("./src/test/resources/dhszwTest.xhtml", Charset.defaultCharset());
		OrbeonCategoryTranslator.getInstance().readXml(this.form, xmlStructure);
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleLoadAndExecution() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException, IOException, CategoryDoesNotExistException, QuestionDoesNotExistException {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createRuleTestForm();
		formDrools.parse(vaadinForm);
		formDrools.go(this.form);

		// Check the results of the drools execution
		ICategory testCat1 = this.form.getCategory("Alcohol-, drugs-, game- of gokverslaving");
		com.biit.abcd.core.drools.facts.inputform.Question testQuestion1 = (com.biit.abcd.core.drools.facts.inputform.Question) testCat1.getQuestion("Verslaving.Aanwezig");
		ICategory testCat2 = this.form.getCategory("Huisvesting");
		com.biit.abcd.core.drools.facts.inputform.Question testQuestion2 = (com.biit.abcd.core.drools.facts.inputform.Question) testCat2.getQuestion("Huisvesting.Gebruik");
		Assert.assertEquals(1.0, testQuestion1.getNumberVariableValue("qScore"));
		Assert.assertEquals(5.0, testQuestion2.getNumberVariableValue("qScore"));
	}

	/**
	 * Create the form structure. Creates to simple assignation rules in the
	 * table rule and one expression with max func Form used to create the
	 * drools rules
	 *
	 * @return
	 * @throws NotValidChildException
	 * @throws NotValidOperatorInExpression
	 * @throws ChildrenNotFoundException
	 * @throws FieldTooLongException
	 * @throws IOException
	 */
	private Form createRuleTestForm() throws NotValidChildException, NotValidOperatorInExpression,
	ChildrenNotFoundException, FieldTooLongException, IOException {

		// Create the form
		Form form = new Form("DhszwForm");

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		TableRule tableRule = new TableRule("BaseTable");

		String lastCategory = "";
		Category category = null;
		String lastQuestion = "";
		Question question = null;
		for(String line: Files.readAllLines(Paths.get("./src/test/resources/tables/baseTable"), StandardCharsets.UTF_8)) {
			// [0] = category, [1] = question, [2] = answer, [3] = value
			String[] lineSplit = line.split("\t");
			if(!lastCategory.equals(lineSplit[0])){
				// Create a category
				category = new Category(lineSplit[0]);
				form.addChild(category);
				lastCategory = lineSplit[0];
			}
			if(!lastQuestion.equals(lineSplit[1])){
				// Create a question
				question = new Question(lineSplit[1]);
				category.addChild(question);
				lastQuestion = lineSplit[1];
			}
			Answer answer = new Answer(lineSplit[2]);
			question.addChild(answer);

			tableRule.getRules().add(
					new TableRuleRow(new ExpressionValueTreeObjectReference(question), new ExpressionChain(
							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
									new ExpressionValueCustomVariable(question, customVarQuestion),
									new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(
											Double.parseDouble(lineSplit[3])))));
		}

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleDiagram(tableRule));

		return form;
	}

	private Diagram createSimpleDiagram(TableRule tableRule){
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

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
