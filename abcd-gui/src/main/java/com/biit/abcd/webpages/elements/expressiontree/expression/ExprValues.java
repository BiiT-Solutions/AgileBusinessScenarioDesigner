package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.util.ArrayList;
import java.util.List;

public class ExprValues extends ExprBasic {
	
	private List<ExprValue> values;
	
	public ExprValues(){
		values = new ArrayList<ExprValue>();
	}

	@Override
	public String getExpressionTableString() {
		if(values.isEmpty()){
			return "[ ]";
		}
		String expression = new String();
		if(values.size()==1){
			expression+=values.get(0).getExpressionTableString();
		}else{
			for(ExprValue value: values){
				expression+=value.getExpressionTableString()+",";
			}
			expression = expression.substring(0, expression.length()-2);
		}
		return expression;
	}

	public void addValue(ExprValue value) {
		values.add(value);
	}

}
