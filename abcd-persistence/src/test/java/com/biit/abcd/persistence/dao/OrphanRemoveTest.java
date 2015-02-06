package com.biit.abcd.persistence.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Test that one to one entities are removed correctly using orphanremove=true
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
// @TransactionConfiguration(defaultRollback = true)
@Test(groups = { "orphan" })
public class OrphanRemoveTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form with Orphan";
	private final static String FULL_FORM = "Full Form";
	private final static String DUMMY_DIAGRAM = "Dummy DIAGRAM";

	@Autowired
	private IRuleDao ruleDao;

	@Autowired
	private IExpressionChainDao expressionChainDao;

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IDiagramDao diagramDao;

	@Autowired
	private IExpressionValueTreeObjectReferenceDao expressionValueTreeObjectReferenceDao;

	@Test
	public void removeBasicRule() throws UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementCannotBeRemovedException {
		int prevRules = ruleDao.getRowCount();
		int prevExpressions = expressionChainDao.getRowCount();
		// Rule already has two chains inside.
		Rule rule = new Rule();
		ruleDao.makePersistent(rule);
		Assert.assertEquals(ruleDao.getRowCount(), prevRules + 1);
		Assert.assertEquals(expressionChainDao.getRowCount(), prevExpressions + 2);
		ruleDao.makeTransient(rule);
		Assert.assertEquals(ruleDao.getRowCount(), prevRules);
		Assert.assertEquals(expressionChainDao.getRowCount(), prevExpressions);
	}

	@Test
	public void removeRuleOfForm() throws FieldTooLongException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementCannotBeRemovedException, ElementCannotBePersistedException {
		int prevRules = ruleDao.getRowCount();
		int prevExpressions = expressionChainDao.getRowCount();

		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		// Rule already has two chains inside.
		Rule rule = new Rule();
		form.getRules().add(rule);

		formDao.makePersistent(form);
		Assert.assertEquals(ruleDao.getRowCount(), prevRules + 1);
		Assert.assertEquals(expressionChainDao.getRowCount(), prevExpressions + 2);

		formDao.makeTransient(form);
		Assert.assertEquals(ruleDao.getRowCount(), prevRules);
		Assert.assertEquals(expressionChainDao.getRowCount(), prevExpressions);
	}

	@Test
	public void removeDiagram() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException,
			ElementCannotBeRemovedException {
		int prevForm = formDao.getRowCount();
		int prevDiagram = diagramDao.getRowCount();
		int prevExpressions = expressionValueTreeObjectReferenceDao.getRowCount();

		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(FULL_FORM + "1");

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

		Question question3 = new Question();
		question3.setName("Question3");
		group1.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question1.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question1.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question1.addChild(answer3);

		Diagram diagram = new Diagram(DUMMY_DIAGRAM);
		DiagramSource start = new DiagramSource();
		diagram.addDiagramObject(start);
		form.addDiagram(diagram);

		DiagramFork fork = new DiagramFork();
		ExpressionValueTreeObjectReference expression = new ExpressionValueTreeObjectReference(question1);
		fork.setReference(expression);
		diagram.addDiagramObject(fork);

		DiagramLink link1 = new DiagramLink();
		ExpressionChain selectAnswer1 = new ExpressionChain();
		selectAnswer1.addExpression(new ExpressionValueTreeObjectReference(answer1));
		link1.setExpressionChain(selectAnswer1);
		diagram.addDiagramObject(link1);

		DiagramLink link2 = new DiagramLink();
		ExpressionChain selectAnswer2 = new ExpressionChain();
		selectAnswer2.addExpression(new ExpressionValueTreeObjectReference(answer2));
		link2.setExpressionChain(selectAnswer2);
		diagram.addDiagramObject(link2);

		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), prevForm + 1);
		Assert.assertEquals(diagramDao.getRowCount(), prevDiagram + 1);
		Assert.assertEquals(expressionValueTreeObjectReferenceDao.getRowCount(), prevExpressions + 3);

		formDao.makeTransient(form);
		Assert.assertEquals(formDao.getRowCount(), prevForm);
		Assert.assertEquals(diagramDao.getRowCount(), prevDiagram);
		Assert.assertEquals(expressionValueTreeObjectReferenceDao.getRowCount(), prevExpressions);
	}

	@Test
	public void changeTreeObjectReference() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		int prevForm = formDao.getRowCount();
		int prevDiagram = diagramDao.getRowCount();
		int prevExpressions = expressionValueTreeObjectReferenceDao.getRowCount();

		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(FULL_FORM + "2");

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

		Question question3 = new Question();
		question3.setName("Question3");
		group1.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question1.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question1.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question1.addChild(answer3);

		Diagram diagram = new Diagram(DUMMY_DIAGRAM);
		DiagramSource start = new DiagramSource();
		diagram.addDiagramObject(start);
		form.addDiagram(diagram);

		DiagramFork fork = new DiagramFork();
		ExpressionValueTreeObjectReference expression = new ExpressionValueTreeObjectReference(question1);
		fork.setReference(expression);
		diagram.addDiagramObject(fork);

		DiagramLink link1 = new DiagramLink();
		ExpressionChain selectAnswer1 = new ExpressionChain();
		selectAnswer1.addExpression(new ExpressionValueTreeObjectReference(answer1));
		link1.setExpressionChain(selectAnswer1);
		diagram.addDiagramObject(link1);

		DiagramLink link2 = new DiagramLink();
		ExpressionChain selectAnswer2 = new ExpressionChain();
		selectAnswer2.addExpression(new ExpressionValueTreeObjectReference(answer2));
		link2.setExpressionChain(selectAnswer2);
		diagram.addDiagramObject(link2);

		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), prevForm + 1);
		Assert.assertEquals(diagramDao.getRowCount(), prevDiagram + 1);
		Assert.assertEquals(expressionValueTreeObjectReferenceDao.getRowCount(), prevExpressions + 3);

		fork.setReference(new ExpressionValueTreeObjectReference(question2));
		formDao.makePersistent(form);

		formDao.makeTransient(form);
		Assert.assertEquals(formDao.getRowCount(), prevForm);
		Assert.assertEquals(diagramDao.getRowCount(), prevDiagram);
		Assert.assertEquals(expressionValueTreeObjectReferenceDao.getRowCount(), prevExpressions);
	}

}
