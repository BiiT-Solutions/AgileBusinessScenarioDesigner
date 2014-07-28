package com.biit.abcd.core.drools.rules;

import java.util.HashMap;

import com.biit.abcd.persistence.entity.CustomVariableScope;

public class VariablesMap {

	// Store the form variables: Scope | Name | Value
	private HashMap<CustomVariableScope, HashMap<String, Object>> formVariables;
	private static VariablesMap INSTANCE = new VariablesMap();

	private VariablesMap(){}

	public static VariablesMap getInstance(){
		return INSTANCE;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public HashMap<String, Object> getVariablesFromScope(CustomVariableScope scope){
		if(formVariables == null){
			formVariables = new HashMap<CustomVariableScope, HashMap<String,Object>>();
			return null;
		} else {
			return formVariables.get(scope);
		}
	}

	public void addVariableValue(CustomVariableScope scope, String variable, Object value){
		if(formVariables == null){
			formVariables = new HashMap<CustomVariableScope, HashMap<String,Object>>();
		}
		if(formVariables.get(scope) == null){
			HashMap<String, Object> nameValue = new HashMap<String, Object>();
			nameValue.put(variable, value);
			formVariables.put(scope, nameValue);
		}else{
			formVariables.get(scope).put(variable, value);
		}
	}

	public Object getVariableValue(CustomVariableScope scope, String name){
		if((formVariables != null) && (formVariables.get(scope) != null)) {
			return formVariables.get(scope).get(name);
		}
		return null;
	}

}
