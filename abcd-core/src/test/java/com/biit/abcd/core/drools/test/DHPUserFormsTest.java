package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryNameWithoutTranslation;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonCategoryTranslator;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonImporter;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;

public class DHPUserFormsTest {

	private final static String APP = "WebForms";
	private final static String FORM = "De_Haagse_Passage_v2";
	private final static List<String> DocumentIds = Arrays.asList("d15138bcf2ce90e0b73bb91359df30d5ac887684",
			"5d0fe66cdfc4a1e9a0398cb12ef1f8e4f2f41aa3", "c7a65ec54d049afbc3809c1b3e9936103b282a7d",
			"e8018e7bd6969fddaa478f799be8a334434e50f6", "c32d64ee6f4864633d23bee49c1c56c5e6e5f1e9",
			"0a15054a01a539ea5d5f55f6660948da7b638288", "3694a3fc98d4e57707981c2f83a6fbc4ea9f8e32",
			"370023c797b9b9b691ed0e64a559f6adb7971df5", "ee9f7ba259d2980f7c3def579aaf53920666026c",
			"9a8bc9ac0c130f3de436fc9dbcb4c587755aa004", "9dcd7d2ee3eca81248fe0f5403c283ea21bca128",
			"7b3754242240690f130508e61b7b593755396c6e", "ec3a4ce69cb6a4df0cda028e78e553deee0f56db");

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();
	private List<Rule> questionExceptionRules = new ArrayList<Rule>();
	private List<Rule> categoryExceptionRules = new ArrayList<Rule>();

