package com.biit.abcd.persistence.entity.expressions;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.DateManager;

@Entity
@Table(name = "EXPRESSION_VALUE_TIMESTAMP")
public class ExprValueTimestamp extends ExprValue {

	private Timestamp value;

	protected ExprValueTimestamp() {
		super();
	}

	public ExprValueTimestamp(Timestamp value) {
		super();
		this.setValue(value);
	}

	@Override
	public String getExpressionTableString() {
		return DateManager.convertDateToString(value);
	}

	public Timestamp getValue() {
		return value;
	}

	public void setValue(Timestamp value) {
		this.value = value;
	}

}
