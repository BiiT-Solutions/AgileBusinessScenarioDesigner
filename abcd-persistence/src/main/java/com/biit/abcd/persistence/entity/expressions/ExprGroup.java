package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_GROUP")
public class ExprGroup extends ExprWithChilds {

	public ExprGroup() {
		super();
	}

	protected void addChildExpression(ExprBasic expression) {
		expression.parent = this;
		childs.add(expression);
	}

	public void addChildExpression(ExprOp exprJoint, ExprBasic exprBasic) {
		// TODO check?
		addChildExpression(exprJoint);
		addChildExpression(exprBasic);
	}

	protected List<ExprBasic> removeChildExpression(ExprBasic expression){
		List<ExprBasic> elementsRemoved = new ArrayList<>();
		
		if (childs.size() == 1) {
			//If this is a group, and there is only one element, we remove the parenthesis and do that parent makes all the cleaning 
			elementsRemoved.add(this);
			removeParenthesis();
			elementsRemoved.addAll(((ExprGroup)parent).removeChildExpression(expression));
			return elementsRemoved;
		}
		
		int indexOfExpression = childs.indexOf(expression);
		elementsRemoved.add(childs.remove(indexOfExpression));
		if(indexOfExpression==0){
			//First element
			if(!childs.isEmpty()){
				//If not empty delete the join now on position 0
				elementsRemoved.add(childs.remove(0));				
			}
		}else{
			//Remove previous join expression.
			elementsRemoved.add(childs.remove(indexOfExpression-1));
		}
		return elementsRemoved;
	}

	@Override
	public String getExpressionTableString() {
		return "( " + getChildExpressionTableString() + " )";
	}
	
	public String getChildExpressionTableString(){
		String expressionTableString = new String();
		for (ExprBasic expression : getChilds()) {
			expressionTableString += expression.getExpressionTableString();
		}
		return expressionTableString;
	}

	@Override
	public void removeParenthesis() {
		ExprGroup parentGroup = (ExprGroup) parent;
		int indexOfThisElement = parentGroup.childs.indexOf(this);
		for (ExprBasic child : getChilds()) {
			child.parent = parent;
			parentGroup.childs.add(indexOfThisElement, child);
		}
		parentGroup.childs.remove(this);
	}

	public void addChildExpression() {
		ExprPort portParent = (ExprPort) getParentOfClass(ExprPort.class);
		if (portParent != null) {
			addChildExpression(portParent.getDefaultJoiner(), portParent.getDefaultExpression());
		}
	}
}
