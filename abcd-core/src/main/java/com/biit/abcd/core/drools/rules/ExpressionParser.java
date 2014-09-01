package com.biit.abcd.core.drools.rules;

import java.util.List;
import java.util.UUID;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;

/**
 * Known restrictions: - The questions'/groups'/categories' score to set and
 * calculate can only be separated by one level (You can't calculate the minimum
 * score of the questions belonging to a group and set it to a category)
 *
 */
public class ExpressionParser extends GenericParser {

	public ExpressionParser() {
		super();
	}

	public String parse(ExpressionChain expressionChain, String extraConditions) throws ExpressionInvalidException {
		String newRule = "";
		if (expressionChain != null) {
			if (expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable) {
				newRule += this.createDroolsRule(null, expressionChain.getExpressions(), extraConditions);
			} else {
				String expressionName = expressionChain.getName();
				// RuleChecker.checkExpressionValid(expressionChain);
				newRule += Utils.getStartRuleString(expressionName);
				newRule += Utils.getAttributes();
				newRule += Utils.getWhenRuleString();
				newRule += this.createDroolsRule(null, expressionChain.getExpressions(), extraConditions);
				newRule += Utils.getEndRuleString();
			}
		}
		return newRule;
	}

	public String parse(List<Expression> expressions, String extraConditions) throws ExpressionInvalidException {
		String newRule = "";
		if (expressions != null) {
			if (expressions.get(0) instanceof ExpressionValueGenericCustomVariable) {
				newRule += this.createDroolsRule(null, expressions, extraConditions);
			} else {
//				String expressionName = expressionChain.getName();
				// RuleChecker.checkExpressionValid(expressionChain);
				newRule += Utils.getStartRuleString(UUID.randomUUID().toString().replaceAll("-", ""));
				newRule += Utils.getAttributes();
				newRule += Utils.getWhenRuleString();
				newRule += this.createDroolsRule(null, expressions, extraConditions);
				newRule += Utils.getEndRuleString();
			}
		}
		return newRule;
	}
}
