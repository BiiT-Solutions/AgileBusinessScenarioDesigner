package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.util.HashSet;
import java.util.Set;

public abstract class ExprOp extends ExprBasic{

	//Do not transient in DB
	private Set<ExprOpValue> acceptedValues;
	private ExprOpValue currentValue;
	
	//Transient the value of the current exprOpValue
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
		if(value==null){
			return " "+getValueNullCaption()+" ";
		}else{
			return " "+caption+" ";
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
