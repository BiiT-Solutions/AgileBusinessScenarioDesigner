package com.biit.abcd.webpages.elements.expressiontree.expression;

import java.util.List;

/**
 * Childless expression. It has the parenthesis method.
 * 
 */
public abstract class ExprWoChild extends ExprBasic{

	public void addParenthesis(){
		//Create a new parenthesis
		ExprWChild parenthesis = new ExprWChild();
		//Insert in the same position as this element in parent and update parent reference.
		parenthesis.parent = parent;
		int indexOfThisElement = ((ExprWChild)parent).childs.indexOf(this);
		((ExprWChild)parent).childs.set(indexOfThisElement, parenthesis);
		parenthesis.addChildExpression(this);
	}
	
	public List<ExprBasic> delete(){
		ExprWChild parentGroup = (ExprWChild) parent;
		return parentGroup.removeChildExpression(this);
	}
}
