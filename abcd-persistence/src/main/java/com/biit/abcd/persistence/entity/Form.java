package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.liferay.portal.model.UserGroup;

@Entity
@Table(name = "FORMS", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version" }) })
public class Form extends TreeObject {
	private static final String DEFAULT_NAME = "New Form";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Category.class));

	private String name;
	private Integer version = 1;

	@Column(nullable = false)
	private Timestamp availableFrom;
	private Timestamp availableTo;

	public Form() {
		setName(DEFAULT_NAME);
	}

	/**
	 * Gets all children of the form. This annotations are in the method because overwrites the TreeObject. Forms'
	 * children must use FetchType.LAZY.
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinTable(name = "CHILDRENS_RELATIONSHIP")
	@OrderColumn(name = "children_index")
	public List<TreeObject> getChildren() {
		return super.getChildren();
	}

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
		throw new NotValidParentException("Forms cannot have a parent.");
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
		this.resetIds();
	}

	public UserGroup getUserGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		if (availableFrom == null) {
			this.availableFrom = dateCreated;
		}
		super.setCreationTime(dateCreated);
	}

	public Timestamp getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Timestamp availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Timestamp getAvailableTo() {
		return availableTo;
	}

	public void setAvailableTo(Timestamp availableTo) {
		this.availableTo = availableTo;
	}

}
