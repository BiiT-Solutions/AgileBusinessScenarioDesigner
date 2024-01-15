package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.serialization.expressions.ExpressionValueNumberDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueNumberSerializer;
import com.biit.abcd.serialization.expressions.ExpressionValuePostalCodeDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValuePostalCodeSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Defines a value as postal code.
 * 
 */
@Entity
@JsonDeserialize(using = ExpressionValuePostalCodeDeserializer.class)
@JsonSerialize(using = ExpressionValuePostalCodeSerializer.class)
@Table(name = "expression_value_postal_code")
public class ExpressionValuePostalCode extends ExpressionValueString {
	private static final long serialVersionUID = -4770567829915607298L;

	public ExpressionValuePostalCode() {
		super();
	}

	public ExpressionValuePostalCode(String value) throws NotValidExpressionValue {
		if (!value.matches(AbcdConfigurationReader.getInstance().getPostalCodeMask())) {
			throw new NotValidExpressionValue("Value '" + value + "' is not a valid postal code.");
		}
		setValue(value);
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValuePostalCode) {
			super.copyData(object);
			ExpressionValuePostalCode expressionValuePostalCode = (ExpressionValuePostalCode) object;
			this.setValue(expressionValuePostalCode.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueString.");
		}
	}
}
