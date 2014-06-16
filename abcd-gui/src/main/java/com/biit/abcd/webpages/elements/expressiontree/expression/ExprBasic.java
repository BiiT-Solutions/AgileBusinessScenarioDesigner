package com.biit.abcd.webpages.elements.expressiontree.expression;

public abstract class ExprBasic {

	protected ExprBasic parent;

	public abstract String getExpressionTableString();

	public ExprBasic getParent() {
		return parent;
	}

	protected ExprBasic getParentOfClass(Class<?> clazz) {
		if (parent == null) {
			return null;
		}
		if (clazz.isAssignableFrom(parent.getClass())) {
			return parent;
		}
		return parent.getParentOfClass(clazz);
	}
}
