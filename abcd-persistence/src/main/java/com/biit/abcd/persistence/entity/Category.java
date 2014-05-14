package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;

@Entity
@Table(name = "CATEGORIES", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "form" }) })
public class Category extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Form.class));

	private String label;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "form")
	private Form form;

	public Category() {
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Form parameter is created for creating a unique key.
	 * 
	 * @return
	 */
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public void setParent(TreeObject parent) throws NotValidParentException {
		super.setParent(parent);
		setForm((Form) parent);
	}

	@Override
	public String toString() {
		return getLabel();
	}

}
