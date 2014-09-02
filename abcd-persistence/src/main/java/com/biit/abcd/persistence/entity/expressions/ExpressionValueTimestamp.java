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
@Table(name = "expression_value_timestamp")
public class ExpressionValueTimestamp extends ExpressionValue {

	private Timestamp value;
	// specifies if the created expression must return the current system date
	private boolean systemDate = false;

	protected ExpressionValueTimestamp() {
		super();
	}

	public ExpressionValueTimestamp(Timestamp value) {
		super();
		setValue(value);
	}

	/**
	 * Does not store any value<br>
	 * When the return is called, it returns the current system date.
	 *
	 * @param systemDate
	 */
	public ExpressionValueTimestamp(boolean systemDate) {
		super();
		this.systemDate = systemDate;
	}

	@Override
	public String getRepresentation() {
		if (systemDate) {
			return "SystemDate";
		} else if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	public Timestamp getValue() {
		if (systemDate) {
			return new Timestamp(System.currentTimeMillis());
		} else {
			return value;
		}
	}

	public void setValue(Timestamp value) {
		this.value = value;
	}

	@Override
	public String getExpression() {
		if (systemDate) {
			return "SystemDate";
		} else if (value != null) {
			return DateManager.convertDateToString(value);
		} else {
			return "";
		}
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueTimestamp copy = new ExpressionValueTimestamp();
		if (!systemDate) {
			copy.value = new Timestamp(value.getTime());
		}
		copy.systemDate = systemDate;
		return copy;
	}

}
