package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class TableRuleTest extends AbstractTransactionalTestNGSpringContextTests {

	private final static String DUMMY_FORM = "Form with table rules";
//	private final static String CONDITION_EXPRESSION = "Question=Question1 AND Answer=Yes";
//	private final static String BASIC_ACTION = "System.out.println( \"Hello world!\");";

	@Autowired
	private ITableRuleDao tableRuleDao;

	@Autowired
	private ITableRuleRowDao tableRuleRowDao;

	@Autowired
	private IFormDao formDao;

	@Test(groups = { "tableRulesDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(tableRuleDao.getRowCount(), 0);
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyTableRule() throws NotValidFormException, FieldTooLongException {
		Form basicForm = new Form();
		basicForm.setName(DUMMY_FORM);

		TableRule tableRule = new TableRule();

		TableRuleRow tableRuleRow = new TableRuleRow();
		tableRule.getRules().add(tableRuleRow);
		tableRuleDao.makePersistent(tableRule);
		basicForm.getTableRules().add(tableRule);

		formDao.makePersistent(basicForm);

		Assert.assertEquals(tableRuleDao.getRowCount(), 1);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 1);

		List<TableRule> tableRules = tableRuleDao.getAll();
		Assert.assertEquals(tableRules.size(), 1);
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = "storeDummyTableRule")
	public void storeTableRule() throws NotValidChildException, NotValidExpression, FieldTooLongException {
		// Define form.
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v2");

		Category category = new Category();
		form.addChild(category);

		Question question = new Question();
		question.setName("Select one answer:");
		question.setAnswerType(AnswerType.RADIO);
		category.addChild(question);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question.addChild(answer3);

		// Define rule elements
//		TableRule tableRule = new TableRule();
//		TableRuleRow tableRuleRow = new TableRuleRow();

		//		ActionString action = new ActionString();
		//		action.setExpression(BASIC_ACTION);
		//		tableRuleRow.addAction(action);
		//
		//		// Set into the rule.
		//		ExpressionChain condition = new ExpressionChain();
		//		//		condition.addExpression(new ExpressionValueString(CONDITION_EXPRESSION));
		//		condition.addExpression(new ExpressionValueTreeObjectReference(question));
		//		tableRuleRow.getConditions().add(condition);
		//		tableRule.getRules().add(tableRuleRow);
		//
		//		form.getTableRules().add(tableRule);
		//
		//		formDao.makePersistent(form);
		//
		//		Form retrievedForm = formDao.getForm(DUMMY_FORM + "_v2");
		//		Assert.assertEquals(retrievedForm.getTableRules().size(), 1);
		//		Assert.assertEquals(tableRuleDao.getRowCount(), 2);
		//		Assert.assertEquals(tableRuleRowDao.getRowCount(), 2);
		//		Assert.assertEquals(retrievedForm.getTableRules().get(0).getRules().get(0).getActions().get(0).getExpression(),
		//				BASIC_ACTION);
		//		Assert.assertEquals(
		//				((ExpressionValueTreeObjectReference)((ExpressionChain)retrievedForm.getTableRules().get(0).getRules().get(0).getConditions().get(0)).getExpressions().get(0)).getReference(), question);
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = { "storeTableRule" })
	public void removeTableRules() {
		formDao.removeAll();
		Assert.assertEquals(formDao.getRowCount(), 0);
		Assert.assertEquals(tableRuleDao.getRowCount(), 0);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 0);
	}
}
