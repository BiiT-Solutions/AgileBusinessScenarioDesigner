package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.abcd.annotation.AutoLogger;
import com.biit.abcd.annotation.AutoLoggerLevel;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.DependencyExistException;
import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.liferay.portal.model.User;

/**
 * Basic functionality of the hierarchy of the elements of the form.
 */
@Entity
@Table(name = "TREE_OBJECTS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TreeObject extends StorableObject {

	// MySQL unique keys are limited to 767 bytes that in utf8mb4 are ~185. Form name is unique.
	@Column(length = 185)
	private String name;

	// For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
	@Column(nullable = false)
	private long sortSeq = 0;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "PARENT_OF_CHILDREN")
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
				throw new NotValidChildException("Class '" + child.getClass().getName()
						+ "' does not allows instances of '" + this.getClass().getName() + "' as parent.");
			}
		}
	}

	public void updateChildrenSortSeqs() {
		for (int i = 0; i < getChildren().size(); i++) {
			getChildren().get(i).setSortSeq(i);
			getChildren().get(i).updateChildrenSortSeqs();
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

	public void remove() throws DependencyExistException {
		checkDependencies();
		if (getParent() != null) {
			try {
				getParent().removeChild(this);
			} catch (Exception e) {
				AbcdLogger.severe(this.getClass().getName(), "Element not removed correctly");
				e.printStackTrace();
			}
		}
	}

	public void removeChild(TreeObject elementToRemove) throws ChildrenNotFoundException, DependencyExistException {
		boolean removed = false;
		if (getChildren().contains(elementToRemove)) {
			elementToRemove.checkDependencies();
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

	/**
	 * This element or any of its children has a dependency. Checks if it is used in a TableRule, Diagram or Expression.
	 */
	public void checkDependencies() throws DependencyExistException {
		Form form = getForm();
		if (form != null) {
			// Check dependencies with TableRules.
			for (TableRule tableRule : form.getTableRules()) {
				for (TableRuleRow row : tableRule.getRules()) {
					for (Expression condition : row.getConditions()) {
						// TODO checks agains table rule.
					}
				}
			}

			// Checks dependencies with Diagram
			// TODO

			// Checks dependencies with Expressions
			// TODO
		}
		for (TreeObject child : getChildren()) {
			child.checkDependencies();
		}
	}

	public void removeChild(int index) throws ChildrenNotFoundException, DependencyExistException {
		if (getChildren() == null || getChildren().size() < index) {
			throw new ChildrenNotFoundException("Index out of bounds. Index " + index + " is invalid.");
		} else {
			getChildren().get(index).checkDependencies();
			getChildren().remove(index);
		}
	}

	public void switchChildren(int indexChild1, int indexChild2, User user) throws ChildrenNotFoundException {
		if ((indexChild1 >= 0 && indexChild1 < getChildren().size())
				&& (indexChild2 >= 0 && indexChild2 < getChildren().size())) {
			Collections.swap(getChildren(), indexChild1, indexChild2);
			// Update elements date modification.
			if (user != null) {
				getChildren().get(indexChild1).setUpdatedBy(user.getUserId());
				getChildren().get(indexChild2).setUpdatedBy(user.getUserId());
			}
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
	 * Gets the inner group where this element is included.
	 * 
	 * @return
	 */
	public Form getForm() {
		if (this instanceof Form) {
			return (Form) this;
		}
		while (getParent() != null) {
			if (getParent() instanceof Form) {
				return (Form) getParent();
			}
			return getParent().getForm();
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

	@Override
	public void resetIds() {
		super.resetIds();
		for (TreeObject child : getChildren()) {
			child.resetIds();
		}
	}

	public long getSortSeq() {
		return sortSeq;
	}

	public void setSortSeq(long sortSeq) {
		this.sortSeq = sortSeq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws FieldTooLongException {
		if (name.length() > 185) {
			throw new FieldTooLongException("Name is limited to 185 characters due to database restrictions. ");
		}
		this.name = name;
	}

	/**
	 * Function to return the list of questions that a TreeObject contains.
	 * 
	 * @return
	 */
	public List<Question> getQuestions() {
		List<Question> questions = new ArrayList<Question>();
		if (this instanceof Question) {
			questions.add((Question) this);
			return questions;
		}
		for (TreeObject child : getChildren()) {
			if (child instanceof Question) {
				questions.add((Question) child);
				continue;
			}
			if (child instanceof Category || child instanceof Group) {
				questions.addAll(child.getQuestions());
				continue;
			}
		}
		return questions;
	}

	@Override
	public void setUpdateTime(Timestamp dateUpdated) {
		super.setUpdateTime(dateUpdated);
		if (getParent() != null) {
			getParent().setUpdateTime(dateUpdated);
		}
	}

	@Override
	public void setUpdatedBy(Long updatedBy) {
		super.setUpdatedBy(updatedBy);
		if (getParent() != null) {
			getParent().setUpdatedBy(updatedBy);
		}
	}

	/**
	 * Gets the common tree object for a set of treeObjects
	 * 
	 * @param treeObjects
	 * @return
	 */
	public static TreeObject getCommonTreeObject(Set<TreeObject> treeObjects) {
		if (treeObjects == null || treeObjects.isEmpty()) {
			return null;
		}

		Set<TreeObject> temp = new HashSet<>(treeObjects);

		// Get the current least common level
		int commonMinLevel = temp.iterator().next().getLevel();
		for (TreeObject object : temp) {
			if (commonMinLevel > object.getLevel()) {
				commonMinLevel = object.getLevel();
			}
		}
		while (temp.size() > 1) {
			// Now we take only the elements in the common level or parents that
			// are in the common level.
			Set<TreeObject> nextTemp = new HashSet<>();
			for (TreeObject object : temp) {
				TreeObject candidate = object;
				while (candidate.getLevel() > commonMinLevel) {
					candidate = candidate.getParent();
				}
				nextTemp.add(candidate);
			}
			temp = nextTemp;

			// Reduce the level and execute the loop to get Battle Royale.
			commonMinLevel--;
		}
		return temp.iterator().next();
	}

	public int getLevel() {
		if (parent == null) {
			return 0;
		} else {
			return parent.getLevel() + 1;
		}
	}
}
