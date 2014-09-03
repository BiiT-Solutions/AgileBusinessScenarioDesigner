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
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
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
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class DiagramDateTest {
	private final static String APP = "Application1";
	private final static String FORM = "Form1";

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/dhszwTest.xml",  StandardCharsets.UTF_8);
		this.orbeonImporter.readXml(xmlFile, this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void translateFormCategories() throws DocumentException, IOException, CategoryNameWithoutTranslation {
		String xmlStructure = readFile("./src/test/resources/dhszwTest.xhtml",  StandardCharsets.UTF_8);
		OrbeonCategoryTranslator.getInstance().readXml(this.form, xmlStructure);
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleLoadAndExecution() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, QuestionDoesNotExistException, InvalidAnswerFormatException, RuleNotImplementedException {
		FormToDroolsExporter formDrools = new FormToDroolsExporter();
		Form vaadinForm = this.createDiagramTestForm();
		formDrools.parse(vaadinForm);
		formDrools.runDroolsRules(this.form);

		// Check the results of the drools execution
		com.biit.abcd.core.drools.facts.inputform.Category testCat1 = (com.biit.abcd.core.drools.facts.inputform.Category) this.form
				.getCategory("Financiën");
		com.biit.abcd.core.drools.facts.inputform.Question testQuestion1 = (com.biit.abcd.core.drools.facts.inputform.Question) testCat1
				.getQuestion("Financien.Inkomen");
		Assert.assertEquals(5.0, testQuestion1.getNumberVariableValue("qScore"));
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
	 */
	private Form createDiagramTestForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException, InvalidAnswerFormatException {

		// Create the form
		Form form = new Form("DhszwForm");

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		TableRule tableRule1 = new TableRule("BaseTable1");

		String lastCategory1 = "";
		Category category1 = null;
		String lastQuestion1 = "";
		Question question1 = null;
		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/table1"), StandardCharsets.UTF_8)) {
			// [0] = category, [1] = question, [2] = answer, [3] = value
			String[] lineSplit = line.split("\t");
			if (!lastCategory1.equals(lineSplit[0])) {
				// Create a category
				category1 = new Category(lineSplit[0]);
				form.addChild(category1);
				lastCategory1 = lineSplit[0];
			}
			if (!lastQuestion1.equals(lineSplit[1])) {
				// Create a question
				question1 = new Question(lineSplit[1]);
				category1.addChild(question1);
				lastQuestion1 = lineSplit[1];
			}
			Answer answer = new Answer(lineSplit[2]);
			question1.addChild(answer);

			tableRule1.getRules().add(
					new TableRuleRow(new ExpressionValueTreeObjectReference(question1), new ExpressionChain(
							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
							new ExpressionValueCustomVariable(question1, customVarQuestion),
							new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(Double
									.parseDouble(lineSplit[3])))));
		}

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule1);

		// Create the tableRule
		TableRule tableRule2 = new TableRule("BaseTable2");

		String lastCategory2 = "";
		Category category2 = null;
		String lastQuestion2 = "";
		Question question2 = null;
		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/table2"), StandardCharsets.UTF_8)) {
			// [0] = category, [1] = question, [2] = answer, [3] = value
			String[] lineSplit = line.split("\t");
			if (!lastCategory2.equals(lineSplit[0])) {
				// Create a category
				category2 = new Category(lineSplit[0]);
				form.addChild(category2);
				lastCategory2 = lineSplit[0];
			}
			if (!lastQuestion2.equals(lineSplit[1])) {
				// Create a question
				question2 = new Question(lineSplit[1]);
				category2.addChild(question2);
				lastQuestion2 = lineSplit[1];
			}
			Answer answer = new Answer(lineSplit[2]);
			question2.addChild(answer);

			tableRule2.getRules().add(
					new TableRuleRow(new ExpressionValueTreeObjectReference(question2), new ExpressionChain(
							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
							new ExpressionValueCustomVariable(question2, customVarQuestion),
							new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(Double
									.parseDouble(lineSplit[3])))));
		}

		// Add the rows and the table to the form
		form.getTableRules().add(tableRule2);

		Question forkQuestion = new Question("Aanvrager.Geboortedatum");
		forkQuestion.setAnswerType(AnswerType.INPUT);
		forkQuestion.setAnswerFormat(AnswerFormat.DATE);
		// Answer forkAnswer1 = new Answer("Dagbesteding.Werk.Vast");
		// Answer forkAnswer2 = new Answer("Dagbesteding.Werk.Tijdelijk");
		// Answer forkAnswer3 = new Answer("Dagbesteding.Werk.OpleidingHoger");
		// forkQuestion.addChild(forkAnswer1);
		// forkQuestion.addChild(forkAnswer2);
		// forkQuestion.addChild(forkAnswer3);
		// Group forkGroup = new Group("Aanvrager");
		// forkGroup.addChild(forkQuestion);
		Category forkCategory = new Category("Persoonsgegevens");
		forkCategory.addChild(forkQuestion);
		form.addChild(forkCategory);

		// Creation of a simple diagram to load the table rule
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramForkNode = new DiagramFork();
		diagramForkNode.setJointjsId(IdGenerator.createId());
		diagramForkNode.setType(DiagramObjectType.FORK);
		// Question Justitie.Politie, Answer in test form Justitie.Politie.Nooit
		diagramForkNode.setReference(new ExpressionValueTreeObjectReference(forkQuestion));
		Node nodeFork = new Node(diagramForkNode.getJointjsId());

		DiagramTable diagramTableRuleNode1 = new DiagramTable();
		diagramTableRuleNode1.setTable(tableRule1);
		diagramTableRuleNode1.setJointjsId(IdGenerator.createId());
		diagramTableRuleNode1.setType(DiagramObjectType.TABLE);
		Node nodeTable1 = new Node(diagramTableRuleNode1.getJointjsId());

		// DiagramTable diagramTableRuleNode2 = new DiagramTable();
		// diagramTableRuleNode2.setTable(tableRule2);
		// diagramTableRuleNode2.setJointjsId(IdGenerator.createId());
		// diagramTableRuleNode2.setType(DiagramObjectType.TABLE);
		// Node nodeTable2 = new Node(diagramTableRuleNode2.getJointjsId());

		DiagramLink startForkLink = new DiagramLink(nodeSource, nodeFork);
		startForkLink.setJointjsId(IdGenerator.createId());
		startForkLink.setType(DiagramObjectType.LINK);

		DiagramLink forkTable1Link = new DiagramLink(nodeFork, nodeTable1);
		forkTable1Link.setJointjsId(IdGenerator.createId());
		forkTable1Link.setType(DiagramObjectType.LINK);
		// forkTable1Link.setExpressionChain(new ExpressionChain(
		// new ExpressionOperatorLogic(AvailableOperator.GREATER_EQUALS),
		// new ExpressionValueNumber(18.)
		// ));
		forkTable1Link.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(forkQuestion),
				new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(0.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						18.), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));
		// DiagramLink forkTable2Link = new DiagramLink(nodeFork, nodeTable2);
		// forkTable2Link.setJointjsId(IdGenerator.createId());
		// forkTable2Link.setType(DiagramObjectType.LINK);
		// forkTable2Link.setExpressionChain(new ExpressionChain(new
		// ExpressionValueTreeObjectReference(forkQuestion.getChild(0))));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramForkNode);
		mainDiagram.addDiagramObject(diagramTableRuleNode1);
		// mainDiagram.addDiagramObject(diagramTableRuleNode2);

		mainDiagram.addDiagramObject(startForkLink);
		mainDiagram.addDiagramObject(forkTable1Link);
		// mainDiagram.addDiagramObject(forkTable2Link);

		form.addDiagram(mainDiagram);

		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
