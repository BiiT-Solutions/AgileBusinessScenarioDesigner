package com.biit.abcd.persistence.entity.expressions;

import java.util.List;

/**
 * Childless expression. It has the parenthesis method.
 * 
 */
public abstract class ExprAtomic extends ExprBasic{
	
	public void addParenthesis(){
		//Create a new parenthesis
		ExprGroup parenthesis = new ExprGroup();
		//Insert in the same position as this element in parent and update parent reference.
		parenthesis.parent = parent;
		int indexOfThisElement = ((ExprGroup)parent).childs.indexOf(this);
		((ExprGroup)parent).childs.set(indexOfThisElement, parenthesis);
		parenthesis.addChildExpression(this);
	}
	
	public List<ExprBasic> delete(){
		ExprGroup parentGroup = (ExprGroup) parent;
		return parentGroup.removeChildExpression(this);
	}

	public boolean isParenthised(){
		if(parent instanceof ExprGroup && !(parent instanceof ExprPort)){
			return true;
		}
		return false;
	}
	
	public void removeParenthesis() {
		if(isParenthised()){
			((ExprGroup)parent).removeParenthesis();
		}
	}

	public void addExpression() {
		if(parent instanceof ExprGroup){
			((ExprGroup)parent).addChildExpression();
		}
	}
}
