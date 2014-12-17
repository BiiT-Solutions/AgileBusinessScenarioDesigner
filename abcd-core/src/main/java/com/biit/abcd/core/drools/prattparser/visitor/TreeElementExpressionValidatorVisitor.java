package com.biit.abcd.core.drools.prattparser.visitor;

import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.GroupExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.validators.ExpressionValidator;
import com.biit.abcd.core.drools.rules.validators.ValueType;

public class TreeElementExpressionValidatorVisitor implements ITreeElementVisitor {

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		assign.getRightElement().accept(this);
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getFunction().accept(this);
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
		}
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		condition.getCondition().accept(this);
		condition.getThenArm().accept(this);
		condition.getElseArm().accept(this);
	}

	@Override
	public void visit(NameExpression name) throws NotCompatibleTypeException {
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		operator.getLeftElement().accept(this);
		operator.getRightElement().accept(this);
		if (!operator.getOperator().equals(ExpressionTokenType.OR)
				&& !operator.getOperator().equals(ExpressionTokenType.AND)) {

			if ((operator.getLeftElement() instanceof NameExpression)
					&& (operator.getRightElement() instanceof NameExpression)) {
				ValueType leftType = ExpressionValidator.getValueInsideExpressionChain(((NameExpression) operator
						.getLeftElement()).getExpressionChain());
				ValueType rightType = ExpressionValidator.getValueInsideExpressionChain(((NameExpression) operator
						.getRightElement()).getExpressionChain());
				if (leftType == rightType) {
					if (operator.getOperator().equals(ExpressionTokenType.PLUS)
							|| operator.getOperator().equals(ExpressionTokenType.MINUS)
							|| operator.getOperator().equals(ExpressionTokenType.MULTIPLICATION)
							|| operator.getOperator().equals(ExpressionTokenType.DIVISION)) {
						// Cannot apply the operators '+', '-', '*','/' to
						// anything but numbers
						if (!leftType.equals(ValueType.NUMBER)) {
							throw new NotCompatibleTypeException("Operator types not compatible", null);
						}
					}
				} else {
					throw new NotCompatibleTypeException("Operator types not compatible", null);
				}
			}
		}
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		postfix.getLeftElement().accept(this);
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		prefix.getRightElement().accept(this);
	}

	@Override
	public void visit(GroupExpression groupExpression) throws NotCompatibleTypeException {
		groupExpression.getElement().accept(this);
	}

}
