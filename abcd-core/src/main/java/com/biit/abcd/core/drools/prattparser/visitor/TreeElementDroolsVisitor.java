package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;


public class TreeElementDroolsVisitor implements ITreeElementVisitor {

	@Override
	public void visit(AssignExpression assign) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(CallExpression call) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ConditionalExpression condition) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(NameExpression name) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(OperatorExpression operator) {
//		if ((operator.getExpression() instanceof ExpressionOperatorLogic)
//				&& ((ExpressionOperatorLogic) operator.getExpression()).getValue().equals(AvailableOperator.EQUALS)) {
//		}
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(PostfixExpression postfix) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(PrefixExpression prefix) {
		// TODO Auto-generated method stub
	}
}
