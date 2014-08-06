package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.jexeval.ExpressionChecker;
import com.biit.jexeval.ExpressionEvaluator;

/**
 * A concatenation of expressions: values, operators, ... that defines a more
 * complex expression.
 */
@Entity
@Table(name = "EXPRESSIONS_CHAIN")
public class ExpressionChain extends Expression implements INameAttribute {

	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "expression_index")
	private List<Expression> expressions;

	public ExpressionChain() {
		this.expressions = new ArrayList<>();
	}

	public ExpressionChain(String name) {
		this.expressions = new ArrayList<>();
		this.setName(name);
	}

	public ExpressionChain(Expression ...expressions){
		this.expressions = new ArrayList<>();
		for(Expression expression : expressions){
			this.addExpression(expression);
		}
	}

	public ExpressionChain(String name, Expression ...expressions) {
		this.expressions = new ArrayList<>();
		this.setName(name);
		for(Expression expression : expressions){
			this.addExpression(expression);
		}
	}

	public List<Expression> getExpressions() {
		return this.expressions;
	}

	public boolean removeExpression(Expression expression) {
		return this.expressions.remove(expression);
	}

	public void setExpressions(List<Expression> expressions) {
		this.removeAllExpressions();
		this.expressions.addAll(expressions);
	}

	public void addExpression(Expression expression) {
		this.expressions.add(expression);
	}

	public void addExpressions(Expression ...expressions) {
		for(Expression expression : expressions){
			this.addExpression(expression);
		}
	}

	public void removeAllExpressions() {
		this.expressions.clear();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getRepresentation() {
		if (this.expressions.isEmpty()) {
			return "null";
		}

		String result = "";
		for (Expression expression : this.expressions) {
			result += expression.getRepresentation() + " ";
		}
		return result.trim();
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
		for (int i = 0; i < this.expressions.size(); i++) {
			// Dots are not allowed in the Evaluator Expression.
			if ((this.expressions.get(i) instanceof ExpressionValueString)
					|| (this.expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (this.expressions.get(i) instanceof ExpressionValueCustomVariable)
					|| (this.expressions.get(i) instanceof ExpressionValueGlobalConstant)) {
				result += this.filterVariables(this.expressions.get(i)) + " ";
			} else {
				result += this.expressions.get(i).getExpression();
			}
		}
		return result.trim();
	}

	public ExpressionEvaluator getExpressionEvaluator() {
		ExpressionChecker evaluator = new ExpressionChecker(this.getExpression());
		List<String> definedVariables = new ArrayList<>();
		// Define variables.
		for (int i = 0; i < this.expressions.size(); i++) {
			if ((this.expressions.get(i) instanceof ExpressionValueString)
					|| (this.expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (this.expressions.get(i) instanceof ExpressionValueCustomVariable)
					|| (this.expressions.get(i) instanceof ExpressionValueGlobalConstant)) {
				// Dots are not allowed.
				String varName = this.filterVariables(this.expressions.get(i));
				// Do not repeat variable declaration.
				if (!definedVariables.contains(varName)) {
					// Value is not needed for evaluation.
					String value = "1";
					evaluator.with(varName, value);
				}
			}
		}
		return evaluator;
	}

	@Override
	public String toString() {
		return this.getName() + this.expressions;
	}

	/**
	 * Some characters are not allowed in the Expression Evaluator.
	 * 
	 * @param expression
	 * @return
	 */
	private String filterVariables(Expression expression) {
		return expression.getExpression().replace(" ", "_").replace(".", "_").replace(":", "");
	}

	@Override
	public ExpressionChain generateCopy() {
		ExpressionChain copy = new ExpressionChain();
		if (this.name != null) {
			copy.name = new String(this.name);
		}
		for (Expression expression : this.expressions) {
			Expression copyExpression = expression.generateCopy();
			copy.expressions.add(copyExpression);
		}
		return copy;
	}

	protected Set<TreeObject> getReferencedTreeObjects() {
		List<Expression> expressions = this.getExpressions();
		Set<TreeObject> references = new HashSet<>();
		for (Expression expression : expressions) {
			if (expression instanceof ExpressionValueTreeObjectReference) {
				references.add(((ExpressionValueTreeObjectReference) expression).getReference());
				continue;
			}
		}
		return references;
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = this.getReferencedTreeObjects();
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if(commonTreeObject.equals(treeObject)){
				return true;
			}
		}
		return false;
	}
}
