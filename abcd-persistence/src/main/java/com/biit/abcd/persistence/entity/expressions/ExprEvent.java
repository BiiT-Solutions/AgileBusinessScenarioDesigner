package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_EVENT")
public abstract class ExprEvent extends ExprWithChilds {

	public abstract void addDefaultChild();

	public String getChildsAsString() {
		String childText = "";
		if (getChilds() != null && !getChilds().isEmpty()) {
			for (ExprBasic exprPort : getChilds()) {
				if (childText.length() > 0) {
					childText += ", ";
				}
				for (ExprBasic child : ((ExprPort) exprPort).getChilds()) {
					childText += child.getExpressionTableString();
				}
			}
		}
		return childText;
	}
}
