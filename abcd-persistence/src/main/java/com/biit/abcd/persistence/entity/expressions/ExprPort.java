package com.biit.abcd.persistence.entity.expressions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_PORT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExprPort extends ExprGroup {

	private String portName;

	protected ExprPort() {
		super();
		addChildExpression(getDefaultExpression());
	}

	public ExprPort(String portName) {
		super();
		this.portName = portName;
		addChildExpression(getDefaultExpression());
	}

	public String getName() {
		return portName;
	}

	@Override
	public void addChildExpression() {
		addChildExpression(getDefaultJoiner(), getDefaultExpression());
	}

	@Override
	public String getExpressionTableString() {
		return portName;
	}

	public abstract ExprOp getDefaultJoiner();

	public abstract ExprBasic getDefaultExpression();

	@Override
	public void removeParenthesis() {
		// This operation won't be performed in the port
	}

	@Override
	protected List<ExprBasic> removeChildExpression(ExprBasic expression) {
		if (childs.size() == 1) {
			List<ExprBasic> elementsRemoved = new ArrayList<>();
			elementsRemoved.add(childs.remove(0));
			addChildExpression(getDefaultExpression());
			return elementsRemoved;
		} else {
			return super.removeChildExpression(expression);
		}
	}
}
