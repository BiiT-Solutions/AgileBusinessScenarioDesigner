package com.biit.abcd.webpages.elements.expressiontree.expression;

public abstract class ExprBasic {

	protected ExprBasic parent;

	public ExprBasic() {
	}

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

	protected String generateNullLabelCaption(String value) {
		return "<div style=\"background-color: rgb(179, 46, 46); color: rgb(255,255,255); display: inline;\">" + value
				+ "</div>";
	}
}
