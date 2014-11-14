package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.GroupExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;


public interface ITreeElementVisitor {

	// Defines the different tree nodes that can be visited
	void visit(AssignExpression assign) throws NotCompatibleTypeException;
	void visit(CallExpression call) throws NotCompatibleTypeException;
	void visit(ConditionalExpression condition) throws NotCompatibleTypeException;
	void visit(NameExpression name) throws NotCompatibleTypeException;
	void visit(OperatorExpression operator) throws NotCompatibleTypeException;
	void visit(PostfixExpression postfix) throws NotCompatibleTypeException;
	void visit(PrefixExpression prefix) throws NotCompatibleTypeException;
	void visit(GroupExpression groupExpression) throws NotCompatibleTypeException;
}
