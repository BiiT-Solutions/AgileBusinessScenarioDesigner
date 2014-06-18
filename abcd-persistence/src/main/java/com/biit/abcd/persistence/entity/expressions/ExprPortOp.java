package com.biit.abcd.persistence.entity.expressions;

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
