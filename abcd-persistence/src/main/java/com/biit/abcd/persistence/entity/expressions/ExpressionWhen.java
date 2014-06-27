package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_WHEN")
public class ExpressionWhen extends ExprEvent {

	public ExpressionWhen() {
		super();
	}

	@Override
	public void addDefaultChild() {
		addChild(new ExprPortLogic());
	}

	@Override
	public String getExpressionTableString() {
		return "WHEN ( " + getChildsAsString() + " )";
	}

}
