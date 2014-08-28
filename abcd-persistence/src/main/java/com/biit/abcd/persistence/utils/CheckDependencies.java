package com.biit.abcd.persistence.utils;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;

public class CheckDependencies {

	public static void checkTreeObjectDependencies(TreeObject treeObject) throws DependencyExistException {
		if (treeObject != null) {
			Form form = (Form) treeObject.getForm();
			if (form != null) {
				// Check the table rules
				for (TableRule tableRule : form.getTableRules()) {
					for (TableRuleRow tableRuleRow : tableRule.getRules()) {
						// Check conditions of the table rule row
						checkTreeObjectDependeciesInExpressionChain(tableRuleRow.getConditionsChain(), treeObject);
						// Check actions of the table rule row
						checkTreeObjectDependeciesInExpressionChain(tableRuleRow.getAction(), treeObject);
					}
				}
				// Check the rules
				for (Rule rule : form.getRules()) {
					checkTreeObjectDependeciesInExpressionChain(rule.getCondition(), treeObject);
					checkTreeObjectDependeciesInExpressionChain(rule.getActions(), treeObject);
				}
				// Check the expressions
				for (ExpressionChain expressions : form.getExpressionChain()) {
					checkTreeObjectDependeciesInExpressionChain(expressions, treeObject);
				}
				// Check the diagrams
				for (Diagram diagram : form.getDiagrams()) {
					checkTreeObjectDependenciesInDiagram(diagram, treeObject);
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
	private static void checkTreeObjectDependeciesInExpressionChain(ExpressionChain expressionChain,
			TreeObject treeObject) throws DependencyExistException {
		if (expressionChain != null) {
			List<Expression> conditions = expressionChain.getExpressions();
			for (Expression condition : conditions) {
				if (condition instanceof ExpressionValueTreeObjectReference) {
					if (((ExpressionValueTreeObjectReference) condition).getReference().equals(treeObject)) {
						throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName()
								+ ", referenced in the form.");
					}
				} else if (condition instanceof ExpressionChain) {
					checkTreeObjectDependeciesInExpressionChain((ExpressionChain) condition, treeObject);
				}
			}
		}
	}

	/**
	 * Look for tree object dependencies inside the diagram<br>
	 * Only three node types can create new dependencies that hasn't been
	 * checked yet:<br>
	 * - DiagramLink<br>
	 * - DiagramFork<br>
	 * - DiagramChild: to check internal dependencies<br>
	 *
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkTreeObjectDependenciesInDiagram(Diagram diagram, TreeObject treeObject)
			throws DependencyExistException {
		List<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjectsList) {
			if (diagramObject instanceof DiagramLink) {
				checkTreeObjectDependeciesInExpressionChain(((DiagramLink) diagramObject).getExpressionChain(),
						treeObject);
			} else if (diagramObject instanceof DiagramFork) {
				if (((DiagramFork) diagramObject).getReference().equals(treeObject)) {
					throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName()
							+ ", referenced in the form.");
				}
			} else if (diagramObject instanceof DiagramChild) {
				checkTreeObjectDependenciesInDiagram(((DiagramChild) diagramObject).getChildDiagram(), treeObject);
			}
		}
	}

	public static void checkTableRuleDependencies(Form form, TableRule tableRule) throws DependencyExistException {
		// Only dependency possible in the diagrams
		for (Diagram diagram : form.getDiagrams()) {
			checkTableRuleDependenciesInDiagram(diagram, tableRule);
		}
	}

	/**
	 * Look for table rule dependencies inside the diagram<br>
	 *
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkTableRuleDependenciesInDiagram(Diagram diagram, TableRule tableRule)
			throws DependencyExistException {
		List<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjectsList) {
			if (diagramObject instanceof DiagramTable) {
				if (((DiagramTable) diagramObject).getTable().equals(tableRule)) {
					throw new DependencyExistException("Cannot delete " + tableRule.getClass().getName()
							+ ", with name: " + tableRule.getName() + " referenced in the form.");
				}
			} else if (diagramObject instanceof DiagramChild) {
				checkTableRuleDependenciesInDiagram(((DiagramChild) diagramObject).getChildDiagram(), tableRule);
			}
		}
	}

	public static void checkRulesDependencies(Form form, Rule rule) throws DependencyExistException {
		// Only dependency possible in the diagrams
		for (Diagram diagram : form.getDiagrams()) {
			checkRuleDependenciesInDiagram(diagram, rule);
		}
	}

	/**
	 * Look for rule dependencies inside the diagram<br>
	 *
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkRuleDependenciesInDiagram(Diagram diagram, Rule rule) throws DependencyExistException {
		List<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjectsList) {
			if (diagramObject instanceof DiagramRule) {
				if (((DiagramRule) diagramObject).getRule().equals(rule)) {
					throw new DependencyExistException("Cannot delete " + rule.getClass().getName() + ", with name: "
							+ rule.getName() + " referenced in the form.");
				}
			} else if (diagramObject instanceof DiagramChild) {
				checkRuleDependenciesInDiagram(((DiagramChild) diagramObject).getChildDiagram(), rule);
			}
		}
	}

	public static void checkExpressionDependencies(Form form, ExpressionChain expressionChain)
			throws DependencyExistException {
		// Only dependency possible in the diagrams
		for (Diagram diagram : form.getDiagrams()) {
			checkExpressionChainDependenciesInDiagram(diagram, expressionChain);
		}
	}

	/**
	 * Look for rule dependencies inside the diagram<br>
	 *
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkExpressionChainDependenciesInDiagram(Diagram diagram, ExpressionChain expressionChain)
			throws DependencyExistException {
		List<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
		for (DiagramObject diagramObject : diagramObjectsList) {
			if (diagramObject instanceof DiagramCalculation) {
				if (((DiagramCalculation) diagramObject).getFormExpression().equals(expressionChain)){
					throw new DependencyExistException("Cannot delete " + expressionChain.getClass().getName() + ", with name: "
							+ expressionChain.getName() + " referenced in the form.");
				}
			} else if (diagramObject instanceof DiagramChild) {
				checkExpressionChainDependenciesInDiagram(((DiagramChild) diagramObject).getChildDiagram(), expressionChain);
			}
		}
	}
}
