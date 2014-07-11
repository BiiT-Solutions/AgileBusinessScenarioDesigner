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
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.ActionString;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";
	private final static String DUMMY_DIAGRAM = "Dummy Diagram";
	private final static String FULL_FORM = "Complete Form";
	private final static String DIAGRAM_FORM = "Diagram Form";
	private final static String TABLE_RULE_FORM = "Table Rule Form";
	private final static String OTHER_FORM = "Other Form";
	private final static String CATEGORY_LABEL = "Category1";
	private final static String CONDITION_EXPRESSION = "Question=Question1 AND Answer=Yes";
	private final static String ACTION_EXPRESSION = "Score=3";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IDiagramDao diagramDao;

	@Autowired
	private ITableRuleRowDao tableRuleDao;

	@Autowired
	private IQuestionDao questionDao;

	private Form form;

	@Test(groups = { "formDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyForm() {
		Form form = new Form();
		form.setName(DUMMY_FORM);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeDummyForm")
	public void getDummyForm() {
		List<Form> forms = formDao.getAll();
		Assert.assertEquals(forms.get(0).getName(), DUMMY_FORM);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "getDummyForm")
	public void removeDummyForm() {
		List<Form> forms = formDao.getAll();
		formDao.makeTransient(forms.get(0));
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "removeDummyForm")
	public void storeFormWithCategory() throws NotValidChildException {
		form = new Form();
		form.setName(FULL_FORM);
		Category category = new Category();
		category.setName(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
	}

	// @Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory", expectedExceptions = {
	// ConstraintViolationException.class })
	// public void addNewCategoryWithSameName() throws NotValidChildException, ConstraintViolationException {
	// Category category = new Category();
	// category.setLabel(CATEGORY_LABEL);
	// form.addChild(category);
	// formDao.makePersistent(form);
	// }
	//
	// @Test(groups = { "formDao" }, dependsOnMethods = "addNewCategoryWithSameName")
	// public void removeRepeatedCategoryWithSameName() throws NotValidChildException, ConstraintViolationException,
	// ChildrenNotFoundException {
	// form.removeChild(0);
	// formDao.makePersistent(form);
	// }

	// @Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory")
	// public void increaseVersion() {
	// Form oldForm = formDao.read(form.getId());
	// form.increaseVersion();
	// Assert.assertEquals(oldForm.getVersion() + 1, (int) form.getVersion());
	// formDao.makePersistent(form);
	// Assert.assertEquals(formDao.getLastVersion(oldForm), 2);
	// Assert.assertEquals(formDao.getLastVersion(oldForm), formDao.getLastVersion(form));
	// }

	@Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory")
	public void storeOtherFormWithSameLabelCategory() throws NotValidChildException {
		Form form2 = new Form();
		form2.setName(OTHER_FORM);
		Category category = new Category();
		category.setName(CATEGORY_LABEL);
		form2.addChild(category);
		formDao.makePersistent(form2);
		Form retrievedForm = formDao.read(form2.getId());

		Assert.assertEquals(retrievedForm.getId(), form2.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory")
	public void moveElementsUp() throws NotValidChildException, ChildrenNotFoundException {
		Category category2 = new Category();
		category2.setName("Category2");
		form.addChild(category2);

		Category category3 = new Category();
		category3.setName("Category3");
		form.addChild(category3);

		Group group1 = new Group();
		group1.setName("Group1");
		category2.addChild(group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category2.addChild(group2);

		Group group3 = new Group();
		group3.setName("Group3");
		category2.addChild(group3);

		Question question1 = new Question();
		question1.setName("Question1");
		group2.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		group2.addChild(question2);

		Question question3 = new Question();
		question3.setName("Question3");
		group2.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);

		// Update form with new elements
		formDao.makePersistent(form);

		// Move #2 up
		form.switchChildren(1, 0, null);
		category2.switchChildren(1, 0, null);
		group2.switchChildren(1, 0, null);
		question2.switchChildren(1, 0, null);

		// Update form with this changes
		formDao.makePersistent(form);
		Form storedForm = formDao.read(form.getId());

		// Compare order is the same.
		Assert.assertTrue(compare(form, storedForm));
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeOtherFormWithSameLabelCategory")
	public void storeFormDiagram() throws NotValidChildException {
		Form form = new Form();
		form.setName(DIAGRAM_FORM);

		Diagram diagram = new Diagram(form, DUMMY_DIAGRAM);
		form.getDiagrams().add(diagram);

		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(diagramDao.getRowCount(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeOtherFormWithSameLabelCategory")
	public void storeFormTableRule() throws NotValidChildException, NotValidExpression {
		Form form = new Form();
		form.setName(TABLE_RULE_FORM);

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

		//		QuestionAndAnswerCondition condition = new QuestionAndAnswerCondition();
		//		condition.setQuestion(question1);
		//		condition.setAnswer(answer1);

		ExpressionChain condition = new ExpressionChain();
		condition.addExpression(new ExpressionValueString(CONDITION_EXPRESSION));
		tableRuleRow.getConditions().add(condition);
		tableRuleRow.addAction(new ActionString());
		tableRuleRow.getActions().get(0).setExpression(ACTION_EXPRESSION);

		tableRule.getRules().add(tableRuleRow);

		form.getTableRules().add(tableRule);

		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getTableRules().size(), 1);

		Assert.assertEquals(retrievedForm.getTableRules().get(0).getRules().get(0).getConditions().get(0).getExpressionTableString(),
				CONDITION_EXPRESSION);
		Assert.assertEquals(retrievedForm.getTableRules().get(0).getRules().get(0).getActions().get(0).getExpression(),
				ACTION_EXPRESSION);
		Assert.assertEquals(tableRuleDao.getRowCount(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = { "storeFormDiagram", "storeOtherFormWithSameLabelCategory",
	"storeFormTableRule" })
	public void removeForms() {
		formDao.removeAll();
		Assert.assertEquals(formDao.getRowCount(), 0);
		Assert.assertEquals(diagramDao.getRowCount(), 0);
		Assert.assertEquals(tableRuleDao.getRowCount(), 0);
	}

	private boolean compare(TreeObject object1, TreeObject object2) {
		if (object1.getId() != object2.getId()) {
			return false;
		}
		if (object1.getClass() != object2.getClass()) {
			return false;
		}
		if (object1 instanceof Form) {
			if (!((Form) object1).getName().equals(((Form) object2).getName())) {
				return false;
			}
		} else if (object1 instanceof Category) {
			if (!((Category) object1).getName().equals(((Category) object2).getName())) {
				return false;
			}
		} else if (object1 instanceof Group) {
			if (!((Group) object1).getName().equals(((Group) object2).getName())) {
				return false;
			}
		} else if (object1 instanceof Question) {
			if (!((Question) object1).getName().equals(((Question) object2).getName())) {
				return false;
			}
		} else if (object1 instanceof Answer) {
			if (!((Answer) object1).getName().equals(((Answer) object2).getName())) {
				return false;
			}
		}
		if (object1.getChildren().size() != object2.getChildren().size()) {
			return false;
		}
		for (int i = 0; i < object1.getChildren().size(); i++) {
			if (!compare(object1.getChildren().get(i), object2.getChildren().get(i))) {
				return false;
			}
		}
		return true;
	}
}
