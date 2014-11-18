package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupEndConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * Transforms a Rule to a Drools rule. Internally is the same. This class is
 * used for standardization purposes.
 * 
 */
public class RuleToDroolsRule {

	private static List<DroolsRule> droolsRules;

	public static List<DroolsRule> parse(Rule rule, ExpressionChain extraConditions) throws RuleInvalidException,
			RuleNotImplementedException {
		DroolsRule droolsRule = null;
		if (rule != null) {
			Rule ruleCopy = rule.generateCopy();
			if (hasAndOrNotConditions(ruleCopy.getConditions())) {
				parseAndOrNotConditions(ruleCopy, extraConditions);

			} else {
				droolsRule = new DroolsRule(ruleCopy);
				// TODO Uncomment when changed the validator to the parser
				// RuleChecker.checkRuleValid(droolsRule);
				droolsRule.setName(RulesUtils.getRuleName(droolsRule.getName(), extraConditions));
				if (extraConditions != null) {
					droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
				}
				droolsRules = Arrays.asList(droolsRule);
			}

		}
		return droolsRules;
	}

	/**
	 * Return true if the conditions of the rule have AND/OR or NOT expressions
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static boolean hasAndOrNotConditions(ExpressionChain expressionChain) {
		for (Expression expression : expressionChain.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				hasAndOrNotConditions((ExpressionChain) expression);
			} else {
				if (expression instanceof ExpressionOperatorLogic) {
					ExpressionOperatorLogic expressionOperatorLogic = (ExpressionOperatorLogic) expression;
					if (expressionOperatorLogic.getValue().equals(AvailableOperator.AND)
							|| expressionOperatorLogic.getValue().equals(AvailableOperator.OR)) {
						return true;
					}
				} else if (expression instanceof ExpressionFunction) {
					ExpressionFunction expressionFunction = (ExpressionFunction) expression;
					if (expressionFunction.getValue().equals(AvailableFunction.NOT)) {
						addBracketsToNotConditions(expressionChain, expression);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Add an extra parenthesis to NOT expressions
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static void addBracketsToNotConditions(ExpressionChain expressionChain, Expression notExpression) {
		int notExpressionIndex = expressionChain.getExpressions().indexOf(notExpression);
		Expression expression = expressionChain.getExpressions().get(notExpressionIndex + 1);
		System.out.println("CHECKING LEFT BRACKET" + expression);

		if (!(expression instanceof ExpressionSymbol)) {
			expressionChain.getExpressions().add(notExpressionIndex + 1,
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
			System.out.println("ADD LEFT BRACKET");
		}
		else if (expression instanceof ExpressionSymbol
				&& !(((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.LEFT_BRACKET))) {
			expressionChain.getExpressions().add(notExpressionIndex + 1,
					new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));

			System.out.println("ADD LEFT BRACKET");
		}
	}

	private static boolean parseAndOrNotConditions(Rule ruleCopy, ExpressionChain extraConditions) {
		ITreeElement result = calculatePrattParserResult(ruleCopy.getConditions());
		TreeElementGroupConditionFinderVisitor treeVisitor = new TreeElementGroupConditionFinderVisitor();
		try {
			result.accept(treeVisitor);
			if (!treeVisitor.getConditions().isEmpty()) {
				int ruleCounter = 1;
				droolsRules = new ArrayList<DroolsRule>();
				for (ExpressionChain visitorRules : treeVisitor.getConditions()) {

					System.out.println("BASIC EXPRESSION FOUND: " + visitorRules);

					DroolsRuleGroup droolsRule = new DroolsRuleGroup();
					droolsRule.setConditionExpressionChainId(visitorRules.getName());
					droolsRule.setConditions(visitorRules);
					// droolsRule.setActions(copyRule.getActions());
					String ruleName = ruleCopy.getName() + "_" + ruleCounter;
					droolsRule.setName(RulesUtils.getRuleName(ruleName, extraConditions));
					if (extraConditions != null) {
						droolsRule.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
					}
					// Set the special actions for the group rules
					String groupAction = "then\n";
					groupAction += "\tAbcdLogger.debug(\"RuleFired\", \"Rule "
							+ droolsRule.getName().replace("\n", "").replace("\"", "") + " fired\");\n";
					groupAction += "\tinsert ( new FiredRule(\""
							+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n";
					droolsRule.setGroupAction(groupAction);
					droolsRules.add(droolsRule);
					ruleCounter++;
				}
				createEndCombinationRule(result, ruleCopy, ruleCounter);
				return true;
			}
		} catch (NotCompatibleTypeException e) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
		}
		return false;
	}

	private static void createEndCombinationRule(ITreeElement result, Rule ruleCopy, int ruleCounter) {
		TreeElementGroupEndConditionFinderVisitor treeVisitor = new TreeElementGroupEndConditionFinderVisitor();
		try {
			result.accept(treeVisitor);
			DroolsRuleGroupEndRule droolsRule = new DroolsRuleGroupEndRule();
			droolsRule.setConditions(treeVisitor.getCompleteExpression());
			droolsRule.setActions(ruleCopy.getActions());
			droolsRule.setName(RulesUtils.getRuleName(ruleCopy.getName() + "_" + ruleCounter, null));
			// Set the special conditions/actions for the group rules
			droolsRule.setGroupCondition("\tnot( FiredRule( getRuleName() == '"
					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "') ) and\n");
			droolsRule.setGroupAction("\tinsert ( new FiredRule(\""
					+ droolsRule.getName().split(" ")[1].replace("\n", "").replace("\"", "") + "\"));\n");
			droolsRule.setParserResult(result);
			for (DroolsRule generatedDroolsRule : droolsRules) {
				droolsRule.putExpresionRuleIdentifiers(((DroolsRuleGroup) generatedDroolsRule)
						.getConditionExpressionChainId(), generatedDroolsRule.getName().split(" ")[1].replace("\n", "")
						.replace("\"", ""));
			}
			System.out.println("END GROUP RULE: " + droolsRule.getConditions());
			droolsRules.add(droolsRule);
		} catch (NotCompatibleTypeException e) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
		}
	}

	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		try {
			prattParserResult = prattParser.parseExpression();
		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(DroolsParser.class.getName(), ex);
		}
		return prattParserResult;
	}
}
