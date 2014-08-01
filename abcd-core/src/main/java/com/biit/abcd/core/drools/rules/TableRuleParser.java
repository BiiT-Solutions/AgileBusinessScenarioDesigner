package com.biit.abcd.core.drools.rules;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class TableRuleParser {

	public static String parseTableRule(TableRule tableRule) throws ExpressionInvalidException {
		String newRules = "";
		String tableRuleName = tableRule.getName();
		int i = 0;

		// One rule for each row
		for (TableRuleRow row : tableRule.getRules()) {
			// checkRowExpressionValid(row, i + 1);
			newRules += Utils.getStartRuleString(tableRuleName + "_row_" + i);
			newRules += createAttributes();
			newRules += Utils.getWhenRuleString();
			newRules += createRule(row);
			newRules += Utils.getEndRuleString();
			i++;
		}
		return newRules;
	}

	private static String createAttributes() {
		return "";
	}

	private static String createRule(TableRuleRow row) {
		return RuleParser.parseRule(row.getConditions(), row.getAction());
	}

	/**
	 * Chesck if the expression is valid using the jexeval, if not valid,
	 * returns without parsing any rules and sends an exception
	 * 
	 * @param row
	 * @param rowIndex
	 * @throws ExpressionInvalidException
	 */
	private static void checkRowExpressionValid(TableRuleRow row, int rowIndex) throws ExpressionInvalidException {
		List<Expression> conditionList = row.getConditions();
		int columnIndex = 1;
		for (Expression condition : conditionList) {
			// Check the conditions that are ExpressionChains, i.e. the answers
			if (condition instanceof ExpressionChain) {
				try {
					((ExpressionChain) condition).getExpressionEvaluator().eval();
				} catch (Exception e) {
					System.out.println("Exception:" + e);
					throw new ExpressionInvalidException("[" + rowIndex + ":" + columnIndex + "] :: ["
							+ condition.getRepresentation() + "] :: Answer expression invalid");
				}
			}
			columnIndex++;
		}
		// Check the action
		try {
			row.getAction().getExpressionEvaluator().eval();
		} catch (Exception e) {
			throw new ExpressionInvalidException("[" + rowIndex + "] :: Action expression invalid: "
					+ row.getAction().toString());
		}
	}
}
