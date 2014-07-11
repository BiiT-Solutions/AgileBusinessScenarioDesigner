package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.ITableCellEditable;
import com.biit.jexeval.ExpressionChecker;
import com.biit.jexeval.ExpressionEvaluator;

/**
 * A concatenation of expressions: values, operators, ... that defines a more complex expression.
 */
@Entity
@Table(name = "EXPRESSION_FORMS_EXPRESSION")
public class Expressions extends Expression implements ITableCellEditable {

	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "expression_index")
	private List<Expression> expressions;

	public Expressions() {
		expressions = new ArrayList<>();
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public void addExpression(Expression expression) {
		this.expressions.add(expression);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getRepresentation() {
		String result = "";
		for (Expression expression : expressions) {
			result += expression.getRepresentation() + " ";
		}
		return result.trim();
	}

	/**
	 * Returns the expression in string format that can be evaluated by a Expression Evaluator.
	 * 
	 * @return
	 */
	@Override
	protected String getExpression() {
		String result = "";
		for (int i = 0; i < expressions.size(); i++) {
			// Dots are not allowed in the Evaluator Expression.
			if ((expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (expressions.get(i) instanceof ExpressionValueFormCustomVariable)
					|| (expressions.get(i) instanceof ExpressionValueGlobalConstant)) {
				result += filterVariables(expressions.get(i));
			} else {
				result += expressions.get(i).getExpression();
			}
		}
		return result.trim();
	}

	public ExpressionEvaluator getExpressionEvaluator() {
		ExpressionChecker evaluator = new ExpressionChecker(getExpression());
		List<String> definedVariables = new ArrayList<>();
		// Define variables.
		for (int i = 0; i < expressions.size(); i++) {
			if ((expressions.get(i) instanceof ExpressionValueTreeObjectReference)
					|| (expressions.get(i) instanceof ExpressionValueFormCustomVariable)
					|| (expressions.get(i) instanceof ExpressionValueGlobalConstant)) {
				// Dots are not allowed.
				String varName = filterVariables(expressions.get(i));
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

	/**
	 * Some characters are not allowed in the Expression Evaluator.
	 * 
	 * @param expression
	 * @return
	 */
	private String filterVariables(Expression expression) {
		return expression.getExpression().replace(" ", "_").replace(".", "_").replace(":", "");
	}
}
