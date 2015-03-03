package com.biit.abcd.core.utils;

import java.util.HashMap;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.TreeObject;

public class UsesOfElement {
	private Form form;
	private HashMap<TreeObject, Integer> usesOfElement;

	public UsesOfElement(Form form) {
		this.form = form;
		usesOfElement = new HashMap<>();
		initialize();
	}

	private void initialize() {
		initializeExpressionsChainUsing();
		initializeRuleUsing();
		initializeTableRulesUsing();
		initializeDiagramUsing();
	}

	private void initializeDiagramUsing() {
		for (Diagram diagram : form.getDiagrams()) {
			initializeDiagramUsing(diagram);
		}
	}

	private void initializeDiagramUsing(Diagram diagram) {
		for (Diagram diagramChild : diagram.getChildDiagrams()) {
			initializeDiagramUsing(diagramChild);
		}
		for (DiagramObject diagramObject : diagram.getDiagramObjects()) {
			initializeDiagramObjectUsing(diagramObject);
		}
	}

	private void initializeDiagramObjectUsing(DiagramObject diagramObject) {
		if (diagramObject instanceof DiagramFork) {
			initializeDiagramForkUsing((DiagramFork) diagramObject);
		}
	}

	private void initializeDiagramForkUsing(DiagramFork diagramObject) {
		if (diagramObject.getReference() != null && diagramObject.getReference().getReference() != null) {
			increaseTreeObjectCounter(diagramObject.getReference().getReference());
		}
	}

	private void initializeTableRulesUsing() {
		for (TableRule tableRule : form.getTableRules()) {
			initializeTableRulesUsing(tableRule);
		}
	}

	private void initializeTableRulesUsing(TableRule tableRule) {
		for (TableRuleRow rows : tableRule.getRules()) {
			initializeTableRuleRowsUsing(rows);
		}
	}

	private void initializeTableRuleRowsUsing(TableRuleRow tableRuleRow) {
		initializeExpressionChainUsing(tableRuleRow.getConditions());
		initializeExpressionChainUsing(tableRuleRow.getAction());
	}

	private void initializeRuleUsing() {
		for (Rule rule : form.getRules()) {
			initializeRulesUsing(rule);
		}
	}

	private void initializeRulesUsing(Rule rule) {
		if (rule != null) {
			initializeExpressionChainUsing(rule.getConditions());
			initializeExpressionChainUsing(rule.getActions());
		}
	}

	private void initializeExpressionsChainUsing() {
		for (ExpressionChain expressionChain : form.getExpressionChains()) {
			initializeExpressionChainUsing(expressionChain);
		}
	}

	private void initializeExpressionChainUsing(ExpressionChain expressionChain) {
		if (expressionChain != null) {
			for (Expression expression : expressionChain.getExpressions()) {
				initializeExpressionUsing(expression);
			}
		}
	}

	private void initializeExpressionUsing(Expression expression) {
		if (expression instanceof ExpressionValueTreeObjectReference) {
			TreeObject reference = ((ExpressionValueTreeObjectReference) expression).getReference();
			increaseTreeObjectCounter(reference);
		} else if (expression instanceof ExpressionChain) {
			initializeExpressionChainUsing((ExpressionChain) expression);
		}
	}

	private void increaseTreeObjectCounter(TreeObject element) {
		if (element != null) {
			usesOfElement.put(element, getUsesOfElement(element) + 1);
		}
	}

	public int getUsesOfElement(TreeObject element) {
		if (element == null) {
			return 0;
		}
		if (usesOfElement.get(element) == null) {
			usesOfElement.put(element, 0);
		}
		return usesOfElement.get(element);
	}
}
