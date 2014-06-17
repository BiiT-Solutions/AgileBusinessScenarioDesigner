package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.sql.Timestamp;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.utils.DateManager;

public class ExprValue extends ExprBasic {

	private Answer answer;
	private String valueString;
	private Double valueDouble;
	private Timestamp valueDate;

	public ExprValue(Answer answer) {
		this.answer = answer;
	}

	public ExprValue(String value) {
		if (value.equals("")) {
			valueString = null;
		}
		valueString = value;
	}

	public ExprValue(Double value) {
		valueDouble = value;
	}

	public ExprValue(Timestamp date) {
		valueDate = date;
	}

	@Override
	public String getExpressionTableString() {
		if (answer != null) {
			return answer.getName();
		}
		if (valueString != null) {
			return valueString;
		}
		if (valueDouble != null) {
			return DateManager.convertDateToString(valueDate);
		}
		return generateNullLabelCaption("Expr-value");
	}

}
