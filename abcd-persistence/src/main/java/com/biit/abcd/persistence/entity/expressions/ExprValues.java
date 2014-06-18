package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUES")
public class ExprValues extends ExprWithChilds {

	public ExprValues() {
		super();
	}

	@Override
	public String getExpressionTableString() {
		if (childs.isEmpty()) {
			return "[ ]";
		}
		String expression = new String();
		if (childs.size() == 1) {
			expression += childs.get(0).getExpressionTableString();
		} else {
			for (ExprBasic value : childs) {
				expression += value.getExpressionTableString() + ",";
			}
			expression = expression.substring(0, expression.length() - 2);
		}
		return expression;
	}

	public void addValue(ExprValue value) {
		childs.add(value);
	}

}
