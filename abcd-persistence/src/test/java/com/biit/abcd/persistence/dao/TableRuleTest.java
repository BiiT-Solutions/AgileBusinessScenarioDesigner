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
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "tableRulesDao" })
public class TableRuleTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with table rules";
	private final static String TABLE_RULE_FORM = "Table Rule Form";
	private final static String CONDITION_EXPRESSION = "Question=Question1 AND Answer=Yes";

	@Autowired
	private ITableRuleDao tableRuleDao;

	@Autowired
	private ITableRuleRowDao tableRuleRowDao;

	@Autowired
	private IFormDao formDao;

	@Test
	public void storeDummyTableRule() throws NotValidFormException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);

		TableRule tableRule = new TableRule();

		TableRuleRow tableRuleRow = new TableRuleRow();
		tableRule.getRules().add(tableRuleRow);
		tableRuleDao.makePersistent(tableRule);
		form.getTableRules().add(tableRule);

		formDao.makePersistent(form);

		Assert.assertEquals(tableRuleDao.getRowCount(), 1);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), 1);

		List<TableRule> tableRules = tableRuleDao.getAll();
		Assert.assertEquals(tableRules.size(), 1);

		formDao.makeTransient(form);
	}

	@Test
	public void storeTableRule() throws NotValidChildException, NotValidExpression, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBeRemovedException {
		// Define form.
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM + "_v2");

		Category category = new Category();
		form.addChild(category);

		Question question = new Question();
		question.setName("Question1");
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

		formDao.makeTransient(form);
	}

	@Test
	public void storeFormTableRule() throws NotValidChildException, NotValidExpression, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(TABLE_RULE_FORM);

		Category category1 = new Category();
		category1.setName("Category1");
		form.addChild(category1);

		Group group1 = new Group();
		group1.setName("Group1");
		category1.addChild(group1);

		Question question1 = new Question();
		question1.setName("Question1");
		group1.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		group1.addChild(question2);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question1.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question1.addChild(answer2);

		TableRule tableRule = new TableRule();

		TableRuleRow tableRuleRow = new TableRuleRow();

		ExpressionChain condition = new ExpressionChain();
		condition.addExpression(new ExpressionValueString(CONDITION_EXPRESSION));
		tableRuleRow.getConditions().addExpression(condition);

		tableRule.getRules().add(tableRuleRow);

		form.getTableRules().add(tableRule);

		formDao.makePersistent(form);
		Form retrievedForm = (Form) formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getTableRules().size(), 1);

		Assert.assertEquals(tableRuleDao.getRowCount(), 1);
		formDao.makeTransient(form);
	}
}
