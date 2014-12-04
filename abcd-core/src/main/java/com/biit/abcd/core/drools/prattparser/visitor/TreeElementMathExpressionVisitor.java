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
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.form.TreeObject;

public class TreeElementMathExpressionVisitor implements ITreeElementVisitor {

	private StringBuilder builder;

	public TreeElementMathExpressionVisitor() {
		this.builder = new StringBuilder();
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		// this.builder.append("(").append(assign.getName()).append(" = ");
		assign.getRightElement().accept(this);
		// this.builder.append(")");
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
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
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		this.builder.append("(");
		condition.getCondition().accept(this);
		this.builder.append(" ? ");
		condition.getThenArm().accept(this);
		this.builder.append(" : ");
		condition.getElseArm().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(NameExpression name) throws NotCompatibleTypeException {
		if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {

			ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) name.getExpressionChain()
					.getExpressions().get(0);
			TreeObject treeObject = expVal.getReference();
			String id = treeObject.getUniqueNameReadable();
			// If it is a question of input type
			if ((treeObject instanceof Question) && ((Question) treeObject).getAnswerType().equals(AnswerType.INPUT)) {
				switch (((Question) treeObject).getAnswerFormat()) {
				case NUMBER:
					this.builder.append("(Double)$" + id + ".getAnswer('" + AnswerFormat.NUMBER.toString() + "')");
					break;
				case DATE:
					if (expVal.getUnit() != null) {
						switch (expVal.getUnit()) {
						case YEARS:
							this.builder.append("DateUtils.returnYearsDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))");
							break;
						case MONTHS:
							this.builder.append("DateUtils.returnMonthsDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))");
							break;
						case DAYS:
							this.builder.append("DateUtils.returnDaysDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))");
							break;
						case DATE:
							this.builder.append("$" + id + ".getAnswer('" + AnswerFormat.DATE.toString() + "')");
							break;
						}
					}
					break;
				case TEXT:
				case POSTAL_CODE:
					throw new NotCompatibleTypeException("Using a text variable inside a mathematical operation",
							expVal);
					// case TEXT:
					// this.builder.append("$" + id + ".getAnswer('" +
					// AnswerFormat.TEXT.toString() + "')");
					// break;
					// case POSTAL_CODE:
					// this.builder.append("$" + id + ".getAnswer('" +
					// AnswerFormat.POSTAL_CODE.toString() + "')");
					// break;
				}
			}
			// If it is not a question input type
			else {
				// If it is a custom variable
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
						throw new NotCompatibleTypeException("Using the text variable: " + variable.getName()
								+ " inside a mathematical operation", expVal);
						// this.builder.append("(String)$" + id +
						// ".getVariableValue('" + variable.getName() + "')");
						// break;
					}
				}
				// For every other possible value, we assume text value
				else {
					throw new NotCompatibleTypeException("Using the text variable: " + treeObject.getName()
							+ " inside a mathematical operation", expVal);
					// this.builder.append("$" + id + ".getAnswer('" +
					// AnswerFormat.TEXT.toString() + "')");
				}
			}
		}
		// In case is a ExpressionValueNumber
		else if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueNumber) {
			this.builder.append(Double.parseDouble(name.getName()));
		}// In case is a global constant
		else if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueGlobalConstant) {
			GlobalVariable globalExpression = ((ExpressionValueGlobalConstant) name.getExpressionChain()
					.getExpressions().get(0)).getValue();

			switch (globalExpression.getFormat()) {
			case NUMBER:
				this.builder.append("(Double)" + name.getName());
				break;
			case TEXT:
			case POSTAL_CODE:
				throw new NotCompatibleTypeException("Using the text constant: " + name.getName()
						+ " inside a mathematical operation", (ExpressionValueGlobalConstant) name.getExpressionChain()
						.getExpressions().get(0));
				// this.builder.append(name.getName());
				// break;
			case DATE:
				this.builder.append("(Date)" + name.getName());
				break;
			}
		} else {
			this.builder.append("'" + name.getName().replace("\"", "\\\"").replace("\'", "\\\'") + "'");
		}
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		this.builder.append("(");
		operator.getLeftElement().accept(this);
		this.builder.append(" ").append(operator.getOperator().punctuator()).append(" ");
		operator.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		this.builder.append("(");
		postfix.getLeftElement().accept(this);
		this.builder.append(postfix.getOperator().punctuator()).append(")");
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		this.builder.append("(").append(prefix.getOperator().punctuator());
		prefix.getRightElement().accept(this);
		this.builder.append(")");
	}

	public StringBuilder getBuilder() {
		return this.builder;
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		group.getElement().accept(this);
	}
}
