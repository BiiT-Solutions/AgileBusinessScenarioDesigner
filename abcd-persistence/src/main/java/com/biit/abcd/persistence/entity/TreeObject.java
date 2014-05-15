package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.biit.abcd.annotation.AutoLogger;
import com.biit.abcd.annotation.AutoLoggerLevel;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.liferay.portal.model.User;

/**
 * Basic functionality of the hierarchy of the elements of the form.
 */
@Entity
@Table(name = "TREE_OBJECTS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TreeObject {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(nullable = false)
	private Timestamp creationDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long createdBy = null;
	private Timestamp updatedDate = null;
	@Column(columnDefinition = "DOUBLE")
	private Long updatedBy = null;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "PARENT_OF_CHILDREN")
	@OrderColumn(name = "children_index")
	private List<TreeObject> children;
	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject parent;

	public TreeObject() {
		setCreationTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	}

	/**
	 * Gets all children of the treeObject. These annotations are in the method because must been overwritten by the
	 * Form object. All objects but forms must be FetchType.EAGER.
	 */
	public List<TreeObject> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}

	public void addChild(int index, TreeObject child) throws NotValidChildException {
		if (!getAllowedChilds().contains(child.getClass())) {
			throw new NotValidChildException("Class '" + this.getClass().getName() + "' does not allows instances of '"
					+ child.getClass().getName() + "' as childs.");
		}
		if (!getChildren().contains(child)) {
			// Remove the child from previous parent.
			if (child.getParent() != null && child.getParent() != this) {
				child.getParent().getChildren().remove(child);
			}
			getChildren().add(index, child);
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
			}
		}
	}

	@AutoLogger(AutoLoggerLevel.DEBUG)
	public void addChild(TreeObject child) throws NotValidChildException {
		if (getChildren() == null) {
			setChildren(new ArrayList<TreeObject>());
		}
		if (getAllowedChilds() == null || !getAllowedChilds().contains(child.getClass())) {
			throw new NotValidChildException("Class '" + this.getClass().getName() + "' does not allows instances of '"
					+ child.getClass().getName() + "' as child.");
		}
		if (!getChildren().contains(child)) {
			// Remove the child from previous parent.
			if (child.getParent() != null && child.getParent() != this) {
				child.getParent().getChildren().remove(child);
			}
			getChildren().add(child);
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
				throw new NotValidChildException("Class '" + child.getClass().getName()
						+ "' does not allows instances of '" + this.getClass().getName() + "' as parent.");
			}
		}
	}

	public boolean contains(TreeObject child) {
		return contains(child, getChildren());
	}

	private boolean contains(TreeObject treeObject, List<TreeObject> children) {
		for (TreeObject child : children) {
			if (child.equals(treeObject)) {
				return true;
			}
			if (child.contains(treeObject)) {
				return true;
			}
		}
		return false;
	}

	public void remove() {
		if (getParent() != null) {
			try {
				getParent().removeChild(this);
			} catch (Exception e) {
				AbcdLogger.severe(this.getClass().getName(), "Element not removed correctly");
			}
		}
	}

	public void removeChild(TreeObject elementToRemove) throws ChildrenNotFoundException {
		boolean removed = false;
		if (getChildren().contains(elementToRemove)) {
			getChildren().remove(elementToRemove);
			removed = true;
		} else {
			for (TreeObject child : getChildren()) {
				try {
					child.removeChild(elementToRemove);
					// Removed, not continue searching.
					removed = true;
					break;
				} catch (ChildrenNotFoundException cnfe) {
					// Not found, continue the loop.
				}
			}
		}
		if (!removed) {
			throw new ChildrenNotFoundException("Children '" + elementToRemove + "' does not exist.");
		}
	}

	public void removeChild(int index) throws ChildrenNotFoundException {
		if (getChildren() == null || getChildren().size() < index) {
			throw new ChildrenNotFoundException("Index out of bounds. Index " + index + " is invalid.");
		} else {
			getChildren().remove(index);
		}
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public Timestamp getCreationTime() {
		if (creationDate != null) {
			return creationDate;
		} else {
			creationDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return creationDate;
		}
	}

	public void setUpdateTime() {
		setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	}

	public Timestamp getUpdateTime() {
		if (updatedDate != null) {
			return updatedDate;
		} else {
			updatedDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return updatedDate;
		}
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedBy(User user) {
		if (user != null) {
			this.createdBy = user.getUserId();
		}
	}

	public void setCreationTime(Timestamp dateCreated) {
		this.creationDate = dateCreated;
	}

	public void setUpdateTime(Timestamp dateUpdated) {
		this.updatedDate = dateUpdated;
		if (getParent() != null) {
			getParent().setUpdateTime(dateUpdated);
		}
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
		if (getParent() != null) {
			getParent().setUpdatedBy(updatedBy);
		}
	}

	public void setUpdatedBy(User user) {
		if (user != null) {
			this.updatedBy = user.getUserId();
		}
	}

	public void switchChildren(int indexChild1, int indexChild2, User user) throws ChildrenNotFoundException {
		if ((indexChild1 >= 0 && indexChild1 < getChildren().size())
				&& (indexChild2 >= 0 && indexChild2 < getChildren().size())) {
			Collections.swap(getChildren(), indexChild1, indexChild2);
			// Update elements date modification.
			getChildren().get(indexChild1).setUpdatedBy(user.getUserId());
			getChildren().get(indexChild2).setUpdatedBy(user.getUserId());
		} else {
			if (indexChild1 > indexChild2) {
				throw new ChildrenNotFoundException("Index out of bounds. Index " + indexChild1 + " is invalid.");
			} else {
				throw new ChildrenNotFoundException("Index out of bounds. Index " + indexChild2 + " is invalid.");
			}
		}
	}

	public TreeObject getChild(int index) throws ChildrenNotFoundException {
		if (getChildren() == null || getChildren().size() < index) {
			throw new ChildrenNotFoundException("Index out of bounds. Index " + index + " is invalid.");
		} else {
			return getChildren().get(index);
		}
	}

	protected abstract List<Class<?>> getAllowedChilds();

	public void setChildren(List<TreeObject> children) throws NotValidChildException {
		// Only allowed classes can be a child.
		for (TreeObject child : children) {
			if (!getAllowedChilds().contains(child.getClass())) {
				throw new NotValidChildException("Class '" + this.getClass().getName()
						+ "' does not allows instances of '" + child.getClass().getName() + "' as childs.");
			}
		}

		// Set childs.
		this.children = new ArrayList<>();
		if (children != null) {
			for (TreeObject child : children) {
				this.children.add(child);
			}
		}
		// this.children = children;

		// Update parents.
		for (TreeObject child : children) {
			// Remove the child from previous parent.
			if (child.getParent() != null && child.getParent() != this) {
				child.getParent().getChildren().remove(child);
			}
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
			}
		}
	}

	protected abstract List<Class<?>> getAllowedParents();

	public void setParent(TreeObject parent) throws NotValidParentException {
		if (parent == null) {
			this.parent = null;
		} else {
			if (!getAllowedParents().contains(parent.getClass())) {
				throw new NotValidParentException("Class '" + this.getClass().getName()
						+ "' does not allows instances of '" + parent.getClass().getName() + "' as parent.");
			}
			this.parent = parent;
		}
	}

	public TreeObject getParent() {
		return parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the category in which this element is included.
	 * 
	 * @return
	 */
	public Category getCategory() {
		if (this instanceof Category) {
			return (Category) this;
		}
		while (getParent() != null) {
			if (getParent() instanceof Category) {
				return (Category) getParent();
			}
			return getParent().getCategory();
		}
		return null;
	}

	/**
	 * Gets the inner group where this element is included.
	 * 
	 * @return
	 */
	public Group getGroup() {
		if (this instanceof Group) {
			return (Group) this;
		}
		while (getParent() != null) {
			if (getParent() instanceof Group) {
				return (Group) getParent();
			}
			return getParent().getGroup();
		}
		return null;
	}

	/**
	 * Return the last element in order of this category.
	 * 
	 * @return
	 */
	public TreeObject getLastElement() {
		if (getChildren().size() > 0) {
			return getChildren().get(getChildren().size() - 1).getLastElement();
		}
		return this;
	}

	public void resetIds() {
		setId(null);
		for (TreeObject child : getChildren()) {
			child.resetIds();
		}
	}
}
