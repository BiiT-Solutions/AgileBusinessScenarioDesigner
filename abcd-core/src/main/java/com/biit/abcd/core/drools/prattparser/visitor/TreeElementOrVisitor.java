//package com.biit.abcd.core.drools.prattparser.visitor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.biit.abcd.core.drools.prattparser.ExpressionTokenType;
//import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.GroupExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
//import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
//import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
//import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
//import com.biit.abcd.persistence.entity.expressions.Expression;
//import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
//import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
//
//public class TreeElementOrVisitor implements ITreeElementVisitor {
//
//	private List<ExpressionChain> conditions;
//
//	public TreeElementOrVisitor() {
//		setConditions(new ArrayList<ExpressionChain>());
//	}
//
//	@Override
//	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
//		assign.getRightElement().accept(this);
//	}
//
//	@Override
//	public void visit(CallExpression call) throws NotCompatibleTypeException {
//		call.getFunction().accept(this);
//		for (int i = 0; i < call.getArgs().size(); i++) {
//			call.getArgs().get(i).accept(this);
//		}
//	}
//
//	@Override
//	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
//		condition.getCondition().accept(this);
//		condition.getThenArm().accept(this);
//		condition.getElseArm().accept(this);
//	}
//
//	@Override
//	public void visit(NameExpression name) {
//	}
//
//	@Override
//	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
//		operator.getLeftElement().accept(this);
//		if (operator.getOperator().equals(ExpressionTokenType.OR)) {
//			if (!hasOrOperator(operator.getLeftElement().getExpressionChain())) {
//				getConditions().add(operator.getLeftElement().getExpressionChain());
//			}
//			if (!hasOrOperator(operator.getRightElement().getExpressionChain())) {
//				getConditions().add(operator.getRightElement().getExpressionChain());
//			}
//		}
//		operator.getRightElement().accept(this);
//	}
//
//	private boolean hasOrOperator(ExpressionChain expressionChain) {
//		for (Expression expression : expressionChain.getExpressions()) {
//			if (expression instanceof ExpressionOperatorLogic) {
//				if (((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
//		postfix.getLeftElement().accept(this);
//	}
//
//	@Override
//	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
//		prefix.getRightElement().accept(this);
//	}
//	
//	@Override
//	public void visit(GroupExpression group) throws NotCompatibleTypeException {
//		group.getElement().accept(this);
//	}
//
//	public void setConditions(List<ExpressionChain> conditions) {
//		this.conditions = conditions;
//	}
//
//	public List<ExpressionChain> getConditions() {
//		return this.conditions;
//	}
//}
