package com.biit.abcd.core.drools.facts.inputform;

import com.biit.orbeon.form.ICommonAttributes;

public abstract class SubmittedFormObject implements ICommonAttributes {

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
}
