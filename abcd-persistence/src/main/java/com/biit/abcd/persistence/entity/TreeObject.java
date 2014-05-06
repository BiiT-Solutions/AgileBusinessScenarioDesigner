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

import org.hibernate.annotations.Cascade;

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
public abstract class TreeObject implements ITreeObject {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	private Timestamp creationDate = null;
	private User createdBy = null;
	private Timestamp updatedDate = null;
	private User updatedBy = null;
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@JoinTable(name = "CHILDRENS_RELATIONSHIP")
	@OrderColumn(name = "children_index")
	private List<TreeObject> children;
	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject parent;

	public List<TreeObject> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}

	@Override
	public void addChild(int index, ITreeObject child) throws NotValidChildException {
		if (!getAllowedChilds().contains(child.getClass())) {
			throw new NotValidChildException("Class '" + this.getClass().getName() + "' does not allows instances of '"
					+ child.getClass().getName() + "' as childs.");
		}
		if (!getChildren().contains(child)) {
			// Remove the child from previous parent.
			if (((TreeObject) child).getParent() != null && ((TreeObject) child).getParent() != this) {
				((TreeObject) child).getParent().getChildren().remove(child);
			}
			getChildren().add(index, (TreeObject) child);
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
			}
		}
	}

	@AutoLogger(AutoLoggerLevel.DEBUG)
	@Override
	public void addChild(ITreeObject child) throws NotValidChildException {
		if (getChildren() == null) {
			setChildren(new ArrayList<ITreeObject>());
		}
		if (getAllowedChilds() == null || !getAllowedChilds().contains(child.getClass())) {
			throw new NotValidChildException("Class '" + this.getClass().getName() + "' does not allows instances of '"
					+ child.getClass().getName() + "' as child.");
		}
		if (!getChildren().contains(child)) {
			// Remove the child from previous parent.
			if (((TreeObject) child).getParent() != null && ((TreeObject) child).getParent() != this) {
				((TreeObject) child).getParent().getChildren().remove(child);
			}
			getChildren().add((TreeObject) child);
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
				throw new NotValidChildException("Class '" + child.getClass().getName()
						+ "' does not allows instances of '" + this.getClass().getName() + "' as parent.");
			}
		}
	}

	@Override
	public boolean contains(ITreeObject child) {
		return contains(child, getChildren());
	}

	private boolean contains(ITreeObject treeObject, List<TreeObject> children) {
		for (ITreeObject child : children) {
			if (child.equals(treeObject)) {
				return true;
			}
			if (child.contains(treeObject)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void remove() {
		if (getParent() != null) {
			try {
				getParent().removeChild(this);
			} catch (Exception e) {
				AbcdLogger.severe(this.getClass().getName(), "Element not removed correctly");
			}
		}
	}

	@Override
	public void removeChild(ITreeObject elementToRemove) throws ChildrenNotFoundException {
		if (getChildren().contains(elementToRemove)) {
			getChildren().remove(elementToRemove);
		} else {
			for (ITreeObject child : getChildren()) {
				try {
					child.removeChild(elementToRemove);
					// Removed, not continue searching.
					break;
				} catch (ChildrenNotFoundException cnfe) {
					// Not found, continue the loop.
				}
			}
		}
		throw new ChildrenNotFoundException("Children '" + elementToRemove + "' does not exist.");
	}

	@Override
	public void removeChild(int index) throws ChildrenNotFoundException {
		if (getChildren() == null || getChildren().size() < index) {
			throw new ChildrenNotFoundException("Index out of bounds. Index " + index + " is invalid.");
		} else {
			getChildren().remove(index);
		}
	}

	@Override
	public User getCreatedBy() {
		return createdBy;
	}

	@Override
	public Timestamp getCreationTime() {
		if (creationDate != null) {
			return creationDate;
		} else {
			creationDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return creationDate;
		}
	}

	@Override
	public void setUpdateTime() {
		setUpdateTime(new java.sql.Timestamp(new java.util.Date().getTime()));
	}

	@Override
	public Timestamp getUpdateTime() {
		if (updatedDate != null) {
			return updatedDate;
		} else {
			updatedDate = new java.sql.Timestamp(new java.util.Date().getTime());
			return updatedDate;
		}
	}

	@Override
	public User getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		this.creationDate = dateCreated;
	}

	@Override
	public void setUpdateTime(Timestamp dateUpdated) {
		this.updatedDate = dateUpdated;
		if (getParent() != null) {
			getParent().setUpdateTime(dateUpdated);
		}
	}

	@Override
	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
		if (getParent() != null) {
			getParent().setUpdatedBy(updatedBy);
		}
	}

	@Override
	public void switchChildren(int indexChild1, int indexChild2, User user) throws ChildrenNotFoundException {
		if ((indexChild1 >= 0 && indexChild1 < getChildren().size())
				&& (indexChild2 >= 0 && indexChild2 < getChildren().size())) {
			Collections.swap(getChildren(), indexChild1, indexChild2);
			// Update elements date modification.
			getChildren().get(indexChild1).setUpdatedBy(user);
			getChildren().get(indexChild2).setUpdatedBy(user);
		} else {
			if (indexChild1 > indexChild2) {
				throw new ChildrenNotFoundException("Index out of bounds. Index " + indexChild1 + " is invalid.");
			} else {
				throw new ChildrenNotFoundException("Index out of bounds. Index " + indexChild2 + " is invalid.");
			}
		}
	}

	@Override
	public ITreeObject getChild(int index) throws ChildrenNotFoundException {
		if (getChildren() == null || getChildren().size() < index) {
			throw new ChildrenNotFoundException("Index out of bounds. Index " + index + " is invalid.");
		} else {
			return getChildren().get(index);
		}
	}

	protected abstract List<Class<?>> getAllowedChilds();

	@Override
	public void setChildren(List<ITreeObject> children) throws NotValidChildException {
		// Only allowed classes can be a child.
		for (ITreeObject child : children) {
			if (!getAllowedChilds().contains(child.getClass())) {
				throw new NotValidChildException("Class '" + this.getClass().getName()
						+ "' does not allows instances of '" + child.getClass().getName() + "' as childs.");
			}
		}

		// Set childs.
		this.children = new ArrayList<>();
		if (children != null) {
			for (ITreeObject child : children) {
				this.children.add((TreeObject) child);
			}
		}
		// this.children = children;

		// Update parents.
		for (ITreeObject child : children) {
			// Remove the child from previous parent.
			if (((TreeObject) child).getParent() != null && ((TreeObject) child).getParent() != this) {
				((TreeObject) ((TreeObject) child).getParent()).getChildren().remove(child);
			}
			try {
				child.setParent(this);
			} catch (NotValidParentException e) {
			}
		}
	}

	protected abstract List<Class<?>> getAllowedParents();

	@Override
	public void setParent(ITreeObject parent) throws NotValidParentException {
		if (parent == null) {
			this.parent = null;
		} else {
			if (!getAllowedParents().contains(parent.getClass())) {
				throw new NotValidParentException("Class '" + this.getClass().getName()
						+ "' does not allows instances of '" + parent.getClass().getName() + "' as parent.");
			}
			this.parent = (TreeObject) parent;
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
}
