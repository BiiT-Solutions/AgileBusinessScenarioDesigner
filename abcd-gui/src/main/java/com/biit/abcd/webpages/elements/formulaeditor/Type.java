package com.biit.abcd.webpages.elements.formulaeditor;

import java.util.HashSet;
import java.util.Set;

public enum Type {

	VOID,
	
	COMPARISON,
	
	LOGIC,
	
	ASSIGNATION,
	
	CALCULATION,
	
	TREE_OBJECT_REFERENCE,
	
	VARIABLE,
	
	EXPRESSION;	

	public static Set<Type> getAnyType() {
		Set<Type> types = new HashSet<Type>();
		for (Type type : Type.values()) {
			types.add(type);
		}
		return types;
	}
	
	public static Set<Type> getComparisonAndLogic(){
		Set<Type> types = new HashSet<Type>();
		types.add(COMPARISON);
		types.add(LOGIC);
		return types;
	}
	
	public static Set<Type> getVoidAssignationCalculation(){
		Set<Type> types = new HashSet<Type>();
		types.add(VOID);
		types.add(ASSIGNATION);
		types.add(CALCULATION);
		return types;
	}
	
	public static Set<Type> getVoidComparisonLogicOrReference(){
		Set<Type> types = new HashSet<Type>();
		types.add(VOID);
		types.add(COMPARISON);
		types.add(LOGIC);
		types.add(TREE_OBJECT_REFERENCE);
		return types;
	}
}
