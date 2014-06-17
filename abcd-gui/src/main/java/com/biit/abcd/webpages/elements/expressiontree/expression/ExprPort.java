package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class ExprPort extends ExprWChild {

	private String portName;

	public ExprPort(String portName) {
		super();
		this.portName = portName;
		addChildExpression(getDefaultExpression());
	}

	public String getName() {
		return portName;
	}

	@Override
	public void addChildExpression() {
		addChildExpression(getDefaultJoiner(), getDefaultExpression());
	}

	@Override
	public String getExpressionTableString() {
		return portName;
	}

	public abstract ExprJoint getDefaultJoiner();

	public abstract ExprBasic getDefaultExpression();

	@Override
	public void removeParenthesis() {
		// This operation won't be performed in the port
	}

	@Override
	protected List<ExprBasic> removeChildExpression(ExprBasic expression) {
		if (childs.size() == 1) {
			List<ExprBasic> elementsRemoved = new ArrayList<>();
			elementsRemoved.add(childs.remove(0));
			addChildExpression(getDefaultExpression());
			return elementsRemoved;
		} else {
			return super.removeChildExpression(expression);
		}
	}
}
