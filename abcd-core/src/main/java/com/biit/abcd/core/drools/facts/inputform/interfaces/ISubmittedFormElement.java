package com.biit.abcd.core.drools.facts.inputform.interfaces;

import java.util.List;

import com.biit.abcd.persistence.entity.CustomVariableScope;

public interface ISubmittedFormElement {

	public String getName();
	
	public String getOriginalValue();
	
	public List<ISubmittedFormElement> getChildren();
	
	public boolean isScoreSet(String varName);

	public boolean isScoreSet(Object submittedFormTreeObject, String varName);

	public Object getVariableValue(String varName);

	public Object getVariableValue(Object submmitedFormObject, String varName);

	public void setVariableValue(String varName, Object value);

	public void setVariableValue(Object submmitedFormObject, String varName, Object value);
	
	public CustomVariableScope getVariableScope();
}
