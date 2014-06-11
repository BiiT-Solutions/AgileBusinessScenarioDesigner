package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Type {

	BOOLEAN,
	STRING,
	DOUBLE,
	TIMESTAMP,
	TREE_OBJECT_REFERENCE,
	EXPRESSION;
	
	public static List<Type> getAnyType(){
		return new ArrayList<Type>(Arrays.asList(Type.values()));
	}
}
