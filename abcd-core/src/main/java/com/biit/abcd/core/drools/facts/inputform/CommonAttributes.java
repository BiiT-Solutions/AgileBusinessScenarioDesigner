package com.biit.abcd.core.drools.facts.inputform;

import com.biit.abcd.core.drools.facts.interfaces.ICommonAttributes;


public abstract class CommonAttributes implements ICommonAttributes {

	// Tasg of the Orbeon form
	private String tag;
	// The real name of the element
	private String text;

	@Override
	public String toString() {
		return tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
