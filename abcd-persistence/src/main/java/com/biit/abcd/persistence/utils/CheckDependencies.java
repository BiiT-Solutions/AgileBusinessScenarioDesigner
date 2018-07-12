package com.biit.abcd.persistence.utils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.diagram.DiagramRule;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.StorableObject;

public class CheckDependencies {

	public static void checkTreeObjectDependencies(TreeObject treeObject) throws DependencyExistException {
		if (treeObject != null) {
			Form form = (Form) treeObject.getAncestor(Form.class);
			if (form != null) {
				// Check the table rules
				for (TableRule tableRule : form.getTableRules()) {
					for (TableRuleRow tableRuleRow : tableRule.getRules()) {
						// Check conditions of the table rule row
						checkTreeObjectDependeciesInExpressionChain(tableRuleRow.getConditions(), treeObject);
						// Check actions of the table rule row
						checkTreeObjectDependeciesInExpressionChain(tableRuleRow.getAction(), treeObject);
					}
				}
				// Check the rules
				for (Rule rule : form.getRules()) {
					checkTreeObjectDependeciesInExpressionChain(rule.getConditions(), treeObject);
					checkTreeObjectDependeciesInExpressionChain(rule.getActions(), treeObject);
				}
				// Check the expressions
				for (ExpressionChain expressions : form.getExpressionChains()) {
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
	private static void checkTreeObjectDependeciesInExpressionChain(ExpressionChain expressionChain, TreeObject treeObject) throws DependencyExistException {
		if (expressionChain != null) {
			List<Expression> conditions = expressionChain.getExpressions();
			for (Expression condition : conditions) {
				if (condition instanceof ExpressionValueTreeObjectReference) {
					TreeObject reference = ((ExpressionValueTreeObjectReference) condition).getReference();
					if ((reference != null) && Objects.equals(reference, treeObject)) {
						throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName() + ", referenced in the form.");
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
	private static void checkTreeObjectDependenciesInDiagram(Diagram diagram, TreeObject treeObject) throws DependencyExistException {
		if (diagram != null) {
			Set<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
			for (DiagramObject diagramObject : diagramObjectsList) {
				if (diagramObject instanceof DiagramLink) {
					checkTreeObjectDependeciesInExpressionChain(((DiagramLink) diagramObject).getExpressionChain(), treeObject);
				} else if (diagramObject instanceof DiagramFork) {
					if (Objects.equals(((DiagramFork) diagramObject).getReference(), treeObject)) {
						throw new DependencyExistException("Cannot delete " + treeObject.getClass().getName() + ", referenced in the form.");
					}
				} else if (diagramObject instanceof DiagramChild) {
					checkTreeObjectDependenciesInDiagram(((DiagramChild) diagramObject).getDiagram(), treeObject);
				}
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
	private static void checkTableRuleDependenciesInDiagram(Diagram diagram, TableRule tableRule) throws DependencyExistException {
		if (diagram != null) {
			Set<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
			for (DiagramObject diagramObject : diagramObjectsList) {
				if (diagramObject instanceof DiagramTable) {
					if (Objects.equals(((DiagramTable) diagramObject).getTable(), tableRule)) {
						throw new DependencyExistException("Cannot delete " + tableRule.getClass().getName() + ", with name: " + tableRule.getName()
								+ " referenced in the form.");
					}
				} else if (diagramObject instanceof DiagramChild) {
					checkTableRuleDependenciesInDiagram(((DiagramChild) diagramObject).getDiagram(), tableRule);
				}
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
		if (diagram != null) {
			Set<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
			for (DiagramObject diagramObject : diagramObjectsList) {
				if (diagramObject instanceof DiagramRule) {
					if (Objects.equals(((DiagramRule) diagramObject).getRule(), rule)) {
						throw new DependencyExistException("Cannot delete " + rule.getClass().getName() + ", with name: " + rule.getName()
								+ " referenced in the form.");
					}
				} else if (diagramObject instanceof DiagramChild) {
					checkRuleDependenciesInDiagram(((DiagramChild) diagramObject).getDiagram(), rule);
				}
			}
		}
	}

	public static void checkExpressionDependencies(Form form, ExpressionChain expressionChain) throws DependencyExistException {
		// Only dependency possible in the diagrams
		for (Diagram diagram : form.getDiagrams()) {
			checkExpressionChainDependenciesInDiagram(diagram, expressionChain);
		}
	}

	/**
	 * Look up for rule dependencies inside the diagram<br>
	 * 
	 * @param diagram
	 * @throws DependencyExistException
	 */
	private static void checkExpressionChainDependenciesInDiagram(Diagram diagram, ExpressionChain expressionChain) throws DependencyExistException {
		if (diagram != null) {
			Set<DiagramObject> diagramObjectsList = diagram.getDiagramObjects();
			for (DiagramObject diagramObject : diagramObjectsList) {
				if (diagramObject instanceof DiagramExpression) {
					if (((DiagramExpression) diagramObject).getExpression() != null
							&& Objects.equals(((DiagramExpression) diagramObject).getExpression(), expressionChain)) {
						throw new DependencyExistException("Cannot delete " + expressionChain.getClass().getName() + ", with name: "
								+ expressionChain.getName() + " referenced in the form.");
					}
				} else if (diagramObject instanceof DiagramChild) {
					checkExpressionChainDependenciesInDiagram(((DiagramChild) diagramObject).getDiagram(), expressionChain);
				}
			}
		}
	}

	/**
	 * Look up for custom variables.<br>
	 * 
	 * @param diagram
	 * @throws DependencyExistException
	 */
	public static void checkCustomVariableDependencies(Form form, CustomVariable customVariable) throws DependencyExistException {
		for (ExpressionChain expressionChain : form.getExpressionChains()) {
			for (StorableObject storableObject : expressionChain.getAllInnerStorableObjects()) {
				if (storableObject instanceof Expression) {
					checkCustomVariableDependencies((Expression) storableObject, customVariable);
				}
			}
		}

		for (Rule rule : form.getRules()) {
			for (StorableObject storableObject : rule.getAllInnerStorableObjects()) {
				if (storableObject instanceof Expression) {
					checkCustomVariableDependencies((Expression) storableObject, customVariable);
				}
			}
		}

		for (TableRule tableRule : form.getTableRules()) {
			for (StorableObject storableObject : tableRule.getAllInnerStorableObjects()) {
				if (storableObject instanceof Expression) {
					checkCustomVariableDependencies((Expression) storableObject, customVariable);
				}
			}
		}

		for (Diagram diagram : form.getDiagrams()) {
			for (StorableObject storableObject : diagram.getAllInnerStorableObjects()) {
				if (storableObject instanceof Expression) {
					checkCustomVariableDependencies((Expression) storableObject, customVariable);
				}
			}
		}

	}

	private static void checkCustomVariableDependencies(Expression expression, CustomVariable customVariable) throws DependencyExistException {
		if (expression instanceof ExpressionValueCustomVariable) {
			if (Objects.equals(((ExpressionValueCustomVariable) expression).getVariable(), customVariable)) {
				throw new DependencyExistException("Cannot delete custom variable '" + customVariable.getName() + "'. Used in expression '"
						+ expression.getRepresentation(true) + "'");
			}
		}

		if (expression instanceof ExpressionValueGenericCustomVariable) {
			if (Objects.equals(((ExpressionValueGenericCustomVariable) expression).getVariable(), customVariable)) {
				throw new DependencyExistException("Cannot delete custom variable '" + customVariable.getName() + "'. Used in expression '"
						+ expression.getRepresentation(true) + "'");
			}
		}
	}
}
