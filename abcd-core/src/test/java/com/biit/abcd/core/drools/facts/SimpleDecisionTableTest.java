package com.biit.abcd.core.drools.facts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import junit.framework.Assert;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryNameWithoutTranslation;
import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
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

public class SimpleDecisionTableTest {

	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private final static String DOCUMENT_ID = "7912c3f8b7328253dd7647cf507455a795367f49";

	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	// @Test(groups = { "orbeon" })
	// public void getXml() throws MalformedURLException, DocumentException {
	// orbeonImporter = new OrbeonSubmittedAnswerImporter();
	// xmlText = OrbeonImporter.getXml(APP, FORM, DOCUMENT_ID);
	// Assert.assertNotNull(xmlText);
	// };

	@Test(groups = { "orbeon" })
	public void readXml() throws DocumentException, IOException {
		this.form = new SubmittedForm(APP, FORM);
		// Charset could be StandardCharsets.UTF_8, but we leave the default one
		String xmlFile = readFile("./src/test/resources/decisionTableTest.xml", Charset.defaultCharset());
		this.orbeonImporter.readXml(xmlFile, this.form);
		Assert.assertNotNull(this.form);
		Assert.assertFalse(this.form.getCategories().isEmpty());
	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void translateFormCategories() throws MalformedURLException, DocumentException,
			CategoryNameWithoutTranslation {
		OrbeonCategoryTranslator.getInstance().readXml(this.form);
	}

	@Test(groups = { "rules" }, dependsOnMethods = { "readXml" })
	public void updateQuestionsScore() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, RuleInvalidException {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = this.createSimpleTestForm();
		formDrools.parse(vaadinForm);
		formDrools.go(this.form);

		try {
			Assert.assertEquals("RuimVoldoende", this.form.getCategory("Financien").getQuestion("Inkomen").getValue());
			Assert.assertEquals(
					5.0,
					((SubmittedForm) this.form).getVariableValue(
							this.form.getCategory("Financien").getQuestion("Inkomen"), "qScore"));
		} catch (QuestionDoesNotExistException | CategoryDoesNotExistException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the form structure. Form used to create the drools rules
	 *
	 * @return
	 * @throws NotValidChildException
	 * @throws NotValidOperatorInExpression
	 */
	private Form createSimpleTestForm() throws NotValidChildException, NotValidOperatorInExpression {
		Form form = new Form();
		form.setName("Test form");
		Category category = new Category();
		category.setName("Financien");
		form.addChild(category);

		Question question = new Question();
		question.setName("Inkomen");
		category.addChild(question);

		Answer answer11 = new Answer();
		answer11.setName("RuimVoldoende");
		question.addChild(answer11);

		TableRule tableRule = new TableRule();
		TableRuleRow tableRuleRow1 = new TableRuleRow();

		// Create question
		ExpressionValueTreeObjectReference question1Row1 = new ExpressionValueTreeObjectReference();
		question1Row1.setReference(question);
		// Create answer
		ExpressionChain answer1Row1ExpChain = new ExpressionChain();
		ExpressionValueTreeObjectReference answer1Row1 = new ExpressionValueTreeObjectReference();
		answer1Row1.setReference(answer11);
		answer1Row1ExpChain.addExpression(answer1Row1);
		// Create action
		ExpressionChain actionRow1ExpChain = new ExpressionChain();
		ExpressionValueCustomVariable actionElement1Row1 = new ExpressionValueCustomVariable();
		actionElement1Row1.setReference(question);
		CustomVariable customVar = new CustomVariable(form, "qScore", CustomVariableType.STRING,
				CustomVariableScope.QUESTION);
		actionElement1Row1.setVariable(customVar);
		ExpressionOperatorMath actionElement2Row1 = new ExpressionOperatorMath();
		actionElement2Row1.setValue(AvailableOperator.ASSIGNATION);
		ExpressionValueNumber actionElement3Row1 = new ExpressionValueNumber(5.0);
		actionRow1ExpChain.addExpression(actionElement1Row1);
		actionRow1ExpChain.addExpression(actionElement2Row1);
		actionRow1ExpChain.addExpression(actionElement3Row1);
		// Add to table rule
		tableRuleRow1.getConditions().add(question1Row1);
		tableRuleRow1.getConditions().add(answer1Row1ExpChain);
		tableRuleRow1.getAction().setExpressions(actionRow1ExpChain.getExpressions());

		tableRule.getRules().add(tableRuleRow1);
		form.getTableRules().add(tableRule);
		return form;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
