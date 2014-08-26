package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;


public class TreeElementPrintVisitor implements ITreeElementVisitor{

	private StringBuilder builder;

	public TreeElementPrintVisitor() {
		this.builder = new StringBuilder();
	}

	@Override
	public void visit(AssignExpression assign) {
		this.builder.append("(").append(assign.getName()).append(" = ");
		assign.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(CallExpression call) {
		call.getFunction().accept(this);
		this.builder.append("(");
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
			if (i < (call.getArgs().size() - 1)) {
				this.builder.append(", ");
			}
		}
		this.builder.append(")");
	}

	@Override
	public void visit(ConditionalExpression condition) {
		this.builder.append("(");
		condition.getCondition().accept(this);
		this.builder.append(" ? ");
		condition.getThenArm().accept(this);
		this.builder.append(" : ");
		condition.getElseArm().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(NameExpression name) {
		this.builder.append(name.getName());
	}

	@Override
	public void visit(OperatorExpression operator) {
		this.builder.append("(");
		operator.getLeftElement().accept(this);
		this.builder.append(" ").append(operator.getOperator().punctuator()).append(" ");
		operator.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(PostfixExpression postfix) {
		this.builder.append("(");
		postfix.getLeftElement().accept(this);
		this.builder.append(postfix.getOperator().punctuator()).append(")");
	}

	@Override
	public void visit(PrefixExpression prefix) {
		this.builder.append("(").append(prefix.getOperator().punctuator());
		prefix.getRightElement().accept(this);
		this.builder.append(")");
	}

	public StringBuilder getBuilder() {
		return this.builder;
	}
}
