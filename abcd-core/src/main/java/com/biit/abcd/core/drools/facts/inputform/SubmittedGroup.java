package com.biit.abcd.core.drools.facts.inputform;

import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.ISubmittedObject;

public class SubmittedGroup extends com.biit.form.submitted.SubmittedGroup implements ISubmittedFormElement {

	public SubmittedGroup(String tag) {
		super(tag);
	}

	public boolean isScoreSet(String varName) {
		return isScoreSet(this, varName);
	}

	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((SubmittedCategory) getParent()).isScoreSet(submittedFormTreeObject, varName);
		} else {
			return ((SubmittedGroup) getParent()).isScoreSet(submittedFormTreeObject, varName);
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !isScoreSet(varName);
	}

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public Object getVariableValue(Object submmitedFormObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((SubmittedCategory) this.getParent()).getVariableValue(submmitedFormObject, varName);
		} else {
			return ((SubmittedGroup) this.getParent()).getVariableValue(submmitedFormObject, varName);
		}
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		if (this.getParent() instanceof ICategory) {
			((SubmittedCategory) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		} else {
			((SubmittedGroup) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		}
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
		return CustomVariableScope.GROUP;
	}
}
