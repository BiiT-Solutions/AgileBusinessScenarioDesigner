package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_OPERATION_LOGIC")
public class ExprOpLogic extends ExprOp {

	public ExprOpLogic() {
		super();
		getAcceptedValues().add(new ExprOpValue("AND", "AND"));
		getAcceptedValues().add(new ExprOpValue("OR", "OR"));
	}

	@Override
	public String getValueNullCaption() {
		return generateNullLabelCaption("logic-join");
	}

}
