package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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

	@Override
	public String getExpressionTableString() {
		if (unit != null) {
			return super.getExpressionTableString() + " (" + unit.getAbbreviature() + ")";
		}
		return super.getExpressionTableString();
	}

}
