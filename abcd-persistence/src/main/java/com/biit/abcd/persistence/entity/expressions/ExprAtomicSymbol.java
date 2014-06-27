package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_ATOMIC_SYMBOL")
public class ExprAtomicSymbol extends ExprAtomic {

	@Enumerated(EnumType.STRING)
	private ExprSymbol value;

	public ExprAtomicSymbol() {
		super();
	}

	@Override
	public String getExpressionTableString() {
		if (value == null) {
			return "";
		} else {
			return value.getCaption();
		}
	}

	public void setValue(ExprSymbol value) {
		this.value = value;
	}

	public ExprSymbol getValue() {
		return this.value;
	}

	@Override
	public String getExpression() {
		return getExpressionTableString();
	}
}
