package com.biit.abcd.core.drools.facts;

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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class DhszwDiagTest {

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
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException, IOException, CategoryDoesNotExistException {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createCompleteDhszwForm();
		formDrools.parse(vaadinForm);
		formDrools.go(this.form);
		Assert.assertEquals("Geen contact met politie. Geen strafblad.", ((SubmittedForm) this.form).getVariableValue(this.form.getCategory("Justitie"), "cScoreText"));
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
	private Form createCompleteDhszwForm() throws NotValidChildException, NotValidOperatorInExpression,
			ChildrenNotFoundException, FieldTooLongException, IOException {

		// Create the form
		Form form = new Form("DhszwForm");

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarTextCategory = new CustomVariable(form, "cScoreText", CustomVariableType.STRING,
				CustomVariableScope.CATEGORY);

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
					// not
					// a comma
					testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
				}
				i++;
			}
			testExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			form.getExpressionChain().add(testExpressionChain);
			accumExp++;
		}

		// Creation of the result rules
		int ruleNumber = 1;
		for(String line: Files.readAllLines(Paths.get("./src/test/resources/tables/returnedText"), StandardCharsets.UTF_8)) {
			// [0] = category, [1] = score, [2] = text
			String[] lineSplit = line.split("\t");
			form.getRules().add(new Rule(
					"ruleText"+ruleNumber,
					new ExpressionChain(
							new ExpressionValueCustomVariable(this.getCategoryFromForm(form, lineSplit[0]), customVarCategory),
							new ExpressionOperatorLogic(AvailableOperator.EQUALS),
							new ExpressionValueNumber(Double.parseDouble(lineSplit[1]))),
					new ExpressionChain(
							new ExpressionValueCustomVariable(this.getCategoryFromForm(form, lineSplit[0]), customVarTextCategory),
							new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
							new ExpressionValueString(lineSplit[2]))));
			ruleNumber++;
		}
		return form;
	}

	public Category getCategoryFromForm(Form form, String catName){
		for(TreeObject child : form.getChildren()){
			if((child instanceof Category) && child.getName().equals(catName)){
				return (Category) child;
			}
		}
		return null;
	}

	private Question getQuestionFromCategory(Category category, String questionName){
		for(Question question : category.getQuestions()){
			if(question.getName().equals(questionName)) {
				return question;
			}
		}
		return null;
	}
}
