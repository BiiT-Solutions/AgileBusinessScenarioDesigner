package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_OPERATION_MATH")
public class ExprOpMath extends ExprOp {

	public ExprOpMath() {
		super();
		getAcceptedValues().add(new ExprOpValue("=", "="));
		getAcceptedValues().add(new ExprOpValue("+", "+"));
		getAcceptedValues().add(new ExprOpValue("-", "-"));
		getAcceptedValues().add(new ExprOpValue("*", "*"));
		getAcceptedValues().add(new ExprOpValue("/", "/"));
		getAcceptedValues().add(new ExprOpValue("%", "%"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("expr-join");
	}

}
