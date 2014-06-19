package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_THEN")
public class ExpressionThen extends ExprFunction {
	private String name;

	public ExpressionThen() {
		super();
	}

	@Override
	public void addDefaultChild() {
		addChild(new ExprPortMath());
	}

	@Override
	public String getExpressionTableString() {
		return "THEN ( " + getChildsAsString() + " )";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
