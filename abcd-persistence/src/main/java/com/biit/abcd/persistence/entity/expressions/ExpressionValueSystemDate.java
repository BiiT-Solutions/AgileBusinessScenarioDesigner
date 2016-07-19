package com.biit.abcd.persistence.entity.expressions;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines current time when used in a rule.
 * 
 */
@Entity
@Table(name = "expression_value_systemdate")
public class ExpressionValueSystemDate extends ExpressionValueTimestamp {
	private static final long serialVersionUID = -8660891361751270777L;

	public ExpressionValueSystemDate() {
		super();
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		return "SystemDate";
	}

	@Override
	public Timestamp getValue() {
		return new Timestamp(System.currentTimeMillis());

	}

	@Override
	public String getExpression() {
		return "SystemDate";
	}
}
