package com.biit.abcd.webpages.elements.expressiontree.expression;

public class ExprWoChildLogic extends ExprWoChild {

	private ExprBasic leftOperand;
	private ExprWoChildLogicType type;
	private ExprBasic rightOperand;

	public enum ExprWoChildLogicType {
		ALWAYS, EQ, NE, LT, GT, LE, GE, IN, BETWEEN
	};

	@Override
	public String getExpressionTableString() {
		if (type == null) {
			return generateNullLabelCaption("expr-logic");
		} else {
			switch (type) {
			case ALWAYS:
				return "ALWAYS";
			case EQ:
				return "==";
			case NE:
				return "!=";
			case LT:
				return "<";
			case GT:
				return ">";
			case LE:
				return "<=";
			case GE:
				return ">=";
			case IN:
				return "IN";
			case BETWEEN:
				return "BETWEEN";
			}
		}
		return "unknown error";
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
}
