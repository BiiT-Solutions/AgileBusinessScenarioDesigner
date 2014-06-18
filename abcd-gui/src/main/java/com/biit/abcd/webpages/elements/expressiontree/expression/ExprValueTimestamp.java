package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.sql.Timestamp;

import com.biit.abcd.utils.DateManager;

public class ExprValueTimestamp extends ExprValue{

	private Timestamp value;
	
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
