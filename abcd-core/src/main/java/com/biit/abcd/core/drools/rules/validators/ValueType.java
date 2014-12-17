package com.biit.abcd.core.drools.rules.validators;


/**
 *	Enum that combines the answer type and answer format
 *	(Used in the expression validator to check value types) 
 *
 */
public enum ValueType {

	RADIO,

	MULTI_CHECKBOX,

	TEXT,

	NUMBER,

	DATE,

	POSTAL_CODE;
}
