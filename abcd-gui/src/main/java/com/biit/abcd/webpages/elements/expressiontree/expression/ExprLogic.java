package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprLogic extends ExprWoChild {
	
	public String value;

	@Override
	public String getExpressionTableString() {
		if(value==null){
			return "expr-logic";
		}else{
			return value;
		}
	}

}
