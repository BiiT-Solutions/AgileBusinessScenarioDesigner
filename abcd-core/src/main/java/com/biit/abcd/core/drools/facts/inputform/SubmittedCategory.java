package com.biit.abcd.core.drools.facts.inputform;

import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.orbeon.form.ISubmittedObject;

public class SubmittedCategory extends com.biit.form.submitted.SubmittedCategory implements ISubmittedFormElement {

	public SubmittedCategory(String tag) {
		super(tag);
	}

	public boolean isScoreNotSet(String varName) {
		return !isScoreSet(varName);
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		return isScoreSet(this, varName);
	}

	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		// Retrieve the form which will have the variables
		return ((SubmittedForm) getParent()).isScoreSet(submittedFormTreeObject, varName);
	}

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public Object getVariableValue(Object submmitedFormObject, String varName) {
		return ((SubmittedForm) this.getParent()).getVariableValue(submmitedFormObject, varName);
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		((SubmittedForm) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
	}

	@Override
	public String generateXML(String tabs) {
		String xmlFile = tabs + "<" + getTag() + " type=\"" + this.getClass().getSimpleName() + "\"" + ">\n";
		for (ISubmittedObject child : getChildren()) {
			xmlFile += ((ISubmittedFormElement) child).generateXML(tabs + "\t");
		}
		xmlFile += tabs + "</" + getTag() + ">\n";
		return xmlFile;
	}

	@Override
	public String getName() {
		return getTag();
	}

	@Override
	public String getOriginalValue() {
		return "";
	}

	@Override
	public CustomVariableScope getVariableScope() {
		return CustomVariableScope.CATEGORY;
	}
}
