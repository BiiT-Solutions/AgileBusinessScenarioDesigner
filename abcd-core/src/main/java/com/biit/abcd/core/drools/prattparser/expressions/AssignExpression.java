package com.biit.abcd.core.drools.prattparser.expressions;

import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;

/**
 * An assignment expression like "a = b".
 */
public class AssignExpression implements ITreeElement {
	private final String name;
	private final ITreeElement rightElement;
	private final Expression expression;
	private final String treeElementId;

	public AssignExpression(NameExpression expression, ITreeElement right) {
		this.name = expression.getName();
		this.expression = expression.getExpressionChain();
		this.rightElement = right;
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}

	public String getName() {
		return this.name;
	}

	public ITreeElement getRightElement() {
		return this.rightElement;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.expression);
		expChain.addExpressions(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expChain.addExpressions(this.rightElement.getExpressionChain());
		expChain.setName(this.treeElementId);
		return expChain;
	}
}