	public void readXml(String docId) throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		this.orbeonImporter.readXml(OrbeonImporter.getXml(APP, FORM, docId), this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	public void translateFormCategories() throws DocumentException, CategoryNameWithoutTranslation, IOException {
		String xmlStructure = readFile("./src/test/resources/dhszwTest.xhtml", Charset.defaultCharset());
		OrbeonCategoryTranslator.getInstance().readXml(this.form, xmlStructure);
	}

	@Test(groups = { "rules" })
	public void completeZrmTest() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createZrmForm();
		// Load the submitted form
		for (String docId : DocumentIds) {
			formDrools.parse(vaadinForm);
			this.readXml(docId);
			this.translateFormCategories();
			formDrools.go(this.form);
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
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
	private Form createZrmForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException {

		// Create the form
		Form form = new Form("DhszwForm");

		// Create the question/ category to test the birth date
		Question birthDateQuest = new Question("Aanvrager.Geboortedatum");
		birthDateQuest.setAnswerType(AnswerType.INPUT);
		birthDateQuest.setAnswerFormat(AnswerFormat.DATE);
		Category birthDateCat = new Category("Persoonsgegevens");
		birthDateCat.addChild(birthDateQuest);
		form.addChild(birthDateCat);

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		// CustomVariable customVarTextCategory = new CustomVariable(form,
		// "cScoreText", CustomVariableType.STRING,
		// CustomVariableScope.CATEGORY);

		// Create the tableRule
		TableRule baseTableRule = new TableRule("BaseTable");

		String lastCategory = "";
		Category category = null;
		String lastQuestion = "";
		Question question = null;
		// We need to store this questions/answers for posterior
		// rules/exceptions
		Category categoryFin = null;
		Category categoryGee = null;
		Category categoryLich = null;
		Question questionRul11 = null;
		Question questionRul12 = null;
		Question questionRul21 = null;
		Question questionRul22 = null;
		Question questionRul31 = null;
		Question questionRul32 = null;
		Question questionRul41 = null;
		Question questionRul42 = null;
		Question questionRul43 = null;
		Question questionRul51 = null;
		Question questionRul52 = null;
		Answer answer11Rul1 = null;
		Answer answer12Rul1 = null;
		Answer answer13Rul1 = null;
		Answer answer21Rul2 = null;
		Answer answer22Rul2 = null;
		Answer answer31Rul3 = null;
		Answer answer32Rul3 = null;
		Answer answer41Rul4 = null;
		Answer answer42Rul4 = null;
		Answer answer43Rul4 = null;
		Answer answer44Rul4 = null;
		Answer answer45Rul4 = null;
		Answer answer51Rul5 = null;
		Answer answer52Rul5 = null;
		Answer answer53Rul5 = null;
		Answer answer54Rul5 = null;
		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/baseTable"),
				StandardCharsets.UTF_8)) {
			// [0] = category, [1] = question, [2] = answer, [3] = value
			String[] lineSplit = line.split("\t");
			if (!lastCategory.equals(lineSplit[0])) {
				// Create a category
				category = new Category(lineSplit[0]);
				if (lineSplit[0].equals("Financiën")) {
					categoryFin = category;
				} else if (lineSplit[0].equals("Geestelijke gezondheid")) {
					categoryGee = category;
				} else if (lineSplit[0].equals("Lichamelijke gezondheid")) {
					categoryLich = category;
				}
				form.addChild(category);
				lastCategory = lineSplit[0];
			}
			if (!lastQuestion.equals(lineSplit[1])) {
				// Create a question
				question = new Question(lineSplit[1]);
				category.addChild(question);
				lastQuestion = lineSplit[1];
				if (lineSplit[1].equals("Geestelijk.Behandeling")) {
					questionRul11 = question;
				} else if (lineSplit[1].equals("Geestelijk.Functioneren")) {
					questionRul12 = question;
				} else if (lineSplit[1].equals("Lichamelijk.Behandeling")) {
					questionRul21 = question;
				} else if (lineSplit[1].equals("Lichamelijk.Aandoeningen")) {
					questionRul22 = question;
				} else if (lineSplit[1].equals("Verslaving.Behandeltrouw")) {
					questionRul31 = question;
				} else if (lineSplit[1].equals("Verslaving.Aanwezig")) {
					questionRul32 = question;
				} else if (lineSplit[1].equals("Sociaal.Familie")) {
					questionRul41 = question;
				} else if (lineSplit[1].equals("Sociaal.VriendenS")) {
					questionRul42 = question;
				} else if (lineSplit[1].equals("Sociaal.Beleving")) {
					questionRul43 = question;
				} else if (lineSplit[1].equals("Financien.Beheer.Hulp")) {
					questionRul51 = question;
				} else if (lineSplit[1].equals("Financien.Beheer")) {
					questionRul52 = question;
				}
			}
			Answer answer = new Answer(lineSplit[2]);
			if (lineSplit[2].equals("Geestelijk.Behandeling.Arts")) {
				answer11Rul1 = answer;
			} else if (lineSplit[2].equals("Geestelijk.Behandeling.Regelmatig")) {
				answer12Rul1 = answer;
			} else if (lineSplit[2].equals("Geestelijk.Functioneren.Regelmatig")) {
				answer13Rul1 = answer;
			} else if (lineSplit[2].equals("Lichamelijk.Behandeling.Regelmatig")) {
				answer21Rul2 = answer;
			} else if (lineSplit[2].equals("Lichamelijk.Aandoeningen.Meerdere")) {
				answer22Rul2 = answer;
			} else if (lineSplit[2].equals("Verslaving.Behandeltrouw.Intensief")) {
				answer31Rul3 = answer;
			} else if (lineSplit[2].equals("Verslaving.Aanwezig.Wel")) {
				answer32Rul3 = answer;
			} else if (lineSplit[2].equals("Sociaal.Familie.Geen")) {
				answer41Rul4 = answer;
			} else if (lineSplit[2].equals("Sociaal.Familie.Nooit")) {
				answer42Rul4 = answer;
			} else if (lineSplit[2].equals("Sociaal.VriendenS.Nooit")) {
				answer43Rul4 = answer;
			} else if (lineSplit[2].equals("Sociaal.VriendenS.Geen")) {
				answer44Rul4 = answer;
			} else if (lineSplit[2].equals("Sociaal.Beleving.Eenzaam")) {
				answer45Rul4 = answer;
			} else if (lineSplit[2].equals("Financien.Beheer.Hulp.Ja")) {
				answer51Rul5 = answer;
			} else if (lineSplit[2].equals("Financien.Beheer.WelEensVerrast")) {
				answer52Rul5 = answer;
			} else if (lineSplit[2].equals("Financien.Beheer.VeelVerrassingen")) {
				answer53Rul5 = answer;
			} else if (lineSplit[2].equals("Financien.Beheer.Spoorbijster")) {
				answer54Rul5 = answer;
			}
			question.addChild(answer);

			baseTableRule.getRules().add(
					new TableRuleRow(new ExpressionValueTreeObjectReference(question), new ExpressionChain(
							new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
							new ExpressionValueCustomVariable(question, customVarQuestion), new ExpressionOperatorMath(
									AvailableOperator.ASSIGNATION), new ExpressionValueNumber(Double
									.parseDouble(lineSplit[3])))));
		}

		// Add the rows and the table to the form
		form.getTableRules().add(baseTableRule);

		// Create Rules for the questions
		Rule rul1 = new Rule("QuestionException1",
		// When Q11 IN(A11, A12) AND Q12==A13 Then Q12.qScore += 1
				new ExpressionChain(new ExpressionValueTreeObjectReference(questionRul11), new ExpressionFunction(
						AvailableFunction.IN), new ExpressionValueTreeObjectReference(answer11Rul1),
						new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
								answer12Rul1), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET),
						new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionValueTreeObjectReference(
								questionRul12), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
						new ExpressionValueTreeObjectReference(answer13Rul1)), new ExpressionChain(
						new ExpressionValueCustomVariable(questionRul12, customVarQuestion),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(
								questionRul12, customVarQuestion), new ExpressionOperatorMath(AvailableOperator.PLUS),
						new ExpressionValueNumber(1.)));
		form.getRules().add(rul1);
		this.questionExceptionRules.add(rul1);

		Rule rul2 = new Rule("QuestionException2",
		// When Q21 == A21 AND Q22==A22 Then Q22.qScore += 1
				new ExpressionChain(new ExpressionValueTreeObjectReference(questionRul21), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(answer21Rul2),
						new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionValueTreeObjectReference(
								questionRul22), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
						new ExpressionValueTreeObjectReference(answer22Rul2)), new ExpressionChain(
						new ExpressionValueCustomVariable(questionRul22, customVarQuestion),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(
								questionRul22, customVarQuestion), new ExpressionOperatorMath(AvailableOperator.PLUS),
						new ExpressionValueNumber(1.)));
		form.getRules().add(rul2);
		this.questionExceptionRules.add(rul2);

		Rule rul3 = new Rule("QuestionException3",
		// When Q31 == A31 AND Q32==A32 Then Q32.qScore += 1
				new ExpressionChain(new ExpressionValueTreeObjectReference(questionRul31), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(answer31Rul3),
						new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionValueTreeObjectReference(
								questionRul32), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
						new ExpressionValueTreeObjectReference(answer32Rul3)), new ExpressionChain(
						new ExpressionValueCustomVariable(questionRul32, customVarQuestion),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(
								questionRul32, customVarQuestion), new ExpressionOperatorMath(AvailableOperator.PLUS),
						new ExpressionValueNumber(1.)));
		form.getRules().add(rul3);
		this.questionExceptionRules.add(rul3);

		Rule rul4 = new Rule("QuestionException4", new ExpressionChain(new ExpressionValueTreeObjectReference(
				questionRul41), new ExpressionFunction(AvailableFunction.IN), new ExpressionValueTreeObjectReference(
				answer41Rul4), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(
				answer42Rul4), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
				AvailableOperator.AND), new ExpressionValueTreeObjectReference(questionRul42), new ExpressionFunction(
				AvailableFunction.IN), new ExpressionValueTreeObjectReference(answer43Rul4), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueTreeObjectReference(answer44Rul4), new ExpressionSymbol(
				AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.AND),
				new ExpressionValueTreeObjectReference(questionRul43), new ExpressionOperatorLogic(
						AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(answer45Rul4)),
				new ExpressionChain(new ExpressionValueCustomVariable(questionRul43, customVarQuestion),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(
								questionRul43, customVarQuestion), new ExpressionOperatorMath(AvailableOperator.MINUS),
						new ExpressionValueNumber(1.)));
		form.getRules().add(rul4);
		this.questionExceptionRules.add(rul4);

		// Creation of the accumulate expressions
		int accumExp = 1;
		for (String line : Files.readAllLines(Paths.get("./src/test/resources/tables/accumulations"),
				StandardCharsets.UTF_8)) {
			// [0] = category, [1] = function, [2] = questions
			String[] lineSplit = line.split("\t");

			ExpressionChain testExpressionChain = new ExpressionChain(lineSplit[0] + "Score_" + accumExp);
			testExpressionChain.addExpression(new ExpressionValueCustomVariable(this.getCategoryFromForm(form,
					lineSplit[0]), customVarCategory));
			testExpressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
			if (lineSplit[1].equals("min")) {
				testExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.MIN));
			}
			// Each position is a question
			String[] questionSplit = lineSplit[2].split("::");
			int i = 0;
			for (String questionString : questionSplit) {
				testExpressionChain.addExpression(new ExpressionValueCustomVariable(this.getQuestionFromCategory(
						this.getCategoryFromForm(form, lineSplit[0]), questionString), customVarQuestion));
				if (i < (questionSplit.length - 1)) {
					// So the last expression of the rule before the bracket is
					// not a comma
					testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
				}
				i++;
			}
			testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChain().add(testExpressionChain);
			accumExp++;
		}

		// TODO
		// Create rules for the categories
		Rule rul5 = new Rule("CategoryException1", new ExpressionChain(new ExpressionValueTreeObjectReference(
				questionRul51), new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(answer51Rul5),
				new ExpressionOperatorLogic(AvailableOperator.AND), new ExpressionValueTreeObjectReference(
						questionRul52), new ExpressionFunction(AvailableFunction.IN),
				new ExpressionValueTreeObjectReference(answer52Rul5), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(answer53Rul5), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueTreeObjectReference(answer54Rul5), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)), new ExpressionChain(new ExpressionValueCustomVariable(
				categoryFin, customVarCategory), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(3.)));
		form.getRules().add(rul5);
		this.categoryExceptionRules.add(rul5);

		Rule rul6 = new Rule("CategoryException2", new ExpressionChain(new ExpressionValueTreeObjectReference(
				birthDateQuest), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(0.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(17.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)), new ExpressionChain(new ExpressionValueCustomVariable(
				categoryFin, customVarCategory), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(categoryFin, customVarCategory), new ExpressionOperatorMath(
						AvailableOperator.MINUS), new ExpressionValueNumber(2.)));
		form.getRules().add(rul6);
		this.categoryExceptionRules.add(rul6);

		Rule rul65 = new Rule("CategoryException2.5", new ExpressionChain(new ExpressionValueTreeObjectReference(
				birthDateQuest), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(18.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(27.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)), new ExpressionChain(new ExpressionValueCustomVariable(
				categoryFin, customVarCategory), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(categoryFin, customVarCategory), new ExpressionOperatorMath(
						AvailableOperator.MINUS), new ExpressionValueNumber(1.)));
		form.getRules().add(rul65);
		this.categoryExceptionRules.add(rul65);

		Rule rul7 = new Rule("CategoryException3", new ExpressionChain(new ExpressionValueTreeObjectReference(
				birthDateQuest), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(80.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(150.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)), new ExpressionChain(new ExpressionValueCustomVariable(
				categoryGee, customVarCategory), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(categoryGee, customVarCategory), new ExpressionOperatorMath(
						AvailableOperator.MINUS), new ExpressionValueNumber(1.)));
		form.getRules().add(rul7);
		this.categoryExceptionRules.add(rul7);

		Rule rul8 = new Rule("CategoryException4", new ExpressionChain(new ExpressionValueTreeObjectReference(
				birthDateQuest), new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(80.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(150.), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)), new ExpressionChain(new ExpressionValueCustomVariable(
				categoryLich, customVarCategory), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueCustomVariable(categoryLich, customVarCategory), new ExpressionOperatorMath(
						AvailableOperator.MINUS), new ExpressionValueNumber(1.)));
		form.getRules().add(rul8);
		this.categoryExceptionRules.add(rul8);

		// // Creation of the result rules
		// int ruleNumber = 1;
		// for(String line:
		// Files.readAllLines(Paths.get("./src/test/resources/tables/returnedText"),
		// StandardCharsets.UTF_8)) {
		// // [0] = category, [1] = score, [2] = text
		// String[] lineSplit = line.split("\t");
		// form.getRules().add(new Rule(
		// "ruleText"+ruleNumber,
		// new ExpressionChain(
		// new ExpressionValueCustomVariable(this.getCategoryFromForm(form,
		// lineSplit[0]), customVarCategory),
		// new ExpressionOperatorLogic(AvailableOperator.EQUALS),
		// new ExpressionValueNumber(Double.parseDouble(lineSplit[1]))),
		// new ExpressionChain(
		// new ExpressionValueCustomVariable(this.getCategoryFromForm(form,
		// lineSplit[0]), customVarTextCategory),
		// new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
		// new ExpressionValueString(lineSplit[2]))));
		// ruleNumber++;
		// }

		Diagram mainDiagram = new Diagram("main");
		// Start
		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());
		// Load BaseTable
		DiagramTable diagramTableRuleNode = new DiagramTable();
		diagramTableRuleNode.setTable(baseTableRule);
		diagramTableRuleNode.setJointjsId(IdGenerator.createId());
		diagramTableRuleNode.setType(DiagramObjectType.TABLE);
		Node nodeTable = new Node(diagramTableRuleNode.getJointjsId());
		// Creation of a subdiagram with the rules modifying the questions
		DiagramChild subQuestionRuleDiagramNode = new DiagramChild();
		subQuestionRuleDiagramNode.setChildDiagram(this.createQuestionExceptionRulesSubdiagram(form));
		subQuestionRuleDiagramNode.setJointjsId(IdGenerator.createId());
		subQuestionRuleDiagramNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeQuestionRuleDiagram = new Node(subQuestionRuleDiagramNode.getJointjsId());
		// Creation of a subdiagram with all the expressions to calculate the
		// minimum score of the categories
		DiagramChild subExpressionDiagramNode = new DiagramChild();
		subExpressionDiagramNode.setChildDiagram(this.createExpressionsSubdiagram(form));
		subExpressionDiagramNode.setJointjsId(IdGenerator.createId());
		subExpressionDiagramNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeSubExpressionDiagram = new Node(subExpressionDiagramNode.getJointjsId());
		// Creation of a subdiagram with the rules modifying the categories
		DiagramChild subCategoryRuleDiagramNode = new DiagramChild();
		subCategoryRuleDiagramNode.setChildDiagram(this.createCategoryExceptionRulesSubdiagram(form));
		subCategoryRuleDiagramNode.setJointjsId(IdGenerator.createId());
		subCategoryRuleDiagramNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeSubCategoryRuleDiagram = new Node(subCategoryRuleDiagramNode.getJointjsId());
		// End
		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startTable = new DiagramLink(nodeSource, nodeTable);
		startTable.setJointjsId(IdGenerator.createId());
		startTable.setType(DiagramObjectType.LINK);
		DiagramLink tableQuestion = new DiagramLink(nodeTable, nodeQuestionRuleDiagram);
		tableQuestion.setJointjsId(IdGenerator.createId());
		tableQuestion.setType(DiagramObjectType.LINK);
		DiagramLink questionExpression = new DiagramLink(nodeQuestionRuleDiagram, nodeSubExpressionDiagram);
		questionExpression.setJointjsId(IdGenerator.createId());
		questionExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionCategory = new DiagramLink(nodeSubExpressionDiagram, nodeSubCategoryRuleDiagram);
		expressionCategory.setJointjsId(IdGenerator.createId());
		expressionCategory.setType(DiagramObjectType.LINK);
		DiagramLink categoryEnd = new DiagramLink(nodeSubCategoryRuleDiagram, nodeSink);
		expressionCategory.setJointjsId(IdGenerator.createId());
		expressionCategory.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramTableRuleNode);
		mainDiagram.addDiagramObject(subQuestionRuleDiagramNode);
		mainDiagram.addDiagramObject(subExpressionDiagramNode);
		mainDiagram.addDiagramObject(subCategoryRuleDiagramNode);
		mainDiagram.addDiagramObject(diagramEndNode);

		mainDiagram.addDiagramObject(startTable);
		mainDiagram.addDiagramObject(tableQuestion);
		mainDiagram.addDiagramObject(questionExpression);
		mainDiagram.addDiagramObject(expressionCategory);
		mainDiagram.addDiagramObject(categoryEnd);

		form.addDiagram(mainDiagram);

		return form;
	}

	private Diagram createQuestionExceptionRulesSubdiagram(Form form) {
		Diagram subDiagram = new Diagram("ruleQuestionDiagram");
		for (Rule rule : this.questionExceptionRules) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramRule diagramRule = new DiagramRule();
			diagramRule.setRule(rule);
			diagramRule.setJointjsId(IdGenerator.createId());
			diagramRule.setType(DiagramObjectType.RULE);
			Node nodeRule = new Node(diagramRule.getJointjsId());

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
			subDiagram.addDiagramObject(diagramRule);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}

	private Diagram createExpressionsSubdiagram(Form form) {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : form.getExpressionChain()) {

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

	private Diagram createCategoryExceptionRulesSubdiagram(Form form) {
		Diagram subDiagram = new Diagram("ruleCategoryDiagram");
		for (Rule rule : this.categoryExceptionRules) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramRule diagramRule = new DiagramRule();
			diagramRule.setRule(rule);
			diagramRule.setJointjsId(IdGenerator.createId());
			diagramRule.setType(DiagramObjectType.RULE);
			Node nodeRule = new Node(diagramRule.getJointjsId());

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
			subDiagram.addDiagramObject(diagramRule);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}

	private Category getCategoryFromForm(Form form, String catName) {
		for (TreeObject child : form.getChildren()) {
			if ((child instanceof Category) && child.getName().equals(catName)) {
				return (Category) child;
			}
		}
		return null;
	}

	private Question getQuestionFromCategory(Category category, String questionName) {
		for (Question question : category.getQuestions()) {
			if (question.getName().equals(questionName)) {
				return question;
			}
		}
		return null;
	}
}
