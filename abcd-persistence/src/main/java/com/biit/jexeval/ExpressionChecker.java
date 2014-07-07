package com.biit.jexeval;

import java.math.BigDecimal;
import java.util.List;

import com.biit.jexeval.ExpressionEvaluator;
import com.biit.jexeval.Function;

/**
 * Checks the expression using the ExpressionEvaluator, but is unable to do calculus.
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

}
