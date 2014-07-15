package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Defines a special symbol as '(', ')', or ','
 *
 */
@Entity
@Table(name = "EXPRESSION_SYMBOL")
public class ExpressionSymbol extends Expression {

	@Enumerated(EnumType.STRING)
	private AvailableSymbol value;

	public ExpressionSymbol() {
		super();
	}

	@Override
	public String getRepresentation() {
		if (value == null) {
			return "";
		} else {
			return value.getValue();
		}
	}

	public void setValue(AvailableSymbol value) {
		this.value = value;
	}

	public AvailableSymbol getValue() {
		return this.value;
	}

	@Override
	protected String getExpression() {
		return getRepresentation();
	}

	@Override
	public Expression generateCopy() {
		ExpressionSymbol copy = new ExpressionSymbol();
		copy.value = value;
		return copy;
	}
}
