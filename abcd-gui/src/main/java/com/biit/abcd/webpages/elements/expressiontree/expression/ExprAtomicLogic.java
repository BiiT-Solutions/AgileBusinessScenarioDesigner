package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprAtomicLogic extends ExprAtomic {

	private ExprValueFormReference leftOperand;
	private ExprWoChildLogicType type;
	private ExprValues rightOperand;

	public enum ExprWoChildLogicType {
		ALWAYS, ANY, EQ, NE, LT, GT, LE, GE, IN, BETWEEN
	};
	
	public ExprAtomicLogic(){
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
			case EQ:
				expression += "==";
				break;
			case NE:
				expression += "!=";
				break;
			case LT:
				expression += "<";
				break;
			case GT:
				expression += ">";
				break;
			case LE:
				expression += "<=";
				break;
			case GE:
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
		type = ExprWoChildLogicType.ALWAYS;
	}

	public void setEq() {
		type = ExprWoChildLogicType.EQ;
	}

	public void setNe() {
		type = ExprWoChildLogicType.NE;
	}

	public void setLt() {
		type = ExprWoChildLogicType.LT;
	}

	public void setGt() {
		type = ExprWoChildLogicType.GT;
	}

	public void setLe() {
		type = ExprWoChildLogicType.LE;
	}

	public void setGe() {
		type = ExprWoChildLogicType.GE;
	}

	public void setIn() {
		type = ExprWoChildLogicType.IN;
	}

	public void setBetween() {
		type = ExprWoChildLogicType.BETWEEN;
	}

	public ExprWoChildLogicType getType() {
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
