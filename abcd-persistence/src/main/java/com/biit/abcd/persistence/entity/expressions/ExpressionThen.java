package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_THEN")
public class ExpressionThen extends ExprFunction {

	private static final String EXPR_PORT = "exprPort";

	public ExpressionThen() {
		super();
		addPort(EXPR_PORT, new ExprPortMath(EXPR_PORT));
	}

	@Override
	public String getExpressionTableString() {
		return "THEN ( " + getPort(EXPR_PORT).getChildExpressionTableString() + " )";
	}

}
