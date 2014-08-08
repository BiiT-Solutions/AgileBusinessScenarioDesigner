package com.biit.abcd.persistence.entity.expressions;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.DateManager;

/**
 * Defines a value as a timestamp.
 * 
 */
@Entity
@Table(name = "EXPRESSION_VALUE_TIMESTAMP")
public class ExpressionValueTimestamp extends ExpressionValue {

	private Timestamp value;

	protected ExpressionValueTimestamp() {
		super();
	}

	public ExpressionValueTimestamp(Timestamp value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation() {
		if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	public Timestamp getValue() {
		return value;
	}

	public void setValue(Timestamp value) {
		this.value = value;
	}

	// TODO Check later
	@Override
	public String getExpression() {
		if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueTimestamp copy = new ExpressionValueTimestamp();
		copy.value = new Timestamp(value.getTime());
		return copy;
	}

}
