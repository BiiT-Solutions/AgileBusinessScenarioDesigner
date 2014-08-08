package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * Known restrictions: - The questions'/groups'/categories' score to set and
 * calculate can only be separated by one level (You can't calculate the
 * minimum score of the questions belonging to a group and set it to a category)
 *
 */
public class ExpressionParser extends GenericParser {

	public ExpressionParser(){
		super();
	}

	public String parse(ExpressionChain expressionChain, String extraConditions) throws ExpressionInvalidException {
		String newRule = "";
		if(expressionChain != null){
			String expressionName = expressionChain.getName();
			//		RuleChecker.checkExpressionValid(expressionChain);
			newRule += Utils.getStartRuleString(expressionName);
			newRule += Utils.getAttributes();
			newRule += Utils.getWhenRuleString();
			newRule += this.createDroolsRule(null, expressionChain.getExpressions(), extraConditions);
			newRule += Utils.getEndRuleString();
		}
		return newRule;
	}
}
