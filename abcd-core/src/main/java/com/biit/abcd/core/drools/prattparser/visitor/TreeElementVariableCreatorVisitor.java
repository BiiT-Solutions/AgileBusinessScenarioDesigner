package com.biit.abcd.core.drools.prattparser.visitor;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.rules.DroolsVariable;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

public class TreeElementVariableCreatorVisitor implements ITreeElementVisitor {

	private List<DroolsVariable> variables;

	public TreeElementVariableCreatorVisitor() {
		variables = new ArrayList<DroolsVariable>();
	}

	@Override
	public void visit(AssignExpression assign) {
	}

	@Override
	public void visit(CallExpression call) {
	}

	@Override
	public void visit(ConditionalExpression condition) {
	}

	@Override
	public void visit(NameExpression name) {
		// Get the element inside the expressionChain
		Expression expression = name.getExpressionChain().getExpressions().get(0);
		if (expression instanceof ExpressionValueTreeObjectReference) {
			variables.add(new DroolsVariable((ExpressionValueTreeObjectReference)expression));
		}
	}

	@Override
	public void visit(OperatorExpression operator) {
	}

	@Override
	public void visit(PostfixExpression postfix) {
	}

	@Override
	public void visit(PrefixExpression prefix) {
	}
	
	public List<DroolsVariable> getVariables(){
		return variables;
	} 
}
