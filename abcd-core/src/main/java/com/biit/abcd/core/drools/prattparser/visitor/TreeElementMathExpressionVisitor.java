package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class TreeElementMathExpressionVisitor implements ITreeElementVisitor {

	private StringBuilder builder;

	public TreeElementMathExpressionVisitor() {
		this.builder = new StringBuilder();
	}

	@Override
	public void visit(AssignExpression assign) {
		// this.builder.append("(").append(assign.getName()).append(" = ");
		assign.getRightElement().accept(this);
		// this.builder.append(")");
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
		if ((name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueTreeObjectReference)
				|| (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueCustomVariable)) {
			TreeObject treeObject = ((ExpressionValueTreeObjectReference) name.getExpressionChain().getExpressions()
					.get(0)).getReference();

			String id = treeObject.getComparationIdNoDash();
			if ((treeObject instanceof Question) && ((Question) treeObject).getAnswerType().equals(AnswerType.INPUT)) {
				switch (((Question) treeObject).getAnswerFormat()) {
				case NUMBER:
					this.builder.append("(Double)$" + id + ".getAnswer()");
					break;
				default:
					this.builder.append("$" + id + ".getAnswer()");
					break;
				}
			} else {
				if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
					CustomVariable variable = ((ExpressionValueCustomVariable) name.getExpressionChain()
							.getExpressions().get(0)).getVariable();
					switch (variable.getType()) {
					case NUMBER:
						this.builder.append("(Double)$" + id + ".getVariableValue('" + variable.getName() + "')");
						break;
					case DATE:
						this.builder.append("(Date)$" + id + ".getVariableValue('" + variable.getName() + "')");
						break;
					case STRING:
						this.builder.append("(String)$" + id + ".getVariableValue('" + variable.getName() + "')");
						break;
					}
				} else {
					this.builder.append("$" + id + ".getAnswer()");
				}
			}
		} else {
			this.builder.append(name.getName());
		}
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
