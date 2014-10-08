package com.biit.abcd.webpages.elements.formdesigner;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;

public class RootForm extends SimpleFormView {

	private List<SimpleFormView> childForms;

	public RootForm(String label) {
		setLabel(label);
		childForms = new ArrayList<SimpleFormView>();
	}

	@Override
	public Integer getVersion() {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public void setVersion(Integer version) {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	public SimpleFormView getLastFormVersion() {
		int numVersion = 0;
		SimpleFormView lastVersion = null;
		for (SimpleFormView form : getChildForms()) {
			if (form.getVersion() > numVersion) {
				lastVersion = form;
			}
		}
		return lastVersion;
	}

	public List<SimpleFormView> getChildForms() {
		return childForms;
	}

	public void addChildForm(SimpleFormView form) {
		childForms.add(form);
	}
}
