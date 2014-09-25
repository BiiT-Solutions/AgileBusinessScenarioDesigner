package com.biit.abcd.core.drools.facts.inputform;

public interface IDroolsForm {

	public boolean isScoreSet(String varName);

	public boolean isScoreSet(Object submittedFormTreeObject, String varName);

	public Object getVariableValue(String varName);

	public Object getVariableValue(Object submmitedFormObject, String varName);

	public void setVariableValue(String varName, Object value);

	public void setVariableValue(Object submmitedFormObject, String varName, Object value);
}
