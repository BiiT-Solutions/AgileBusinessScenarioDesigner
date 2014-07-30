package com.biit.abcd.core.drools.rules;

import java.util.HashMap;

public class GlobalVariablesMap {

	// Store the form variables: Scope | Name | Value
	private HashMap<String, HashMap<String, Object>> formVariables;
	private static GlobalVariablesMap INSTANCE = new GlobalVariablesMap();

	private GlobalVariablesMap(){}

	public static GlobalVariablesMap getInstance(){
		return INSTANCE;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public HashMap<String, Object> getVariablesFromScope(String scope){
		if(formVariables == null){
			formVariables = new HashMap<String, HashMap<String,Object>>();
			return null;
		} else {
			return formVariables.get(scope);
		}
	}

	public void setVariableValue(String scope, String variable, Object value){
		if(formVariables == null){
			formVariables = new HashMap<String, HashMap<String,Object>>();
		}
		if(formVariables.get(scope) == null){
			HashMap<String, Object> nameValue = new HashMap<String, Object>();
			nameValue.put(variable, value);
			formVariables.put(scope, nameValue);
		}else{
			formVariables.get(scope).put(variable, value);
		}
	}

	public Object getVariableValue(String scope, String name){
		if((formVariables != null) && (formVariables.get(scope) != null)) {
			return formVariables.get(scope).get(name);
		}
		return null;
	}

}
