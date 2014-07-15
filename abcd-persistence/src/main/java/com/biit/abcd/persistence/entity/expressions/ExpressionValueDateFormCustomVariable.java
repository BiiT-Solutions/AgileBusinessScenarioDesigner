package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.TreeObject;

/**
 * Date expressions must define the unit to be applied in functions. I.e. Between(0Y, 18Y)
 */
@Entity
@Table(name = "EXPRESSION_DATE_VALUE_FORM_CUSTOM_VARIABLE")
public class ExpressionValueDateFormCustomVariable extends ExpressionValueFormCustomVariable {

	@Enumerated(EnumType.STRING)
	private DateUnit unit;

	public ExpressionValueDateFormCustomVariable() {
		super();
	}

	public ExpressionValueDateFormCustomVariable(TreeObject question, CustomVariable variable) {
		super(question, variable);
	}

	public synchronized DateUnit getUnit() {
		return unit;
	}

	public synchronized void setUnit(DateUnit unit) {
		this.unit = unit;
	}

	@Override
	public String getRepresentation() {
		if (unit != null) {
			return super.getRepresentation() + " (" + unit.getAbbreviature() + ")";
		}
		return super.getRepresentation();
	}

	@Override
	protected String getExpression() {
		// Ignore units for evaluation.
		return super.getRepresentation();
	}
	
	@Override
	public Expression generateCopy() {
		ExpressionValueDateFormCustomVariable copy = new ExpressionValueDateFormCustomVariable();
		copy.setQuestion(getQuestion());
		copy.setVariable(getVariable());
		copy.unit = unit;
		return copy;
	}

}