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
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.form.entity.TreeObject;

/**
 * Visitor used to validate and create valid drools mathematical expressions.
 */

public class TreeElementMathExpressionVisitor implements ITreeElementVisitor {

	private StringBuilder builder;

	public TreeElementMathExpressionVisitor() {
		builder = new StringBuilder();
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		assign.getRightElement().accept(this);
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getLeftElement().accept(this);
		builder.append("(");
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
			if (i < (call.getArgs().size() - 1)) {
				builder.append(", ");
			}
		}
		builder.append(")");
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		builder.append("(");
		condition.getCondition().accept(this);
		builder.append(" ? ");
		condition.getThenArm().accept(this);
		builder.append(" : ");
		condition.getElseArm().accept(this);
		builder.append(")");
	}

	@Override
	public void visit(NameExpression name) throws NotCompatibleTypeException {
		if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable expVal = (ExpressionValueCustomVariable) name.getExpressionChain().getExpressions().get(0);
			String customVariableName = expVal.getVariable().getName();
			TreeObject treeObject = expVal.getReference();
			String id = treeObject.getUniqueNameReadable();

			switch (expVal.getVariable().getType()) {
			case NUMBER:
				builder.append("(Double)$" + id + ".getVariableValue('" + customVariableName + "')");
				break;
			case DATE:
				if (expVal.getUnit() != null) {
					builder.append(expVal.getUnit().getDroolsFunction() + "($" + id + ".getVariableValue('" + customVariableName + "'))");
				} else {
					builder.append("$" + id + ".getVariableValue('" + customVariableName + "')");
					break;
				}
				break;
			case STRING:
				builder.append("$" + id + ".getVariableValue('" + customVariableName + "')");
				break;
				//throw new NotCompatibleTypeException("Using a text variable inside a mathematical operation: '" + name.getExpressionChain().toString() + "'.",
				//		expVal);
			}
		} else if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
			ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) name.getExpressionChain().getExpressions().get(0);
			TreeObject treeObject = expVal.getReference();
			String id = treeObject.getUniqueNameReadable();
			// If it is a question of input type
			if ((treeObject instanceof Question) && ((Question) treeObject).getAnswerType().equals(AnswerType.INPUT)) {
				switch (((Question) treeObject).getAnswerFormat()) {
				case NUMBER:
					builder.append("(Double) $" + id + ".getAnswer('" + AnswerFormat.NUMBER + "')");
					break;
				case DATE:
					if (expVal.getUnit() != null) {
						builder.append(expVal.getUnit().getDroolsFunction() + "($" + id + ".getAnswer('" + AnswerFormat.DATE + "'))");
					}
					break;
				case TEXT:
				case MULTI_TEXT:
				case POSTAL_CODE:
					throw new NotCompatibleTypeException("Using a text variable inside a mathematical operation '" + name.getExpressionChain() + "'.", expVal);
				}
			}

		}
		// In case is a ExpressionValueNumber
		else if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueNumber) {
			builder.append(Double.parseDouble(name.getName()));
		}// In case is a global constant
		else if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueGlobalVariable) {
			GlobalVariable globalExpression = ((ExpressionValueGlobalVariable) name.getExpressionChain().getExpressions().get(0)).getValue();

			switch (globalExpression.getFormat()) {
			case NUMBER:
				builder.append("(Double)" + name.getName());
				break;
			case TEXT:
			case MULTI_TEXT:
			case POSTAL_CODE:
				throw new NotCompatibleTypeException("Using the text constant: " + name.getName() + " inside a mathematical operation '"
						+ name.getExpressionChain() + "'", (ExpressionValueGlobalVariable) name.getExpressionChain().getExpressions().get(0));
			case DATE:
				builder.append("(Date)" + name.getName());
				break;
			}
		} else {
			builder.append("'" + name.getName().replace("\"", "\\\"").replace("\'", "\\\'") + "'");
		}
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		builder.append("(");
		operator.getLeftElement().accept(this);
		builder.append(" ").append(operator.getOperator().getPunctuator()).append(" ");
		operator.getRightElement().accept(this);
		builder.append(")");
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		builder.append("(");
		postfix.getLeftElement().accept(this);
		builder.append(postfix.getOperator().getPunctuator()).append(")");
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		builder.append("(").append(prefix.getOperator().getPunctuator());
		prefix.getRightElement().accept(this);
		builder.append(")");
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		group.getElement().accept(this);
	}
}
