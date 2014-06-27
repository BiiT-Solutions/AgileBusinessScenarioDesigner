package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_ATOMIC_LOGIC")
public class ExprAtomicLogic extends ExprAtomic {

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private ExprValue value;

	public ExprAtomicLogic() {
		super();
	}

	@Override
	public String getExpressionTableString() {
		if (value == null) {
			return generateNullLabelCaption("expr-logic");
		} else {
			if(value instanceof ExprValueBoolean){
				if(((ExprValueBoolean)value).getValue()==true){
					return "ALWAYS";
				}else{
					return "NEVER";
				}
			}else{
				return value.getExpressionTableString();
			}
		}
	}

	public void setValue(ExprValue value) {
		this.value = value;
	}

	public ExprValue getValue() {
		return this.value;
	}

	@Override
	protected String getExpression() {
		return value.getExpression();
	}
}
