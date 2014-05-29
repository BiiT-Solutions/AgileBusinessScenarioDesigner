package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum FormulaElementType {

	BOOLEAN,
	STRING,
	DOUBLE,
	DATE,
	QUESTION,
	ANSWER,
	EXPRESION;
	
	public static List<FormulaElementType> getAnyType(){
		return new ArrayList<FormulaElementType>(Arrays.asList(FormulaElementType.values()));
	}
}
