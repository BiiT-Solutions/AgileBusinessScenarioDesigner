package com.biit.abcd.core.drools.rules;

import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class RuleChecker {

	/**
	 * Checks if the expression is valid using the jexeval, if not valid,
	 * returns without parsing any rules and sends an exception
	 * 
	 * @param row
	 * @param rowIndex
	 * @throws ExpressionInvalidException
	 */
	public static void checkExpressionValid(ExpressionChain expression) throws ExpressionInvalidException {
		try {
			expression.getExpressionEvaluator().eval();
		} catch (Exception e) {
			throw new ExpressionInvalidException("Expression invalid", expression);
		}
	}

	/**
	 * Check if the expression is valid using the jexeval, if not valid, returns
	 * without parsing any rules and sends an exception
	 * 
	 * @param row
	 * @param rowIndex
	 * @throws ExpressionInvalidException
	 */
	public static void checkRowExpressionValid(TableRuleRow row, int rowIndex) throws ExpressionInvalidException {
		List<Expression> conditionList = row.getConditions().getExpressions();
		for (Expression condition : conditionList) {
			// Check the conditions that are ExpressionChains, i.e. the answers
			if (condition instanceof ExpressionChain) {
				try {
					((ExpressionChain) condition).getExpressionEvaluator().eval();
				} catch (Exception e) {
					throw new ExpressionInvalidException("Expression invalid", (ExpressionChain) condition);
				}
			}
		}
		// Check the action
		try {
			row.getAction().getExpressionEvaluator().eval();
		} catch (Exception e) {
			throw new ExpressionInvalidException("Expression invalid", (ExpressionChain) row.getAction());
		}
	}

	public static void checkRuleValid(Rule rule) throws RuleInvalidException {
		try {
			rule.getConditions().getExpressionEvaluator().eval();
			rule.getActions().getExpressionEvaluator().eval();
		} catch (Exception e) {
			throw new RuleInvalidException("Rule " + rule.getName() + " invalid");
		}
	}

}
