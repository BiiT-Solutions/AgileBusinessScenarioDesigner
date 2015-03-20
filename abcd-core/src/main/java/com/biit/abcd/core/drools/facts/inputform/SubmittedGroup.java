package com.biit.abcd.core.drools.facts.inputform;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.orbeon.form.ISubmittedObject;

public class SubmittedGroup extends com.biit.form.submitted.SubmittedGroup implements ISubmittedFormElement {

	public SubmittedGroup(String tag) {
		super(tag);
	}

	@Override
	public boolean isVariableDefined(String varName) {
		return isVariableDefined(this, varName);
	}

	@Override
	public boolean isVariableDefined(Object submittedFormTreeObject, String varName) {
		return ((ISubmittedFormElement) getParent()).isVariableDefined(submittedFormTreeObject, varName);
	}

	public boolean isScoreNotSet(String varName) {
		return !isVariableDefined(varName);
	}

	@Override
	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	@Override
	public Object getVariableValue(Object submmitedFormObject, String varName) {
		return ((ISubmittedFormElement) this.getParent()).getVariableValue(submmitedFormObject, varName);
	}

	@Override
	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	@Override
	public Object getVariableValue(Class<?> type, String varName) {
		List<ISubmittedObject> childs = getChildren(type);

		if (childs != null && !childs.isEmpty()) {
			return getVariableValue(childs.get(0), varName);
		}
		return null;
	}

	@Override
	public Object getVariableValue(Class<?> type, String treeObjectName, String varName) {
		ISubmittedObject child = getChild(type, treeObjectName);

		if (child != null) {
			return getVariableValue(child, varName);
		}
		return null;
	}

	@Override
	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		((ISubmittedFormElement) getParent()).setVariableValue(submmitedFormObject, varName, value);
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
	public String toString() {
		return getName();
	}
}
