package com.biit.abcd.core.drools.facts.inputform;

import com.biit.orbeon.form.ICommonAttributes;

public abstract class CommonAttributes implements ICommonAttributes {

	// Tags of the Orbeon form
	private String tag;
	// The real name of the element
	private String text;

	@Override
	public String toString() {
		return tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	// Abstract methods to manage the scores
	public abstract boolean isScoreSet(String varName);
	public abstract boolean isScoreSet(Object submittedFormTreeObject, String varName);
	public abstract boolean isScoreNotSet(String varName);

	public abstract Object getVariableValue(String varName);
	public abstract Object getVariableValue(Object submmitedFormObject, String varName);

	public abstract void setVariableValue(String varName, Object value);
	public abstract void setVariableValue(Object submmitedFormObject, String varName, Object value);
}
