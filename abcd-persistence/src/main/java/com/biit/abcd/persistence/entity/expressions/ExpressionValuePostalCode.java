package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines a value as postal code.
 * 
 */
@Entity
@Table(name = "expression_value_postal_code")
public class ExpressionValuePostalCode extends ExpressionValueString {

	public ExpressionValuePostalCode() {
		super();
	}

	public ExpressionValuePostalCode(String value) throws NotValidExpressionValue {
		if (!value.matches(AbcdConfigurationReader.getInstance().getPostalCodeMask())) {
			throw new NotValidExpressionValue("Value '" + value + "' is not a valid postal code.");
		}
		setValue(value);
	}
}
