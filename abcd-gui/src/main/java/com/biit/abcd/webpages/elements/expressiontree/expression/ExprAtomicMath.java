package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprAtomicMath extends ExprAtomic {
	
	private ExprValue value;

	@Override
	public String getExpressionTableString() {
		if(value == null){
			return generateNullLabelCaption("expr-op");
		}else{
			return value.getExpressionTableString(); 
		}
	}

}
