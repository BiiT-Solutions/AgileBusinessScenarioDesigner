package com.biit.jexeval;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.DateUnit;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueDateFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueDateTreeObjectReference;
import com.biit.jexeval.exceptions.ExpressionException;

public class ExpressionTest {

	@Test(groups = { "expressionEvaluator" })
	public void testInFunction() {
		ExpressionChecker e = new ExpressionChecker("x in(1,2,3)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(groups = { "expressionEvaluator" }, expectedExceptions = ExpressionException.class)
	public void testIncorrectInFunction() {
		ExpressionChecker e = new ExpressionChecker("x + in(1,2,3)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(groups = { "expressionEvaluator" })
	public void testBetweenFunction() {
		ExpressionChecker e = new ExpressionChecker("x between(0, 10)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(groups = { "expressionEvaluator" }, expectedExceptions = ExpressionException.class)
	public void testIncorrectBetweenFunction() {
		ExpressionChecker e = new ExpressionChecker("x + between(0, 10)");
		Assert.assertEquals("1", e.with("x", "1").eval().toPlainString());
	}

	@Test(groups = { "expressionEvaluator" })
	public void testDateTreeObject() {
		TreeObject question = new Question();
		question.setName("Question1");

		ExpressionChain expressions = new ExpressionChain();
		ExpressionValueDateTreeObjectReference dateTreeVariable = new ExpressionValueDateTreeObjectReference();
		dateTreeVariable.setReference(question);
		dateTreeVariable.setUnit(DateUnit.YEARS);
		expressions.addExpression(dateTreeVariable);
		// No exception launch.
		expressions.getExpressionEvaluator().eval();
	}

	@Test(groups = { "expressionEvaluator" })
	public void testDateVariables() {
		TreeObject question = new Question();
		question.setName("Question1");

		CustomVariable variable = new CustomVariable(null, "var1", CustomVariableType.DATE,
				CustomVariableScope.QUESTION);

		ExpressionChain expressions = new ExpressionChain();
		ExpressionValueDateFormCustomVariable dateVariable = new ExpressionValueDateFormCustomVariable(question,
				variable);
		dateVariable.setUnit(DateUnit.MONTHS);
		expressions.addExpression(dateVariable);
		// No exception launch.
		expressions.getExpressionEvaluator().eval();
	}

}
