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
public class Expressions extends Expression implements ITableCellEditable{

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
	public String getExpressionTableString() {
		String result = "";
		for (Expression expression : expressions) {
			result += expression.getExpressionTableString() + " ";
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
		for (Expression expression : expressions) {
			// Dots are not allowed in the Evaluator Expression.
			if ((expression instanceof ExpressionValueFormCustomVariable)
					|| (expression instanceof ExpressionValueGlobalConstant)) {
				result += expression.getExpression().replace(" ", "_").replace(".", "_").replace(":", "") + " ";
			} else {
				result += expression.getExpression() + " ";
			}
		}
		return result.trim();
	}

	public ExpressionEvaluator getExpressionEvaluator() {
		ExpressionChecker evaluator = new ExpressionChecker(getExpression());
		// Define variables.
		for (Expression expression : expressions) {
			if ((expression instanceof ExpressionValueFormCustomVariable)
					|| (expression instanceof ExpressionValueGlobalConstant)) {
				// Dots are not allowed.
				String varName = expression.getExpression().replace(" ", "_").replace(".", "_").replace(":", "");
				// Value is not needed for evaluation.
				String value = "1";
				evaluator.with(varName, value);
			}
		}
		return evaluator;

	}
}
