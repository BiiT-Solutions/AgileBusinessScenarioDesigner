package com.biit.jexeval;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.QuestionUnit;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.jexeval.exceptions.ExpressionException;

@Test(groups = { "expressionEvaluator" })
public class ExpressionTest {

	@Test
	public void testInFunction() {
		ExpressionChecker e = new ExpressionChecker("x in(1,2,3)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(expectedExceptions = ExpressionException.class)
	public void testIncorrectInFunction() {
		ExpressionChecker e = new ExpressionChecker("x + in(1,2,3)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test
	public void testBetweenFunction() {
		ExpressionChecker e = new ExpressionChecker("x between(0, 10)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(expectedExceptions = ExpressionException.class)
	public void testIncorrectBetweenFunction() {
		ExpressionChecker e = new ExpressionChecker("x + between(0, 10)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test
	public void testDateTreeObject() throws FieldTooLongException {
		TreeObject question = new Question();
		question.setName("Question1");

		ExpressionChain expressions = new ExpressionChain();
		ExpressionValueTreeObjectReference dateTreeVariable = new ExpressionValueTreeObjectReference();
		dateTreeVariable.setReference(question);
		dateTreeVariable.setUnit(QuestionUnit.YEARS);
		expressions.addExpression(dateTreeVariable);
		// No exception launch.
		expressions.getExpressionEvaluator().eval();
	}

	@Test
	public void testDateVariables() throws FieldTooLongException {
		TreeObject question = new Question();
		question.setName("Question1");

		CustomVariable variable = new CustomVariable(null, "var1", CustomVariableType.DATE,
				CustomVariableScope.QUESTION);

		ExpressionChain expressions = new ExpressionChain();
		ExpressionValueCustomVariable dateVariable = new ExpressionValueCustomVariable(question, variable);
		dateVariable.setUnit(QuestionUnit.MONTHS);
		expressions.addExpression(dateVariable);
		// No exception launch.
		expressions.getExpressionEvaluator().eval();
	}

}
