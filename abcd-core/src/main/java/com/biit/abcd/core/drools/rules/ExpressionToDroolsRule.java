package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.drools.engine.DroolsHelper;
import com.biit.form.entity.TreeObject;

/**
 * Transforms an Expression into a Drools rule.<br>
 * It also unwraps the generic variables that can be used in the expressions and
 * creates a set of rules if necessary.
 */
public class ExpressionToDroolsRule {

	public static List<DroolsRule> parse(DroolsRule droolsRule, DroolsHelper droolsHelper) throws ExpressionInvalidException, RuleNotImplementedException,
			InvalidRuleException, NotCompatibleTypeException {
		List<DroolsRule> droolsRules = null;

		if (droolsRule.getActions() != null && !droolsRule.getActions().getExpressions().isEmpty()) {
			// If the expression is composed by a generic variable, we have to
			// generate the set of rules that represents the generic
			if (droolsRule.getActions().getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable) {
				droolsRules = createExpressionDroolsRuleSet(droolsRule);
			} else {
				droolsRules = Arrays.asList(createExpressionDroolsRule(droolsRule));
			}
		} else {
			droolsRules = Arrays.asList(droolsRule);
		}
		// Check if the expression has an IF function
		if (droolsRules != null && !droolsRules.isEmpty() && hasIfCondition(droolsRules.get(0))) {
			List<DroolsRule> auxDroolsRules = parseIfRules(droolsRules);
			droolsRules = new ArrayList<DroolsRule>();
			for (DroolsRule auxRule : auxDroolsRules) {
				droolsRules.addAll(RuleToDroolsRule.parse(auxRule, droolsHelper));
			}
		}

		return droolsRules;
	}

