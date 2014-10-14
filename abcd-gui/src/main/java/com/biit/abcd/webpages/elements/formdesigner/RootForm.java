package com.biit.abcd.webpages.elements.formdesigner;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;

public class RootForm extends SimpleFormView {

	private List<SimpleFormView> childForms;
	private String rootName;

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
		int lastVersionChecked = 0;
		SimpleFormView lastVersionForm = null;
		for (SimpleFormView form : getChildForms()) {
			if (form.getVersion() > lastVersionChecked) {
				lastVersionForm = form;
				lastVersionChecked = form.getVersion();
			}
		}
		return lastVersionForm;
	}

	public List<SimpleFormView> getChildForms() {
		return childForms;
	}

	public void addChildForm(SimpleFormView form) {
		childForms.add(form);
	}

	/**
	 * Adds a child in the specified index.
	 * 
	 * @param form
	 * @param index
	 */
	public void addChildForm(SimpleFormView form, int index) {
		if (index >= 0 && index < childForms.size()) {
			childForms.add(index, form);
		} else {
			addChildForm(form);
		}
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
}
