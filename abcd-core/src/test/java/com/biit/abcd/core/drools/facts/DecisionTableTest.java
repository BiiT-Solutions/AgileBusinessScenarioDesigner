package com.biit.abcd.core.drools.facts;

import java.net.MalformedURLException;

import junit.framework.Assert;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonImporter;
import com.biit.abcd.core.drools.facts.inputform.orbeon.OrbeonSubmittedAnswerImporter;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class DecisionTableTest {

	private final static String APP = "Application1";
	private final static String FORM = "Form1";
	private final static String DOCUMENT_ID = "7912c3f8b7328253dd7647cf507455a795367f49";
	//	private final static Integer QUESTION_ORDER = 24;

	private String xmlText;
	private ISubmittedForm form;
	private OrbeonSubmittedAnswerImporter orbeonImporter = new OrbeonSubmittedAnswerImporter();

	@Test(groups = { "orbeon" })
	public void getXml() throws MalformedURLException, DocumentException {
		orbeonImporter = new OrbeonSubmittedAnswerImporter();
		xmlText = OrbeonImporter.getXml(APP, FORM, DOCUMENT_ID);
		Assert.assertNotNull(xmlText);
	};
	@Test(groups = { "orbeon" }, dependsOnMethods = { "getXml" })
	public void readXml() throws MalformedURLException, DocumentException {
		form = new SubmittedForm(APP, FORM);
		orbeonImporter.readXml(xmlText, form);
		Assert.assertNotNull(form);
		Assert.assertFalse(form.getCategories().isEmpty());
	}

	//	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	//	public void translateFormCategories() throws MalformedURLException, DocumentException, CategoryNameWithoutTranslation {
	//		OrbeonCategoryTranslator.getInstance().readXml(form);
	//	}

	@Test(groups = { "orbeon" }, dependsOnMethods = { "readXml" })
	public void updateQuestionsScore() throws ExpressionInvalidException, NotValidChildException, NotValidOperatorInExpression {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		Form vaadinForm = createSimpleTestForm();
		formDrools.parse(vaadinForm);
		formDrools.go(form);

		try {
			Assert.assertEquals("RuimVoldoende", form.getCategory("Financien").getQuestion("Inkomen").getValue());
			Assert.assertEquals(5.0, ((SubmittedForm) form).getVariableValue(form.getCategory("Financien").getQuestion("Inkomen"), "qScore"));
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
		ExpressionValueFormCustomVariable actionElement1Row1 = new ExpressionValueFormCustomVariable();
		actionElement1Row1.setReference(question);
		CustomVariable customVar = new CustomVariable(form, "qScore", CustomVariableType.STRING, CustomVariableScope.QUESTION);
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

}