	private static List<DroolsRule> createExpressionDroolsRuleSet(DroolsRule droolsRule) {

		List<DroolsRule> droolsRules = new ArrayList<DroolsRule>();
		ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) droolsRule.getActions()
				.getExpressions().get(0);
		List<TreeObject> treeObjects = new ArrayList<TreeObject>();
		switch (expressionValueGenericCustomVariable.getType()) {
		case CATEGORY:
			treeObjects.addAll(expressionValueGenericCustomVariable.getVariable().getForm().getChildren());
			break;
		case GROUP:
			for (TreeObject category : expressionValueGenericCustomVariable.getVariable().getForm().getChildren()) {
				List<TreeObject> groups = category.getAll(Group.class);
				// We need to reverse the groups to correctly generate the rules
				// for nested groups
				Collections.reverse(groups);
				treeObjects.addAll(groups);
			}
			break;
		case QUESTION_GROUP:
			for (TreeObject category : expressionValueGenericCustomVariable.getVariable().getForm().getChildren()) {
				List<TreeObject> groups = category.getAll(Group.class);
				// We need to reverse the groups to correctly generate the rules
				// for nested groups
				Collections.reverse(groups);
				List<TreeObject> groupQuestions = new ArrayList<TreeObject>();
				for (TreeObject group : groups) {
					groupQuestions.addAll(group.getChildren(Question.class));
				}
				treeObjects.addAll(groupQuestions);
			}
			break;
		case QUESTION_CATEGORY:
			for (TreeObject category : expressionValueGenericCustomVariable.getVariable().getForm().getChildren()) {
				treeObjects.addAll(category.getChildren(Question.class));
			}
			break;
		}
		// For each treeObject found, we generate the expression to create a new
		// rule
		if (treeObjects != null && !treeObjects.isEmpty()) {
			for (TreeObject treeObject : treeObjects) {
				ExpressionChain expressionChainCopy = (ExpressionChain) droolsRule.getActions().generateCopy();
				ExpressionValueCustomVariable expValCat = new ExpressionValueCustomVariable(treeObject, expressionValueGenericCustomVariable.getVariable());
				// Remove the generic
				expressionChainCopy.removeExpression(0);
				// Add the specific
				expressionChainCopy.addExpression(0, expValCat);
				// Add to the rule set
				DroolsRule createdDroolsRule = createExpressionDroolsRule(droolsRule, expressionChainCopy);
				if (createdDroolsRule != null) {
					droolsRules.add(createdDroolsRule);
				}
			}
		} else {
			// We don't want to create a rule if there is no children
			return null;
		}
		int expressionRuleIndex = 0;
		for (DroolsRule dRule : droolsRules) {
			dRule.setName(RuleGenerationUtils.createRuleName(droolsRule, "_" + expressionRuleIndex));
			expressionRuleIndex++;
		}
		return droolsRules;
	}

	private static DroolsRule createExpressionDroolsRule(DroolsRule droolsRule) {
		DroolsRule newDroolsRule = generateDroolsRule(droolsRule);
		newDroolsRule.setName(RuleGenerationUtils.createRuleName(droolsRule));
		newDroolsRule.setConditions(droolsRule.getConditions());
		// If the expression chain contains generic variables, we have to unwrap
		// them
		if (checkForGenericVariables(droolsRule.getActions())) {
			ExpressionChain expressionChainUnwrapped = unwrapGenericVariables(droolsRule.getActions());
			if (expressionChainUnwrapped != null) {
				newDroolsRule.setActions(expressionChainUnwrapped);
			} else {
				// We don't want to create a rule if there is no actions
				return null;
			}
		} else {
			newDroolsRule.setActions(droolsRule.getActions());
		}
		return newDroolsRule;
	}

	private static DroolsRule createExpressionDroolsRule(DroolsRule droolsRule, ExpressionChain actions) {
		DroolsRule newDroolsRule = generateDroolsRule(droolsRule);
		// newDroolsRule.setName(droolsRule.getName());
		newDroolsRule.setName(RuleGenerationUtils.createRuleName(droolsRule));
		newDroolsRule.setConditions(droolsRule.getConditions());
		// If the expression chain contains generic variables, we have to unwrap
		// them
		if (checkForGenericVariables(actions)) {
			ExpressionChain expressionChainUnwrapped = unwrapGenericVariables(actions);
			if (expressionChainUnwrapped != null) {
				newDroolsRule.setActions(expressionChainUnwrapped);
			} else {
				// We don't want to create a rule if there is no actions
				return null;
			}
		} else {
			newDroolsRule.setActions(actions);
		}
		return newDroolsRule;
	}

	private static List<DroolsRule> parseIfRules(List<DroolsRule> droolsRules) {
		List<DroolsRule> ifRules = new ArrayList<DroolsRule>();
		for (DroolsRule auxRule : droolsRules) {
			ifRules.addAll(parseIfRule(auxRule));
		}
		return ifRules;
	}

	private static List<DroolsRule> parseIfRule(DroolsRule droolsRule) {
		List<DroolsRule> droolsRules = new ArrayList<DroolsRule>();
		ExpressionChain ifCondition = new ExpressionChain();
		ExpressionChain ifActionThen = new ExpressionChain();
		ExpressionChain ifActionElse = new ExpressionChain();
		ExpressionChain expressionCopy = (ExpressionChain) droolsRule.getActions().generateCopy();
		// Set the first values of the if rules
		ifActionThen.addExpression(expressionCopy.getExpressions().get(0));
		ifActionThen.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		ifActionElse.addExpression(expressionCopy.getExpressions().get(0));
		ifActionElse.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		// Remove the left part of the expression
		expressionCopy.removeFirstExpression();
		// Remove the equals
		expressionCopy.removeFirstExpression();
		// Remove the if function
		expressionCopy.removeFirstExpression();
		// and the last parenthesis
		expressionCopy.removeLastExpression();

		int commaCounter = 0;
		int ifIndex = 0;
		for (Expression expression : expressionCopy.getExpressions()) {
			if ((expression instanceof ExpressionFunction)
					&& (((ExpressionFunction) expression).getValue().equals(AvailableFunction.IN) || ((ExpressionFunction) expression).getValue().equals(
							AvailableFunction.BETWEEN))) {
				commaCounter++;
			}
			if ((expression instanceof ExpressionSymbol) && ((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.RIGHT_BRACKET)
					&& commaCounter != 0) {
				commaCounter = 0;
			}
			if ((expression instanceof ExpressionSymbol) && ((ExpressionSymbol) expression).getValue().equals(AvailableSymbol.COMMA) && commaCounter == 0) {
				ifIndex++;
			} else {
				if (ifIndex == 0) {
					ifCondition.addExpression(expression);
				} else if (ifIndex == 1) {
					ifActionThen.addExpression(expression);
				} else if (ifIndex == 2) {
					ifActionElse.addExpression(expression);
				}
			}
		}
		// Creation of the rules that represent the if
		DroolsRule ifThenRule = new DroolsRule();
		ifThenRule.setName(RuleGenerationUtils.createRuleName(droolsRule, "_1stConditon"));
		ifThenRule.setConditions(ifCondition);
		ifThenRule.setActions(ifActionThen);
		droolsRules.add(ifThenRule);

		DroolsRule ifElseRule = new DroolsRule();
		ifElseRule.setName(RuleGenerationUtils.createRuleName(droolsRule, "_2ndConditon"));
		ExpressionChain negatedCondition = new ExpressionChain();
		// For the ELSE part we negate the Condition part
		negatedCondition.addExpression(new ExpressionFunction(AvailableFunction.NOT));
		negatedCondition.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		negatedCondition.addExpressions(ifCondition.getExpressions());
		negatedCondition.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		ifElseRule.setConditions(negatedCondition);
		ifElseRule.setActions(ifActionElse);
		droolsRules.add(ifElseRule);
		return droolsRules;
	}

	/**
	 * Checks if there are generic variables at the right side of the
	 * assignation expression
	 * 
	 * @return
	 */
	private static boolean checkForGenericVariables(ExpressionChain expressionChain) {
		for (Expression expression : expressionChain.getExpressions()) {
			if ((expression instanceof ExpressionValueGenericCustomVariable) || (expression instanceof ExpressionValueGenericVariable)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * We have to substitute the generic for the list of tree objects that
	 * represents
	 * 
	 * @param expressionChain
	 * @return
	 */
	private static ExpressionChain unwrapGenericVariables(ExpressionChain expressionChain) {
		// To avoid modifying the original expression
		ExpressionChain expressionChainCopy = (ExpressionChain) expressionChain.generateCopy();
		ExpressionValueCustomVariable expressionValueLeftTreeObject = (ExpressionValueCustomVariable) expressionChainCopy.getExpressions().get(0);

		// The rule is different if the variable to assign is a Form, a Category
		// a Group or a Question
		// This treeObject will limit the scope of the expression
		TreeObject leftTreeObject = expressionValueLeftTreeObject.getReference();
		ExpressionChain generatedExpressionChain = new ExpressionChain();
		for (int originalExpressionIndex = 0; originalExpressionIndex < expressionChainCopy.getExpressions().size(); originalExpressionIndex++) {
			Expression expression = expressionChainCopy.getExpressions().get(originalExpressionIndex);
			if (expression instanceof ExpressionValueGenericCustomVariable) {
				// Unwrap the generic variables being analyzed
				ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) expression;
				CustomVariable customVariableOfGeneric = expressionValueGenericCustomVariable.getVariable();
				List<TreeObject> treeObjects = getUnwrappedTreeObjects(expressionValueGenericCustomVariable, leftTreeObject);

				// We have to create a CustomVariable for each treeObject found
				// and add it to the expression
				if ((treeObjects != null) && (!treeObjects.isEmpty())) {
					for (TreeObject treeObject : treeObjects) {
						generatedExpressionChain.addExpression(new ExpressionValueCustomVariable(treeObject, customVariableOfGeneric));
						generatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
					}
					// Remove the last comma
					generatedExpressionChain.removeLastExpression();
				} else {
					// Remove the extra comma if there is no value
					if (originalExpressionIndex < expressionChainCopy.getExpressions().size() - 1
							&& (expressionChainCopy.getExpressions().get(originalExpressionIndex + 1) instanceof ExpressionSymbol)
							&& (((ExpressionSymbol) expressionChainCopy.getExpressions().get(originalExpressionIndex + 1)).getValue()
									.equals(AvailableSymbol.COMMA))) {
						expressionChainCopy.getExpressions().remove(originalExpressionIndex + 1);
					}
				}
			} else if (expression instanceof ExpressionValueGenericVariable) {
				// Unwrap the generic variables being analyzed
				List<TreeObject> treeObjects = getUnwrappedTreeObjects((ExpressionValueGenericVariable) expression, leftTreeObject);
				// We have to create a TreeObjecReference for each treeObject
				// found
				// and add it to the expression
				if ((treeObjects != null) && (!treeObjects.isEmpty())) {
					for (TreeObject treeObject : treeObjects) {
						generatedExpressionChain.addExpression(new ExpressionValueTreeObjectReference(treeObject));
						generatedExpressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
					}
					// Remove the last comma
					generatedExpressionChain.removeLastExpression();
				} else {
					// Remove the extra comma if there is no value
					if (originalExpressionIndex < expressionChainCopy.getExpressions().size() - 1
							&& (expressionChainCopy.getExpressions().get(originalExpressionIndex + 1) instanceof ExpressionSymbol)
							&& (((ExpressionSymbol) expressionChainCopy.getExpressions().get(originalExpressionIndex + 1)).getValue()
									.equals(AvailableSymbol.COMMA))) {
						expressionChainCopy.getExpressions().remove(originalExpressionIndex + 1);
					}
				}
			} else {
				// if it is not a generic variable, we copy the same value
				generatedExpressionChain.addExpression(expression);
			}
		}

		// Remove the last comma if there is one
		// Condition to avoid errors in the parser
		if (generatedExpressionChain.getExpressions().size() > 1
				&& ((generatedExpressionChain.getExpressions().get(generatedExpressionChain.getExpressions().size() - 2) instanceof ExpressionSymbol) && (((ExpressionSymbol) generatedExpressionChain
						.getExpressions().get(generatedExpressionChain.getExpressions().size() - 2)).getValue().equals(AvailableSymbol.COMMA)))) {
			generatedExpressionChain.removeExpression(generatedExpressionChain.getExpressions().size() - 2);
		}
		return generatedExpressionChain;
	}

	/**
	 * Return the TreeObject related to the generic variable and limited to the
	 * scope of the left TreeObject
	 * 
	 * @param expressionValueGenericVariable
	 * @param leftTreeObject
	 * @return
	 */
	private static List<TreeObject> getUnwrappedTreeObjects(ExpressionValueGenericVariable expressionValueGenericVariable, TreeObject leftTreeObject) {

		Map<TreeObjectType, TreeObject> lefTreeObjectLimit = new HashMap<TreeObjectType, TreeObject>();
		setLeftVariableMap(lefTreeObjectLimit, leftTreeObject);
		List<TreeObject> treeObjects = null;
		switch (expressionValueGenericVariable.getType()) {
		case CATEGORY:
			if (leftTreeObject instanceof Form) {
				treeObjects = leftTreeObject.getAll(Category.class);

			} else if (leftTreeObject instanceof Category) {
				// It is the same item (we don't want the brothers)
				treeObjects = Arrays.asList(leftTreeObject);

			} else if (leftTreeObject instanceof Question) {
				TreeObject category = lefTreeObjectLimit.get(TreeObjectType.CATEGORY);
				// We want the category that is the parent of the left object
				if (category != null)
					treeObjects = Arrays.asList(category);
			}
			break;
		case GROUP:
			if (leftTreeObject instanceof Form) {
				treeObjects = leftTreeObject.getAll(Group.class);

			} else if ((leftTreeObject instanceof Category) || (leftTreeObject instanceof Group)) {
				treeObjects = new ArrayList<TreeObject>(leftTreeObject.getChildren(Group.class));

			} else if (leftTreeObject instanceof Question) {
				TreeObject group = lefTreeObjectLimit.get(TreeObjectType.GROUP);
				if (group != null)
					treeObjects = Arrays.asList(group);
			}
			break;
		case QUESTION_CATEGORY:
			if (leftTreeObject instanceof Form) {
				List<TreeObject> categories = leftTreeObject.getAll(Category.class);
				if (categories != null && !categories.isEmpty()) {
					treeObjects = new ArrayList<TreeObject>();
					for (TreeObject category : categories) {
						treeObjects.addAll(category.getChildren(Question.class));
					}
				}
			} else if (leftTreeObject instanceof Category) {
				treeObjects = new ArrayList<TreeObject>(leftTreeObject.getChildren(Question.class));

			} else if (leftTreeObject instanceof Question) {
				treeObjects = Arrays.asList(leftTreeObject);
			}
			break;
		case QUESTION_GROUP:
			if ((leftTreeObject instanceof Form) || (leftTreeObject instanceof Category)) {
				List<TreeObject> groups = leftTreeObject.getAll(Group.class);
				if (groups != null && !groups.isEmpty()) {
					treeObjects = new ArrayList<TreeObject>();
					for (TreeObject group : groups) {
						treeObjects.addAll(group.getChildren(Question.class));
					}
				}
			} else if (leftTreeObject instanceof Group) {
				treeObjects = new ArrayList<TreeObject>(leftTreeObject.getChildren(Question.class));

			} else if (leftTreeObject instanceof Question) {
				treeObjects = Arrays.asList(leftTreeObject);
			}
			break;
		}
		return treeObjects;
	}

	private static DroolsRule generateDroolsRule(DroolsRule droolsRule) {
		DroolsRule newDroolsRule;
		if (droolsRule instanceof DroolsRuleGroupEndRule) {
			newDroolsRule = new DroolsRuleGroupEndRule();
			// Set the special conditions/actions for the group rules
			((DroolsRuleGroupEndRule) newDroolsRule).setParserResult(((DroolsRuleGroupEndRule) droolsRule).getParserResult());
			((DroolsRuleGroupEndRule) newDroolsRule).setRulesIdentifierMap(((DroolsRuleGroupEndRule) droolsRule).getRulesIdentifierMap());

		} else if (droolsRule instanceof DroolsRuleGroup) {
			newDroolsRule = new DroolsRuleGroup();
			((DroolsRuleGroup) newDroolsRule).setConditionExpressionChainId(((DroolsRuleGroup) droolsRule).getConditionExpressionChainId());

		} else {
			newDroolsRule = new DroolsRule();
		}
		return newDroolsRule;
	}

	private static boolean hasIfCondition(DroolsRule droolsRule) {
		return RuleGenerationUtils.searchClassInExpressionChain(droolsRule.getActions(), ExpressionFunction.class, AvailableFunction.IF);
	}

	private static void setLeftVariableMap(Map<TreeObjectType, TreeObject> lefTreeObjectLimit, TreeObject treeObject) {
		if (treeObject instanceof Category) {
			lefTreeObjectLimit.put(TreeObjectType.FORM, treeObject.getParent());
			lefTreeObjectLimit.put(TreeObjectType.CATEGORY, treeObject);
		} else if (treeObject instanceof Group) {
			// Not working for nested groups !!!!!!!!!!
			lefTreeObjectLimit.put(TreeObjectType.FORM, treeObject.getParent().getParent());
			lefTreeObjectLimit.put(TreeObjectType.CATEGORY, treeObject.getParent());
			lefTreeObjectLimit.put(TreeObjectType.GROUP, treeObject);
		} else if (treeObject instanceof Question) {
			if (treeObject.getParent() instanceof Category) {
				lefTreeObjectLimit.put(TreeObjectType.FORM, treeObject.getParent().getParent());
				lefTreeObjectLimit.put(TreeObjectType.CATEGORY, treeObject.getParent());
				lefTreeObjectLimit.put(TreeObjectType.QUESTION_CATEGORY, treeObject);
			} else {
				lefTreeObjectLimit.put(TreeObjectType.FORM, treeObject.getParent().getParent().getParent());
				lefTreeObjectLimit.put(TreeObjectType.CATEGORY, treeObject.getParent().getParent());
				lefTreeObjectLimit.put(TreeObjectType.GROUP, treeObject.getParent());
				lefTreeObjectLimit.put(TreeObjectType.QUESTION_GROUP, treeObject);
			}
		}
	}

	private enum TreeObjectType {
		FORM, CATEGORY, GROUP, QUESTION_CATEGORY, QUESTION_GROUP
	}
}
