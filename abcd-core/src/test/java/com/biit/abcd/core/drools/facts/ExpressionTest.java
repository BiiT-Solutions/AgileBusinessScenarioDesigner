package com.biit.abcd.core.drools.facts;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
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
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
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

public class ExpressionTest {


	private final static String APP = "Application1";
	private final static String FORM = "Form1";

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		String xmlFile = readFile("./src/test/resources/expressionTest.xml", Charset.defaultCharset());
		this.orbeonImporter.readXml(xmlFile, this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "readXml" })
	public void updateQuestionsScore() throws ExpressionInvalidException, NotValidChildException, NotValidOperatorInExpression, RuleInvalidException {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createSimpleTestForm();
		formDrools.parse(vaadinForm);
		formDrools.go(this.form);
	}

	/**
	 * Create the form structure.
	 * Creates to simple assignation rules in the table rule and one expression with max func
	 * Form used to create the drools rules
	 *
	 * @return
	 * @throws NotValidChildException
	 * @throws NotValidOperatorInExpression
	 */
	private Form createSimpleTestForm() throws NotValidChildException, NotValidOperatorInExpression {

		// Create the form
		Form form = new Form();
		form.setName("TestForm");
		// Create a category
		Category category = new Category();
		category.setName("C1");
		form.addChild(category);

		// Create the questions
		Question question1 = new Question();
		question1.setName("C1Q1");
		category.addChild(question1);

		Question question2 = new Question();
		question2.setName("C1Q2");
		category.addChild(question2);

		// Create the answers
		Answer answer11 = new Answer();
		answer11.setName("A11");
		question1.addChild(answer11);

		Answer answer22 = new Answer();
		answer22.setName("A22");
		question2.addChild(answer22);

		// Create the custom variables
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.STRING, CustomVariableScope.QUESTION);
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.STRING, CustomVariableScope.CATEGORY);

		// Create the tableRule
		TableRule tableRule = new TableRule();
		tableRule.setName("tableRule1");
		// Create the rows
		TableRuleRow tableRuleRow1 = new TableRuleRow();
		TableRuleRow tableRuleRow2 = new TableRuleRow();

		// Create row1 question
		ExpressionValueTreeObjectReference question1Row1 = new ExpressionValueTreeObjectReference();
		question1Row1.setReference(question1);
		// Create row1 answer
		ExpressionChain answer1Row1ExpChain = new ExpressionChain();
		ExpressionValueTreeObjectReference answer1Row1 = new ExpressionValueTreeObjectReference();
		answer1Row1.setReference(answer11);
		answer1Row1ExpChain.addExpression(answer1Row1);
		// Create row1 action
		ExpressionChain actionRow1ExpChain = new ExpressionChain();
		ExpressionValueCustomVariable actionElement1Row1 = new ExpressionValueCustomVariable();
		actionElement1Row1.setReference(question1);
		actionElement1Row1.setVariable(customVarQuestion);
		ExpressionOperatorMath actionElement2Row1 = new ExpressionOperatorMath();
		actionElement2Row1.setValue(AvailableOperator.ASSIGNATION);
		ExpressionValueNumber actionElement3Row1 = new ExpressionValueNumber(5.0);
		actionRow1ExpChain.addExpression(actionElement1Row1);
		actionRow1ExpChain.addExpression(actionElement2Row1);
		actionRow1ExpChain.addExpression(actionElement3Row1);
		// Add row1 to table rule
		tableRuleRow1.getConditions().add(question1Row1);
		tableRuleRow1.getConditions().add(answer1Row1ExpChain);
		tableRuleRow1.getAction().setExpressions(actionRow1ExpChain.getExpressions());

		// Create row2 question
		ExpressionValueTreeObjectReference question1Row2 = new ExpressionValueTreeObjectReference();
		question1Row2.setReference(question2);
		// Create row1 answer
		ExpressionChain answer1Row2ExpChain = new ExpressionChain();
		ExpressionValueTreeObjectReference answer1Row2 = new ExpressionValueTreeObjectReference();
		answer1Row2.setReference(answer22);
		answer1Row2ExpChain.addExpression(answer1Row2);
		// Create row1 action
		ExpressionChain actionRow2ExpChain = new ExpressionChain();
		ExpressionValueCustomVariable actionElement1Row2 = new ExpressionValueCustomVariable();
		actionElement1Row2.setReference(question2);
		actionElement1Row2.setVariable(customVarQuestion);
		ExpressionOperatorMath actionElement2Row2 = new ExpressionOperatorMath();
		actionElement2Row2.setValue(AvailableOperator.ASSIGNATION);
		ExpressionValueNumber actionElement3Row2 = new ExpressionValueNumber(10.0);
		actionRow2ExpChain.addExpression(actionElement1Row2);
		actionRow2ExpChain.addExpression(actionElement2Row2);
		actionRow2ExpChain.addExpression(actionElement3Row2);
		// Add row1 to table rule
		tableRuleRow2.getConditions().add(question1Row2);
		tableRuleRow2.getConditions().add(answer1Row2ExpChain);
		tableRuleRow2.getAction().setExpressions(actionRow2ExpChain.getExpressions());

		// Add the rows and the table to the form
		tableRule.getRules().add(tableRuleRow1);
		tableRule.getRules().add(tableRuleRow2);
		form.getTableRules().add(tableRule);

		ExpressionChain testExpressionChain = new ExpressionChain();
		testExpressionChain.setName("exp1");
		testExpressionChain.addExpression(new ExpressionValueCustomVariable(category, customVarCategory));
		ExpressionOperatorMath eom = new ExpressionOperatorMath();
		eom.setValue(AvailableOperator.ASSIGNATION);
		testExpressionChain.addExpression(eom);
		ExpressionFunction ef = new ExpressionFunction();
		ef.setValue(AvailableFunction.MIN);
		testExpressionChain.addExpression(ef);
		testExpressionChain.addExpression(new ExpressionValueCustomVariable(question1, customVarQuestion));
		ExpressionSymbol esc = new ExpressionSymbol();
		esc.setValue(AvailableSymbol.COMMA);
		testExpressionChain.addExpression(esc);
		testExpressionChain.addExpression(new ExpressionValueCustomVariable(question2, customVarQuestion));
		ExpressionSymbol eslb = new ExpressionSymbol();
		eslb.setValue(AvailableSymbol.RIGHT_BRACKET);
		testExpressionChain.addExpression(eslb);

		form.getExpressionChain().add(testExpressionChain);

		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
