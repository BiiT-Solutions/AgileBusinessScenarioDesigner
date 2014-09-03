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

	public ExpressionValueSystemDate() {
		super();
	}

	@Override
	public String getRepresentation() {
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

	@Override
	public Expression generateCopy() {
		ExpressionValueTimestamp copy = new ExpressionValueSystemDate();
		return copy;
	}
}
