package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * User for defining functions as MAX, MIN, AVERAGE, ABS, ...
 */
@Entity
@Table(name = "EXPRESSION_FUNCTION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ExpressionFunction extends Expression {

	@Enumerated(EnumType.STRING)
	private AvailableFunction value;

	public ExpressionFunction(){}

	public ExpressionFunction(AvailableFunction function){
		this.value = function;
	}

	@Override
	public String getRepresentation() {
		return this.value.getValue();
	}

	public AvailableFunction getValue() {
		return this.value;
	}

	public void setValue(AvailableFunction function) {
		this.value = function;
	}

	@Override
	public String getExpression() {
		return this.value.getValue();
	}

	@Override
	public Expression generateCopy() {
		ExpressionFunction copy = new ExpressionFunction();
		copy.value = this.value;
		return copy;
	}
}
