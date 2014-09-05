package com.biit.abcd.persistence.entity.expressions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.persistence.utils.DateManager;

/**
 * Defines a value as a timestamp.
 */
@Entity
@Table(name = "expression_value_timestamp")
public class ExpressionValueTimestamp extends ExpressionValue {
	private Timestamp value;
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("d/MM/yy");

	protected ExpressionValueTimestamp() {
		super();
	}

	public ExpressionValueTimestamp(String value) throws ParseException {
		super();
		Date date = DATE_FORMATTER.parse(value);
		setValue(new Timestamp(date.getTime()));
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

	@Override
	public Timestamp getValue() {
		return value;
	}

	public void setValue(Timestamp value) {
		this.value = value;
	}

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

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof Timestamp)) {
			throw new NotValidExpressionValue("Expected Timestamp object in '" + value + "'");
		}
		setValue((Timestamp) value);
	}
}
