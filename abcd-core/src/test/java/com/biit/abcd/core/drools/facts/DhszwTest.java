package com.biit.abcd.core.drools.facts;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryNameWithoutTranslation;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonCategoryTranslator;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
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
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class DhszwTest {

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
	public void updateQuestionsScore() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createDhszwForm();
		formDrools.parse(vaadinForm);
		formDrools.go(this.form);
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
	 */
	private Form createDhszwForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException {

		// Create the form
		Form form = new Form("DhszwForm");
		// Create a category
		Category category = new Category("Financiën");
		form.addChild(category);

		// Create the questions
		List<String> questionsName = Arrays.asList("Financien.Inkomen", "Financien.Bron", "Financien.Schulden",
				"Financien.Uitgaven", "Financien.Beheer");
		List<TreeObject> questionList = new ArrayList<TreeObject>(questionsName.size());
		for (String questionName : questionsName) {
			questionList.add(new Question(questionName));
		}
		category.addChildren(questionList);

		// Create the answers
		List<Integer> answersNumber = Arrays.asList(4, 3, 5, 4, 5);
		List<String> answersName = Arrays.asList("Financien.Inkomen.RuimVoldoende",
				"Financien.Inkomen.NetVoldoendeZonderUitkering", "Financien.Inkomen.Onvoldoende",
				"Financien.Inkomen.Geen", "Inkomsten.Bron.Werk", "Inkomsten.Bron.Uitkering", "Inkomsten.Bron.Anders",
				"Financien.Schulden.Sparen", "Financien.Schulden.NietSparenGeenSchulden", "Financien.Schulden.NemenAf",
				"Financien.Schulden.BlijvenGelijk", "Financien.Schulden.NemenToe",
				"Financien.Uitgaven.OnverstandigGeen", "Financien.Uitgaven.OnverstandigWelVeroorloven",
				"Financien.Uitgaven.OnverstandigSomsNietVeroorloven", "Financien.Uitgaven.OnverstandigRegelmatigNiet",
				"Financien.Beheer.VolledigInzicht", "Financien.Beheer.RedelijkInzicht",
				"Financien.Beheer.WelEensVerrast", "Financien.Beheer.VeelVerrassingen",
				"Financien.Beheer.GeenOplossing");

		int answerNumber = 0;
		for (int iQuestion = 0; iQuestion < questionList.size(); iQuestion++) {
			TreeObject question = questionList.get(iQuestion);
			for (int iAnswer = 0; iAnswer < answersNumber.get(iQuestion); iAnswer++) {
				question.addChild(new Answer(answersName.get(answerNumber)));
				answerNumber++;
			}
		}

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarTextCategory = new CustomVariable(form, "cScoreText", CustomVariableType.STRING,
				CustomVariableScope.CATEGORY);

		// Create the tableRule
		TableRule tableRule = new TableRule("BaseTable");
		List<Double> values = Arrays.asList(5., 4., 2., 1., 5., 3., 1., 5., 4., 4., 3., 2., 5., 5., 3., 2., 5., 4., 3.,
				2., 1.);

		int valueIndex = 0;
		for (TreeObject question : questionList) {
			for (TreeObject answer : question.getChildren()) {
				// Create and add row to table rule
				tableRule.getRules().add(
						new TableRuleRow(new ExpressionValueTreeObjectReference(question), new ExpressionChain(
								new ExpressionValueTreeObjectReference(answer)), new ExpressionChain(
								new ExpressionValueCustomVariable(question, customVarQuestion),
								new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(
										values.get(valueIndex)))));
				valueIndex++;
			}
		}
		// Add the rows and the table to the form
		form.getTableRules().add(tableRule);

		ExpressionChain testExpressionChain = new ExpressionChain("FinancienScore");
		testExpressionChain.addExpression(new ExpressionValueCustomVariable(category, customVarCategory));
		testExpressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		testExpressionChain.addExpression(new ExpressionFunction(AvailableFunction.MIN));
		int i = 0;
		for (TreeObject question : questionList) {
			testExpressionChain.addExpression(new ExpressionValueCustomVariable(question, customVarQuestion));
			if (i < (questionList.size() - 1)) {
				// So the last expression of the rule before the bracket is not
				// a comma
				testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
			}
			i++;
		}
		testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		form.getExpressionChain().add(testExpressionChain);


		// Create new rules to set the text based on the score of the category
		List<String> ruleNames = Arrays.asList(
				"FinancienText1",
				"FinancienText2",
				"FinancienText3",
				"FinancienText4",
				"FinancienText5");
		List<Double> ruleScores = Arrays.asList(1., 2., 3., 4., 5.);
		List<String> messages = Arrays.asList(
				"Geen inkomsten. Hoge, groeiende schulden.",
				"Onvoldoende inkomsten en/of spontaan of ongepast uitgeven. Groeiende schulden.",
				"Komt met inkomsten aan basisbehoeften tegemoet en/of gepast uitgeven. Eventuele schulden zijn ten minste stabiel en/of bewindvoering/inkomstenbeheer.",
				"Komt aan basis behoeften tegemoet zonder uitkering. Beheert evntuele schulden zelf en deze verminderen.",
				"Inkomsten zijn ruim voldoende, goed financieel beheer. Heeft met inkomen mogeijkheid om te sparen.");
		for(int iMessage=0; iMessage<messages.size(); iMessage++){
			form.getRules().add(new Rule(
					ruleNames.get(iMessage),
					new ExpressionChain(
							new ExpressionValueCustomVariable(category, customVarCategory),
							new ExpressionOperatorLogic(AvailableOperator.EQUALS),
							new ExpressionValueNumber(ruleScores.get(iMessage))),
					new ExpressionChain(
							new ExpressionValueCustomVariable(category, customVarTextCategory),
							new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
							new ExpressionValueString(messages.get(iMessage)))));
		}

		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
