package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprPortLogic extends ExprPort{

	public ExprPortLogic(String portName) {
		super(portName);
	}

	@Override
	public ExprBasic getDefaultExpression() {
		return new ExprWoChildLogic();
	}

	@Override
	public ExprOp getDefaultJoiner() {
		return new ExprOpLogic();
	}
	
	

}
