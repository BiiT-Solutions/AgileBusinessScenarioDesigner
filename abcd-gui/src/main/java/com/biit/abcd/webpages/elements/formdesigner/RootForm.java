package com.biit.abcd.webpages.elements.formdesigner;

import com.biit.abcd.persistence.entity.SimpleFormView;

public class RootForm extends SimpleFormView {

	public RootForm(String label) {
		setLabel(label);
	}

	@Override
	public Integer getVersion() {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public void setVersion(Integer version) {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}
}
