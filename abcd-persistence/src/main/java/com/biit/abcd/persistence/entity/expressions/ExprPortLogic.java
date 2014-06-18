package com.biit.abcd.persistence.entity.expressions;

public class ExprPortLogic extends ExprPort{

	public ExprPortLogic(String portName) {
		super(portName);
	}

	@Override
	public ExprBasic getDefaultExpression() {
		return new ExprAtomicLogic();
	}

	@Override
	public ExprOp getDefaultJoiner() {
		return new ExprOpLogic();
	}
	
	

}
