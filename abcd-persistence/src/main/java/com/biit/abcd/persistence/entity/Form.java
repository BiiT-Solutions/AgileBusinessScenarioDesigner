package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.liferay.portal.model.UserGroup;

@Entity
@Table(name = "FORMS", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version" }) })
public class Form extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Category.class));

	private String name;
	private Integer version = 1;

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return null;
	}

	@Override
	public void setParent(TreeObject parent) throws NotValidParentException {
		throw new NotValidParentException("Categories cannot have a parent.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void increaseVersion() {
		this.version++;
		// Force to be stored as a new record
		this.setId(null);
	}

	public UserGroup getGroup() {
		// TODO Auto-generated method stub
		return null;
	}
}
