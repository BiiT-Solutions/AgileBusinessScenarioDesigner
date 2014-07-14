package com.biit.jexeval;

import java.math.BigDecimal;
import java.util.List;

import com.biit.jexeval.ExpressionEvaluator;
import com.biit.jexeval.Function;
import com.biit.jexeval.exceptions.ExpressionException;

/**
 * Adds extra functions to the standard Expression Evaluator, but is unable to do calculus of this new functions.
 */
public class ExpressionChecker extends ExpressionEvaluator {

	public ExpressionChecker(String expression) {
		super(expression);
	}

	@Override
	protected void setFunctions() {
		super.setFunctions();
		addFunction(new Function("IN") {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				return BigDecimal.ONE;
			}
		});
		addFunction(new Function("BETWEEN") {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters) {
				return BigDecimal.ONE;
			}
		});
	}

	/**
	 * Functions except IN and BETWEEN can only precede an operator, '(' or ','. IN and BETWEEN must have a variable.
	 * 
	 * @param lastToken
	 */
	@Override
	protected void checkFunctionOrder(String lastToken, String token) throws ExpressionException {
		if (!token.equals("BETWEEN") && !token.equals("IN")) {
			super.checkFunctionOrder(lastToken, token);
		} else {
			// IN and BETWEEN precedes a variable.
			if (!getVariables().containsKey(lastToken)) {
				throw new ExpressionException("Variable missed between '" + lastToken + "' and '" + token + "'");
			}
		}
	}

}
