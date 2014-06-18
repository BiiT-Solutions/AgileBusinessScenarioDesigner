package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_PORT_LOGIC")
public class ExprPortLogic extends ExprPort {

	public ExprPortLogic(String portName) {
		super(portName);
	}

	@Override
	public ExprBasic getDefaultExpression() {
		return new ExprAtomicLogic();
	}

	@Override
	public ExprOp getDefaultJoiner() {
		return new ExprOpLogic();
	}

}
