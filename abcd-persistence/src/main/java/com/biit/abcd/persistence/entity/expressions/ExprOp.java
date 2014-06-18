package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "EXPRESSION_OPERATION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExprOp extends ExprBasic {

	@Transient
	private Set<ExprOpValue> acceptedValues;
	@Transient
	private ExprOpValue currentValue;

	private String value;
	private String caption;

	public ExprOp() {
		super();
		acceptedValues = new HashSet<>();
		value = null;
	}

	public abstract String getValueNullCaption();

	@Override
	public String getExpressionTableString() {
		if (value == null) {
			return " " + getValueNullCaption() + " ";
		} else {
			return " " + caption + " ";
		}
	}

	public Set<ExprOpValue> getAcceptedValues() {
		return acceptedValues;
	}

	public ExprOpValue getValue() {
		return currentValue;
	}

	public void setValue(ExprOpValue exprOpvalue) {
		currentValue = exprOpvalue;
		this.value = currentValue.getValue();
		this.caption = currentValue.getCaption();
	}

}
