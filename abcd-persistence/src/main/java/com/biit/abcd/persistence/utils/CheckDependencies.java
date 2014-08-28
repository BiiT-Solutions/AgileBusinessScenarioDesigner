package com.biit.abcd.persistence.utils;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;

public class CheckDependencies {

	public static void checkDependencies(TreeObject treeObject) throws DependencyExistException {
		if (treeObject != null) {
			Form form = (Form) treeObject.getForm();
			if (form != null) {
				// Check the table rules
				for (TableRule tableRule : form.getTableRules()) {
					for (TableRuleRow tableRuleRow : tableRule.getRules()) {
						// Check conditions of the table rule row
						checkDependeciesInExpressionChain(tableRuleRow.getConditionsChain(), treeObject);
						// Check actions of the table rule row
						checkDependeciesInExpressionChain(tableRuleRow.getAction(), treeObject);
					}
				}
				// Check the rules
				for (Rule rule : form.getRules()) {
					checkDependeciesInExpressionChain(rule.getCondition(), treeObject);
					checkDependeciesInExpressionChain(rule.getActions(), treeObject);
				}
				// Check the expressions
				for (ExpressionChain expressions : form.getExpressionChain()) {
					checkDependeciesInExpressionChain(expressions, treeObject);
				}
				// Check the diagrams
				for (Diagram diagram : form.getDiagrams()) {
					checkDependenciesInDiagram(diagram, treeObject);
				}
			}
		}
	}

	/**
	 * Check dependencies inside the expression chain hierarchy
	 *
	 * @param expressionChain
	 * @throws DependencyExistException
	 */
	private static void checkDependeciesInExpressionChain(ExpressionChain expressionChain, TreeObject treeObject)
			throws DependencyExistException {
		if (expressionChain != null) {
			List<Expression> conditions = expressionChain.getExpressions();
			for (Expression condition : conditions) {
				if (condition instanceof ExpressionValueTreeObjectReference) {
					if (((ExpressionValueTreeObjectReference) condition).getReference().equals(treeObject)) {
						throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName()
								+ ", referenced in the form.");
					}
				} else if (condition instanceof ExpressionChain) {
					checkDependeciesInExpressionChain((ExpressionChain) condition, treeObject);
				}
			}
		}
	}

	/**
	 * Check dependencies inside the diagram<br>
	 * Only three node types can create new dependencies that hasn't been
	 * checked yet:<br>
	 * - DiagramLink<br>
	 * - DiagramFork<br>
	 * - DiagramChild: to check internal dependencies<br>
	 *
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkDependenciesInDiagram(Diagram diagram, TreeObject treeObject)
			throws DependencyExistException {
		List<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjectsList) {
			if (diagramObject instanceof DiagramLink) {
				checkDependeciesInExpressionChain(((DiagramLink) diagramObject).getExpressionChain(), treeObject);
			} else if (diagramObject instanceof DiagramFork) {
				if (((DiagramFork) diagramObject).getReference().equals(treeObject)) {
					throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName()
							+ ", referenced in the form.");
				}
			} else if (diagramObject instanceof DiagramChild) {
				checkDependenciesInDiagram(((DiagramChild) diagramObject).getChildDiagram(), treeObject);
			}
		}
	}
}
