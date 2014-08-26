package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;


public interface ITreeElementVisitor {

	// Defines the different tree nodes that can be visited
	void visit(AssignExpression assign);
	void visit(CallExpression call);
	void visit(ConditionalExpression condition);
	void visit(NameExpression name);
	void visit(OperatorExpression operator);
	void visit(PostfixExpression postfix);
	void visit(PrefixExpression prefix);
}
