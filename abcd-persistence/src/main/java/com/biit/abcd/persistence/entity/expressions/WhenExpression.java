package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_WHEN")
public class WhenExpression extends ExprFunction {

	private static final String LOGIC_PORT = "logicPort";

	public WhenExpression() {
		super();
		addPort(LOGIC_PORT, new ExprPortLogic(LOGIC_PORT));
	}

	public ExprPort getLogicPort() {
		return getPort(LOGIC_PORT);
	}

	@Override
	public String getExpressionTableString() {
		return "WHEN ( " + getPort(LOGIC_PORT).getChildExpressionTableString() + " )";
	}

}
