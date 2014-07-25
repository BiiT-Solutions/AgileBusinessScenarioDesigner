package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.TreeObject;

/**
 * Date expressions must define the unit to be applied in functions. I.e. Between(0Y, 18Y)
 */
@Entity
@Table(name = "EXPRESSION_DATE_VALUE_TREE_OBJECT_REFERENCE")
public class ExpressionValueDateTreeObjectReference extends ExpressionValueTreeObjectReference {

	@Enumerated(EnumType.STRING)
	private DateUnit unit;

	public synchronized DateUnit getUnit() {
		return unit;
	}

	public synchronized void setUnit(DateUnit unit) {
		this.unit = unit;
	}
	
	public ExpressionValueDateTreeObjectReference(){
		super();
	}
	
	public ExpressionValueDateTreeObjectReference(TreeObject reference, DateUnit dateUnit) {
		setReference(reference);
		setUnit(dateUnit);
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
		//Ignore units for evaluation.
		return super.getRepresentation();
	}
	
	@Override
	public Expression generateCopy() {
		ExpressionValueDateTreeObjectReference copy = new ExpressionValueDateTreeObjectReference();
		copy.setReference(getReference());
		copy.unit = unit;
		return copy;
	}

}
