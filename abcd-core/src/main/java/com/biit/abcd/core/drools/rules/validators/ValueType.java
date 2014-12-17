package com.biit.abcd.core.drools.rules.validators;

import java.sql.Date;

/**
 * Enum that combines the answer type and answer format (Used in the expression
 * validator to check value types)
 * 
 */
public enum ValueType {

	RADIO(String.class),

	MULTI_CHECKBOX(String.class),

	TEXT(String.class),

	NUMBER(Double.class),

	DATE(Date.class),

	POSTAL_CODE(String.class);

	// Used in the plug-in call validation
	// The reflected method needs to know the class of the parameter called
	private Class<?> classType;

	private ValueType(Class<?> classType) {
		this.classType = classType;
	}

	public Class<?> getClassType() {
		return classType;
	}
}
