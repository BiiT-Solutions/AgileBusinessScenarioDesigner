package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines values as a double.
 * 
 */
@Entity
@Table(name = "expression_value_number")
public class ExpressionValueNumber extends ExpressionValue<Double> {
	private static final long serialVersionUID = -8379130602709661373L;
	private Double value;

	protected ExpressionValueNumber() {
		super();
		value = 0d;
	}

	public ExpressionValueNumber(Double value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		return getValueWithoutTrailingZeroes();
	}

	@Override
	public Double getValue() {
		return new Double(value);
	}

	@Override
	public String getExpression() {
		return getValueWithoutTrailingZeroes();
	}

	public String getValueWithoutTrailingZeroes() {
		Double convertedValue = new Double(value);
		return convertedValue.toString().indexOf(".") < 0 ? convertedValue.toString() : convertedValue.toString()
				.replaceAll("0*$", "").replaceAll("\\.$", "");
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueNumber) {
			super.copyData(object);
			ExpressionValueNumber expressionValueNumber = (ExpressionValueNumber) object;
			this.setValue(expressionValueNumber.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueNumber.");
		}
	}

}
