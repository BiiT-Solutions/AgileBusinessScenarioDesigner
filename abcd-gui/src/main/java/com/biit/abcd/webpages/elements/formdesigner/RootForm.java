package com.biit.abcd.webpages.elements.formdesigner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.form.TreeObject;
import com.liferay.portal.model.UserGroup;

public class RootForm extends Form {
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(Arrays.asList(Form.class));

	private String name;

	public RootForm(String name) {
		this.name = name;
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getVersion() {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public void setVersion(Integer version) {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public void increaseVersion() {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public UserGroup getUserGroup() {
		throw new UnsupportedOperationException("Forms Root cannot use this method.");
	}

	@Override
	public Long getCreatedBy() {
		return null;
	}

	@Override
	public Timestamp getCreationTime() {
		return null;
	}

	@Override
	public Timestamp getUpdateTime() {
		return null;
	}

	@Override
	public Long getUpdatedBy() {
		return null;
	}

	@Override
	public Long getId() {
		return null;
	}
}
