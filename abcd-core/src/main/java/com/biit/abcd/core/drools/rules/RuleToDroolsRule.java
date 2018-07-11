package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupEndConditionFinderVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.DiagramElement;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.drools.engine.DroolsHelper;
import com.biit.form.entity.TreeObject;

/**
 * Transforms a Rule into a Drools rule.<br>
 * It also unwraps the generic variables that can be used in the rules and
 * creates a new set of rules if necessary.
 */
public class RuleToDroolsRule {

	private static DroolsHelper droolsHelper;

	public static List<DroolsRule> parse(DiagramElement node, DroolsRule droolsRule, DroolsHelper droolsHelper) throws InvalidRuleException,
			RuleNotImplementedException, ExpressionInvalidException, NotCompatibleTypeException {
		setDroolsHelper(droolsHelper);
		return parse(node, droolsRule, null, droolsHelper);
	}

	public static List<DroolsRule> parse(DiagramElement node, Rule rule, ExpressionChain extraConditions, DroolsHelper droolsHelper)
			throws InvalidRuleException, RuleNotImplementedException, ExpressionInvalidException, NotCompatibleTypeException {

		List<DroolsRule> conditionsRules = new ArrayList<>();
		setDroolsHelper(droolsHelper);
		if (rule != null) {
			List<DroolsRule> droolsRuleList = null;
			Rule ruleCopy = rule.generateCopy();
			// Add extra conditions before parsing rule
			if (extraConditions != null) {
				ruleCopy.addExtraConditions((ExpressionChain) extraConditions.generateCopy());
			}
			// Check for generics
			if (hasGenericVariables(ruleCopy.getConditions())) {
				droolsRuleList = unwrapGenericVariables(ruleCopy);
			}
			// Check for AND/OR/NOT expressions
			if (hasAndOrNotConditions(ruleCopy.getConditions())) {
				try {
					if (droolsRuleList != null) {
						droolsRuleList = parseAndOrNotConditions(droolsRuleList);
					} else {
						droolsRuleList = parseAndOrNotConditions(Arrays.asList(new DroolsRule(ruleCopy)));
					}
				} catch (NotCompatibleTypeException ncte) {
					if (node != null) {
						AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), "Error parsing '" + rule + "' at diagram node '" + node.getText() + "'.");
					} else {
						AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), "Error parsing '" + rule + "'.");
					}
					throw ncte;
				}
			}
			if (droolsRuleList != null) {
				// Set the name for the rules
				int ruleNumber = 0;
				for (DroolsRule droolsRule : droolsRuleList) {
					droolsRule.setName(RuleGenerationUtils.createRuleName(droolsRule, extraConditions, "_" + ruleNumber));
					// Add identifiers to the drools rule end group
					if (droolsRule instanceof DroolsRuleGroupEndRule) {
						for (DroolsRule generatedDroolsRule : droolsRuleList) {
							if ((generatedDroolsRule instanceof DroolsRuleGroup) && !(generatedDroolsRule instanceof DroolsRuleGroupEndRule)) {

								String droolsRuleName = RuleGenerationUtils.getCleanRuleName(generatedDroolsRule.getName());
								((DroolsRuleGroupEndRule) droolsRule).putExpresionRuleIdentifiers(
										((DroolsRuleGroup) generatedDroolsRule).getConditionExpressionChainId(), droolsRuleName);
							}
						}
					}
					conditionsRules.add(droolsRule);
					ruleNumber++;
				}
			} else {
				DroolsRule droolsRule = new DroolsRule(ruleCopy);
				// TODO Uncomment when changed the validator to the parser
				// RuleChecker.checkRuleValid(droolsRule);
				droolsRule.setName(RuleGenerationUtils.createRuleName(droolsRule, extraConditions));
				conditionsRules = Arrays.asList(droolsRule);
			}
		}

		List<DroolsRule> droolsRules = new ArrayList<>();
		for (DroolsRule droolsRule : conditionsRules) {
			try {
				List<DroolsRule> actionsRules = ExpressionToDroolsRule.parse(node, droolsRule, droolsHelper);
				if (actionsRules != null && !actionsRules.isEmpty()) {
					droolsRules.addAll(actionsRules);
				}
			} catch (NotCompatibleTypeException ncte) {
				if (node != null) {
					AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), "Error parsing '" + rule + "' at diagram node '" + node.getText() + "'.");
				} else {
					AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), "Error parsing '" + rule + "'.");
				}
				throw ncte;
			}
		}
		// Validate the rules
		// Check the rules generated by the system
		// for (DroolsRule droolsRule : droolsRules) {
		// RuleChecker.checkRule(droolsRule);
		// }
		return droolsRules;
	}

	protected static boolean hasGenericVariables(ExpressionChain conditions) {
		for (Expression expression : conditions.getExpressions()) {
			if (expression instanceof ExpressionChain) {
				hasGenericVariables((ExpressionChain) expression);
			} else if ((expression instanceof ExpressionValueGenericVariable) || (expression instanceof ExpressionValueGenericCustomVariable)) {
				return true;
			}
		}
		return false;
	}

	// Generate complete drools rules (conditions/actions) with the conditions
	// variables unwrapped
	private static List<DroolsRule> unwrapGenericVariables(Rule rule) {
		List<DroolsRule> unwrappedRules = new ArrayList<DroolsRule>();
		List<TreeObject> unwrappedObjects = null;

		for (int originalExpressionIndex = 0; originalExpressionIndex < rule.getConditions().getExpressions().size(); originalExpressionIndex++) {
			Expression expression = rule.getConditions().getExpressions().get(originalExpressionIndex);
			if ((expression instanceof ExpressionValueGenericCustomVariable) || (expression instanceof ExpressionValueGenericVariable)) {
				// Unwrap the generic variables being analyzed
				unwrappedObjects = getUnwrappedTreeObjects((ExpressionValueGenericCustomVariable) expression);
				break;
			}
		}
		if (unwrappedObjects != null && !unwrappedObjects.isEmpty()) {
			for (TreeObject treeObjectUnwrapped : unwrappedObjects) {
				DroolsRule droolsRule = new DroolsRule();
				for (int originalExpressionIndex = 0; originalExpressionIndex < rule.getConditions().getExpressions().size(); originalExpressionIndex++) {
					Expression expression = rule.getConditions().getExpressions().get(originalExpressionIndex);

					if (expression instanceof ExpressionValueGenericCustomVariable) {
						CustomVariable customVariableOfGeneric = ((ExpressionValueGenericCustomVariable) expression).getVariable();
						droolsRule.getConditions().addExpression(new ExpressionValueCustomVariable(treeObjectUnwrapped, customVariableOfGeneric));

					} else if (expression instanceof ExpressionValueGenericVariable) {
						droolsRule.getConditions().addExpression(new ExpressionValueTreeObjectReference(treeObjectUnwrapped));

					} else {
						droolsRule.getConditions().addExpression(expression);
					}
				}
				unwrappedRules.add(droolsRule);
			}
		}

		// Copy the name and the actions of the rule
		for (DroolsRule droolsRule : unwrappedRules) {
			droolsRule.setName(RuleGenerationUtils.createRuleName(rule));
			droolsRule.setActions(rule.getActions());
		}

		return unwrappedRules;
	}

	/**
	 * Get the brothers of the tree object passed
	 * 
	 * @param expressionValueGenericVariable
	 * @return
	 */
	private static List<TreeObject> getUnwrappedTreeObjects(ExpressionValueGenericVariable expressionValueGenericVariable) {
		List<TreeObject> treeObjects = null;
		switch (expressionValueGenericVariable.getType()) {
		case CATEGORY:
			treeObjects = new ArrayList<TreeObject>(getDroolsHelper().getForm().getAll(Category.class));
			break;
		case GROUP:
			treeObjects = new ArrayList<TreeObject>(getDroolsHelper().getForm().getAll(Group.class));
			break;
		case QUESTION_CATEGORY:
			List<TreeObject> categories = new ArrayList<TreeObject>(getDroolsHelper().getForm().getAll(Category.class));
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject category : categories) {
				treeObjects.addAll(category.getChildren(Question.class));
			}
			break;
		case QUESTION_GROUP:
			List<TreeObject> groups = new ArrayList<TreeObject>(getDroolsHelper().getForm().getAll(Group.class));
			treeObjects = new ArrayList<TreeObject>();
			for (TreeObject group : groups) {
				treeObjects.addAll(group.getChildren(Question.class));
			}
			break;
		}
		return treeObjects;
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
					if (expressionOperatorLogic.getValue().equals(AvailableOperator.AND) || expressionOperatorLogic.getValue().equals(AvailableOperator.OR)) {
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

		if (!(expression instanceof ExpressionSymbol)) {
			expressionChain.getExpressions().add(notExpressionIndex + 1, new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		} else if (expression instanceof ExpressionSymbol && !(((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.LEFT_BRACKET))) {
			expressionChain.getExpressions().add(notExpressionIndex + 1, new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		}
	}

	private static List<DroolsRule> parseAndOrNotConditions(List<DroolsRule> droolsRuleList) throws NotCompatibleTypeException {
		List<DroolsRule> newConditions = new ArrayList<DroolsRule>();
		for (DroolsRule droolsRule : droolsRuleList) {
			ITreeElement result = calculatePrattParserResult(droolsRule.getConditions());
			TreeElementGroupConditionFinderVisitor treeVisitor = new TreeElementGroupConditionFinderVisitor();
			result.accept(treeVisitor);
			if (!treeVisitor.getConditions().isEmpty()) {
				for (ExpressionChain visitorRules : treeVisitor.getConditions()) {
					DroolsRuleGroup droolsRuleGroup = new DroolsRuleGroup();
					droolsRuleGroup.setName(RuleGenerationUtils.createRuleName(droolsRule));
					droolsRuleGroup.setConditionExpressionChainId(visitorRules.getName());
					droolsRuleGroup.setConditions(visitorRules);
					newConditions.add(droolsRuleGroup);
				}
				createEndCombinationRule(result, newConditions, droolsRule);
			}
		}
		return newConditions;
	}

	private static DroolsRule createEndCombinationRule(ITreeElement result, List<DroolsRule> newConditions, DroolsRule droolsRule)
			throws NotCompatibleTypeException {
		DroolsRuleGroupEndRule droolsEngGroupRule = null;
		TreeElementGroupEndConditionFinderVisitor treeVisitor = new TreeElementGroupEndConditionFinderVisitor();
		result.accept(treeVisitor);
		droolsEngGroupRule = new DroolsRuleGroupEndRule();
		droolsEngGroupRule.setName(RuleGenerationUtils.createRuleName(droolsRule));
		droolsEngGroupRule.setConditions(treeVisitor.getCompleteExpression());
		// Set the special conditions/actions for the group rules
		droolsEngGroupRule.setParserResult(result);
		droolsEngGroupRule.setActions(droolsRule.getActions());
		newConditions.add(droolsEngGroupRule);
		return droolsEngGroupRule;
	}

	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		try {
			prattParserResult = prattParser.parseExpression();
		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), ex);
		}
		return prattParserResult;
	}

	public static DroolsHelper getDroolsHelper() {
		return droolsHelper;
	}

	public static void setDroolsHelper(DroolsHelper droolsHelper) {
		RuleToDroolsRule.droolsHelper = droolsHelper;
	}
}
