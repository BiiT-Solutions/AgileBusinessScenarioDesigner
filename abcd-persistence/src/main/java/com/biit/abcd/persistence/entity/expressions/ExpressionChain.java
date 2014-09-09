package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.form.TreeObject;
import com.biit.jexeval.ExpressionChecker;
import com.biit.jexeval.ExpressionEvaluator;

/**
 * A concatenation of expressions: values, operators, ... that defines a more
 * complex expression.
 */
@Entity
@Table(name = "expressions_chain")
public class ExpressionChain extends Expression implements INameAttribute {

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
	// orphanRemoval = true)
	// @OrderColumn(name = "expression_index")
	// private List<Expression> expressions;

	// For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
	// we cannot use the list of children with
	// @Orderby or @OrderColumn we use our own order manager.
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderBy(value = "sortSeq ASC")
	private List<Expression> expressions;

	private String name;

	public ExpressionChain() {
		expressions = new ArrayList<>();
	}

	public ExpressionChain(Expression... expressions) {
		this.expressions = new ArrayList<>();
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public ExpressionChain(List<Expression> expressions) {
		this.expressions = new ArrayList<>();
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public ExpressionChain(String name) {
		expressions = new ArrayList<>();
		setName(name);
	}

	public ExpressionChain(String name, Expression... expressions) {
		this.expressions = new ArrayList<>();
		setName(name);
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public void addExpression(Expression expression) {
		expressions.add(expression);
	}

	public void addExpression(int index, Expression expression) {
		expressions.add(index, expression);
	}

	public Expression removeFirstExpression() {
		return expressions.remove(0);
	}

	public void addExpressions(Expression... expressions) {
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	public void addExpressions(List<Expression> expressions) {
		for (Expression expression : expressions) {
			addExpression(expression);
		}
	}

	/**
	 * Some characters are not allowed in the Expression Evaluator.
	 *
	 * @param expression
	 * @return
	 */
	private String filterVariables(Expression expression) {
		return expression.getExpression().replaceAll("[^a-zA-Z0-9_]", "_");
	}

	@Override
	public ExpressionChain generateCopy() {
		ExpressionChain copy = new ExpressionChain();
		if (name != null) {
			copy.name = new String(name);
		}
		for (Expression expression : expressions) {
			Expression copyExpression = expression.generateCopy();
			copy.expressions.add(copyExpression);
		}
		return copy;
	}

	/**
	 * Returns the expression in string format that can be evaluated by a
	 * Expression Evaluator.
	 *
	 * @return
	 */
	@Override
	public String getExpression() {
		String result = "";
		for (int i = 0; i < expressions.size(); i++) {
			// Dots are not allowed in the Evaluator Expression.
			if ((expressions.get(i) instanceof ExpressionValueString)
					|| (expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (expressions.get(i) instanceof ExpressionValueCustomVariable)
					|| (expressions.get(i) instanceof ExpressionValueGlobalConstant)) {
				result += filterVariables(expressions.get(i)) + " ";
			} else {
				result += expressions.get(i).getExpression() + " ";
			}
		}
		return result.trim();
	}

	public ExpressionEvaluator getExpressionEvaluator() {
		ExpressionChecker evaluator = new ExpressionChecker(getExpression());
		List<String> definedVariables = new ArrayList<>();
		// Define variables.
		for (int i = 0; i < expressions.size(); i++) {
			if ((expressions.get(i) instanceof ExpressionValueString)
					|| (expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (expressions.get(i) instanceof ExpressionValueCustomVariable)
					|| (expressions.get(i) instanceof ExpressionValueGlobalConstant)
					|| (expressions.get(i) instanceof ExpressionValueGenericCustomVariable)
					|| (expressions.get(i) instanceof ExpressionValueGenericVariable)
					|| (expressions.get(i) instanceof ExpressionValueSystemDate)) {
				// Dots are not allowed.
				String varName = filterVariables(expressions.get(i));
				// Do not repeat variable declaration.
				if (!definedVariables.contains(varName)) {
					// Value is not needed for evaluation.
					String value = "1";
					evaluator.with(varName, value);
					definedVariables.add(varName);
				}
			}
		}
		return evaluator;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public String getName() {
		return name;
	}

	protected Set<TreeObject> getReferencedTreeObjects() {
		List<Expression> expressions = getExpressions();
		Set<TreeObject> references = new HashSet<>();
		for (Expression expression : expressions) {
			if (expression instanceof ExpressionValueTreeObjectReference) {
				references.add(((ExpressionValueTreeObjectReference) expression).getReference());
				continue;
			}
		}
		return references;
	}

	@Override
	public String getRepresentation() {
		if (expressions.isEmpty()) {
			return "null";
		}

		String result = "";
		for (Expression expression : expressions) {
			result += expression.getRepresentation() + " ";
		}
		return result.trim();
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = getReferencedTreeObjects();
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if (commonTreeObject.equals(treeObject)) {
				return true;
			}
		}
		return false;
	}

	public void removeAllExpressions() {
		expressions.clear();
	}

	public boolean removeExpression(Expression expression) {
		return expressions.remove(expression);
	}

	public void setExpressions(List<Expression> expressions) {
		removeAllExpressions();
		this.expressions.addAll(expressions);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void updateChildrenSortSeqs() {
		if (getExpressions() != null) {
			for (int i = 0; i < getExpressions().size(); i++) {
				Expression expression = getExpressions().get(i);
				expression.setSortSeq(i);
				if ((expression != null) && (expression instanceof ExpressionChain)) {
					((ExpressionChain) expression).updateChildrenSortSeqs();
				}
			}
		}
	}

	@Override
	public String toString() {
		return getName() + expressions;
	}
}
