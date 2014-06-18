package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_VALUE_EXPRESSION_REFERENCE")
public class ExprValueExprReference extends ExprValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private ExprBasic value;

	protected ExprValueExprReference() {
		super();
	}

	public ExprValueExprReference(ExprBasic value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
		return value.getExpressionTableString();
	}

	public ExprBasic getValue() {
		return value;
	}

	public void setValue(ExprBasic value) {
		this.value = value;
	}

}
