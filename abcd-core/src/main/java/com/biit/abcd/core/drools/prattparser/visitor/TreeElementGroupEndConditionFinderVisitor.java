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
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;

/**
 * This visitor is in charge of adding the special operators that compose a complex rule(i.e. OR, AND and NOT).<br>
 * Is used in combination with the visitors
 * 'TreeElementGroupConditionFinderVisitor' and
 * 'TreeElementGroupEndRuleConditionCreatorVisitor'
 */
public class TreeElementGroupEndConditionFinderVisitor implements ITreeElementVisitor {

	private ExpressionChain completeExpression;

	public TreeElementGroupEndConditionFinderVisitor() {
		setCompleteExpression(new ExpressionChain());
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		assign.getRightElement().accept(this);
		getCompleteExpression().addExpression(
				new ExpressionValueString(assign.getExpressionChain().getName().toString()));
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getLeftElement().accept(this);
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
		}
		getCompleteExpression()
				.addExpression(new ExpressionValueString(call.getExpressionChain().getName().toString()));
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		condition.getCondition().accept(this);
		condition.getThenArm().accept(this);
		condition.getElseArm().accept(this);
	}

	@Override
	public void visit(NameExpression name) {
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		operator.getLeftElement().accept(this);
		if (operator.getOperator().equals(ExpressionTokenType.OR)) {
			getCompleteExpression().addExpression(new ExpressionOperatorLogic(AvailableOperator.OR));
		} else if (operator.getOperator().equals(ExpressionTokenType.AND)) {
			getCompleteExpression().addExpression(new ExpressionOperatorLogic(AvailableOperator.AND));
		} else {
			getCompleteExpression().addExpression(
					new ExpressionValueString(operator.getExpressionChain().getName().toString()));
		}
		operator.getRightElement().accept(this);
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		postfix.getLeftElement().accept(this);
		getCompleteExpression().addExpression(
				new ExpressionValueString(postfix.getExpressionChain().getName().toString()));
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		if (prefix.getOperator().equals(ExpressionTokenType.NOT)) {
			getCompleteExpression().addExpression(new ExpressionFunction(AvailableFunction.NOT));
		}
		prefix.getRightElement().accept(this);
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		completeExpression.addExpression(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		group.getElement().accept(this);
		completeExpression.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
	}

	public ExpressionChain getCompleteExpression() {
		return completeExpression;
	}

	public void setCompleteExpression(ExpressionChain completeExpression) {
		this.completeExpression = completeExpression;
	}
}
