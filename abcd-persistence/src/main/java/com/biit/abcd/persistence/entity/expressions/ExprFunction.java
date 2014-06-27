package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_FUNCTION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ExprFunction extends ExprBasic {

	@Enumerated(EnumType.STRING)
	private AvailableFunctions value;

	@Override
	public String getExpressionTableString() {
		return value.getValue();
	}

	@Override
	public String getExpression() {
		return value.getValue();
	}

	public AvailableFunctions getValue() {
		return value;
	}

	public void setValue(AvailableFunctions function) {
		this.value = function;
	}

}
