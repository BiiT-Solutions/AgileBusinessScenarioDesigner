package com.biit.abcd.webpages.elements.formdesigner;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;

public class RootForm extends SimpleFormView {

	private List<SimpleFormView> childForms;
	private String rootName;

	public RootForm(String label, long organizationId) {
		setLabel(label);
		setOrganizationId(organizationId);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
		result = prime * result + (int) (getOrganizationId() ^ (getOrganizationId() >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RootForm other = (RootForm) obj;
		if (getLabel() == null) {
			if (other.getLabel() != null)
				return false;
		} else if (!getLabel().equals(other.getLabel()))
			return false;
		if (getOrganizationId() != other.getOrganizationId())
			return false;
		return true;
	}
	
	
}
