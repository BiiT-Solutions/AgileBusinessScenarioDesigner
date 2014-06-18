package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EXPRESSION_ATOMIC_MATH")
public class ExprAtomicMath extends ExprAtomic {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private ExprValue value;

	@Override
	public String getExpressionTableString() {
		if(value == null){
			return generateNullLabelCaption("expr-op");
		}else{
			return value.getExpressionTableString(); 
		}
	}

	public ExprValue getValue() {
		return value;
	}

	public void setValue(ExprValue value) {
		this.value = value;
	}

}
