package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_ATOMIC_LOGIC")
public class ExprAtomicLogic extends ExprAtomic {

	@ManyToOne(fetch = FetchType.EAGER)
	private ExprValueFormReference leftOperand;
	@Enumerated(EnumType.STRING)
	private ExprAtomicChildLogicType type;
	@ManyToOne(fetch = FetchType.EAGER)
	private ExprValues rightOperand;

	public enum ExprAtomicChildLogicType {
		ALWAYS, ANY, EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, LESS_EQUALS, GREATER_EQUALS, IN, BETWEEN
	};

	public ExprAtomicLogic() {
		super();
	}

	@Override
	public String getExpressionTableString() {
		if (type == null) {
			return generateNullLabelCaption("expr-logic");
		} else {

			String expression = new String();
			if (leftOperand != null) {
				expression += leftOperand.getExpressionTableString() + " ";
			} else {
				expression += generateNullLabelCaption("Left-Operand");
			}

			switch (type) {
			case ALWAYS:
				expression += "ALWAYS";
				break;
			case ANY:
				expression += "ANY";
				break;
			case EQUALS:
				expression += "==";
				break;
			case NOT_EQUALS:
				expression += "!=";
				break;
			case LESS_THAN:
				expression += "<";
				break;
			case GREATER_THAN:
				expression += ">";
				break;
			case LESS_EQUALS:
				expression += "<=";
				break;
			case GREATER_EQUALS:
				expression += ">=";
				break;
			case IN:
				expression += "IN";
				break;
			case BETWEEN:
				expression += "BETWEEN";
				break;
			}

			if (rightOperand != null) {
				expression += " " + rightOperand.getExpressionTableString();
			}

			return expression;
		}
	}

	public void setAlways() {
		type = ExprAtomicChildLogicType.ALWAYS;
	}

	public void setEq() {
		type = ExprAtomicChildLogicType.EQUALS;
	}

	public void setNe() {
		type = ExprAtomicChildLogicType.NOT_EQUALS;
	}

	public void setLt() {
		type = ExprAtomicChildLogicType.LESS_THAN;
	}

	public void setGt() {
		type = ExprAtomicChildLogicType.GREATER_THAN;
	}

	public void setLe() {
		type = ExprAtomicChildLogicType.LESS_EQUALS;
	}

	public void setGe() {
		type = ExprAtomicChildLogicType.GREATER_EQUALS;
	}

	public void setIn() {
		type = ExprAtomicChildLogicType.IN;
	}

	public void setBetween() {
		type = ExprAtomicChildLogicType.BETWEEN;
	}

	public ExprAtomicChildLogicType getType() {
		return type;
	}

	public ExprValueFormReference getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(ExprValueFormReference leftOperand) {
		this.leftOperand = leftOperand;
	}

	public ExprValues getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(ExprValues values) {
		this.rightOperand = values;
	}

	public void clean() {
		leftOperand = null;
		type = null;
		rightOperand = null;
	}
}
