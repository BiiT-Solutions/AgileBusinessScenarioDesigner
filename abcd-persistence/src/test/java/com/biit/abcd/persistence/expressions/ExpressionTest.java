package com.biit.abcd.persistence.expressions;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicLogic;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicLogic.ExprAtomicChildLogicType;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicMath;
import com.biit.abcd.persistence.entity.expressions.ExprGroup;
import com.biit.abcd.persistence.entity.expressions.ExprOpMath;
import com.biit.abcd.persistence.entity.expressions.ExprOpValue;
import com.biit.abcd.persistence.entity.expressions.ExprPortMath;
import com.biit.abcd.persistence.entity.expressions.ExprValueDouble;
import com.biit.abcd.persistence.entity.expressions.ExprValueFormReference;
import com.biit.abcd.persistence.entity.expressions.ExprValueString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class ExpressionTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with expressions";
	private final static String EXPR_PORT_MATH_NAME = "First Expression";

	@Autowired
	private IFormDao formDao;

	private Form form;
	private Question question2;

	private Form createForm() throws NotValidChildException {
		Form form = new Form();
		form.setName(DUMMY_FORM);

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

		question2 = new Question();
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

		CustomVariable customVariableName = new CustomVariable(form, "Name", CustomVariableType.STRING,
				CustomVariableScope.FORM);
		form.getCustomVariables().add(customVariableName);

		CustomVariable customVariableScore = new CustomVariable(form, "Score", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);
		form.getCustomVariables().add(customVariableScore);

		return form;
	}

//	@Test(groups = { "expressions" })
//	public void basicAlwaysExpression() {
//		ExprAtomicLogic exprAtomic = new ExprAtomicLogic();
//		exprAtomic.setAlways();
//		form.getExpressions().add(exprAtomic);
//		formDao.makePersistent(form);
//
//		Form retrievedForm = formDao.read(form.getId());
//
//		Assert.assertEquals(retrievedForm.getExpressions().size(), 1);
//		Assert.assertEquals(retrievedForm.getExpressions().get(0).getClass(), ExprAtomicLogic.class);
//		Assert.assertEquals(((ExprAtomicLogic) retrievedForm.getExpressions().get(0)).getType(),
//				ExprAtomicChildLogicType.ALWAYS);
//
//		formDao.makeTransient(form);
//	}
//
//	@Test(groups = { "expressions" })
//	public void basicLogicExpression() {
//		ExprPortMath exprPortMath = new ExprPortMath(EXPR_PORT_MATH_NAME);
//
//		// Port math has already a child
//		Assert.assertEquals(exprPortMath.getChilds().size(), 1);
//		Assert.assertEquals(exprPortMath.getChilds().get(0).getClass(), ExprAtomicMath.class);
//
//		// A child expression adds the joiner and a child (2 childs more).
//		exprPortMath.addChildExpression();
//		Assert.assertEquals(exprPortMath.getChilds().size(), 3);
//		Assert.assertEquals(exprPortMath.getChilds().get(1).getClass(), ExprOpMath.class);
//		Assert.assertEquals(exprPortMath.getChilds().get(2).getClass(), ExprAtomicMath.class);
//
//		// Setting values: Questions2's score = 1
//		ExprValueFormReference exprValueString = new ExprValueFormReference(question2, form.getCustomVariables().get(1));
//		((ExprAtomicMath) exprPortMath.getChilds().get(0)).setValue(exprValueString);
//
//		((ExprOpMath) exprPortMath.getChilds().get(1)).setValue((new ExprOpValue("=", "=")));
//
//		((ExprAtomicMath) exprPortMath.getChilds().get(2)).setValue(new ExprValueDouble(1d));
//
//		form.getExpressions().add(exprPortMath);
//		formDao.makePersistent(form);
//
//		Form retrievedForm = formDao.read(form.getId());
//
//	}
}
