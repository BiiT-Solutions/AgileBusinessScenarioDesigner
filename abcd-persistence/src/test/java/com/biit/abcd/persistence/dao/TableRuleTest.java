package com.biit.abcd.persistence.dao;

import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;
import com.biit.abcd.persistence.entity.rules.Action;
import com.biit.abcd.persistence.entity.rules.Condition;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class TableRuleTest extends AbstractTransactionalTestNGSpringContextTests {

	private final static String DUMMY_FORM = "Form with table rules";
	private final static String BASIC_ACTION = "System.out.println( \"Hello world!\");";

	@Autowired
	private ITableRuleDao tableRuleDao;

	@Autowired
	private ITableRuleRowDao tableRuleRowDao;

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IConditionDao conditionDao;

	@Autowired
	private IActionDao actionDao;

	@Test(groups = { "tableRulesDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(tableRuleDao.getRowCount(), 0);
	}

	/**
	 * Returns the question object of the retrieved hashmap from database.
	 * 
	 * @param map
	 * @param originalQuestion
	 * @return
	 */
	private Question getHibernateQuestion(Map<Question, Condition> map, Question originalQuestion) {
		if (originalQuestion.getId() == null) {
			return null;
		}
		for (Question question : map.keySet()) {
			if (question.getId() == originalQuestion.getId()) {
				return question;
			}
		}
		return null;
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyTableRule() throws NotValidFormException {
		Form basicForm = new Form();
		basicForm.setName(DUMMY_FORM);
		formDao.makePersistent(basicForm);

		TableRule tableRule = new TableRule(basicForm);

		TableRuleRow tableRuleRow = new TableRuleRow();
		tableRule.getRules().add(tableRuleRow);
		tableRuleDao.makePersistent(tableRule);

		Assert.assertEquals(tableRuleDao.getRowCount(), 1);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 1);

		List<TableRule> tableRules = tableRuleDao.getAll();
		Assert.assertEquals(tableRules.get(0).getForm().getName(), DUMMY_FORM);
		Assert.assertEquals(tableRuleDao.getFormTableRules(basicForm).size(), 1);
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = "storeDummyTableRule")
	public void storeTableRule() throws NotValidChildException {
		// Define form.
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v2");

		Category category = new Category();
		form.addChild(category);

		Question question = new Question();
		question.setName("Put your age:");
		question.setAnswerFormat(AnswerFormat.NUMBER);
		question.setAnswerType(AnswerType.INPUT);
		category.addChild(question);

		formDao.makePersistent(form);

		// Define rule elements
		TableRule tableRule = new TableRule(form);

		TableRuleRow tableRuleRow = new TableRuleRow();

		Action action = new Action();
		action.setExpression(BASIC_ACTION);
		tableRuleRow.addAction(action);

		Condition condition = new Condition();
		// Set into the rule.
		tableRuleRow.putCondition(question, condition);
		tableRule.getRules().add(tableRuleRow);

		tableRuleDao.makePersistent(tableRule);

		List<TableRule> ruleTablesOfForm = tableRuleDao.getFormTableRules(form);
		Assert.assertEquals(ruleTablesOfForm.size(), 1);
		Assert.assertEquals(tableRuleDao.getRowCount(), 2);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 2);
		Assert.assertEquals(ruleTablesOfForm.get(0).getRules().get(0).getActions().get(0).getExpression(), BASIC_ACTION);
		Assert.assertEquals(
				ruleTablesOfForm.get(0).getRules().get(0).getConditions()
						.get(getHibernateQuestion(ruleTablesOfForm.get(0).getRules().get(0).getConditions(), question))
						.getId(), condition.getId());
	}

	@Test(groups = { "tableRulesDao" }, dependsOnMethods = { "storeTableRule" })
	public void removeTableRules() {
		tableRuleDao.removeAll();
		Assert.assertEquals(tableRuleDao.getRowCount(), 0);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 0);
		Assert.assertEquals(conditionDao.getRowCount(), 0);
		Assert.assertEquals(actionDao.getRowCount(), 0);

		formDao.removeAll();
		Assert.assertEquals(formDao.getRowCount(), 0);
	}
}
