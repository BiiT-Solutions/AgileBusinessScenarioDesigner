package com.biit.abcd.webpages.elements.expressiontree.expression;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.TreeObject;

public class ExprLeftOperand extends ExprBasic {

	private TreeObject treeElement;
	private CustomVariable variable;

	public ExprLeftOperand(TreeObject treeElement, CustomVariable variable) {
		this.treeElement = treeElement;
		this.variable = variable;
	}

	@Override
	public String getExpressionTableString() {
		if (treeElement == null) {
			return generateNullLabelCaption("LeftOperand");
		} else {
			String expression = new String();
			expression += treeElement.getName();
			if (variable != null) {
				expression += "." + variable.getName();
			}
			return expression;
		}
	}

}
