package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_PORT_OPERATION")
public class ExprPortOp extends ExprPort {
	public ExprPortOp(String portName) {
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
