package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_PORT_MATH")
public class ExprPortMath extends ExprPort {

	protected ExprPortMath() {
		super();
	}

	public ExprPortMath(String portName) {
		super(portName);
	}

	@Override
	public ExprOp getDefaultJoiner() {
		return new ExprOpMath();
	}

	@Override
	public ExprBasic getDefaultExpression() {
		return new ExprAtomicMath();
	}
}
