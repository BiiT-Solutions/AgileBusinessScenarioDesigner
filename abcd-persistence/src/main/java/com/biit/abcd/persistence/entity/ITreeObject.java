package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;
import java.util.List;

import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;
import com.liferay.portal.model.User;

public interface ITreeObject {

	/**
	 * Adds a child in a specific position.
	 * 
	 * @param index
	 * @param child
	 */
	public void addChild(int index, ITreeObject child) throws NotValidChildException;

	/**
	 * Adds a child at the end of the children lists.
	 * 
	 * @param child
	 */
	public void addChild(ITreeObject child) throws NotValidChildException;

	/**
	 * Gets the child in the selected position.
	 * 
	 * @param index
	 * @return
	 */
	public ITreeObject getChild(int index) throws ChildrenNotFoundException;

	/**
	 * Exchange current childrens for this list.
	 * 
	 * @param children
	 * @throws NotValidParentException
	 */
	public void setChildren(List<ITreeObject> children) throws NotValidChildException, NotValidParentException;

	/**
	 * Gets all childrens.
	 * 
	 * @return
	 */
	public List<ITreeObject> getChildren();

	/**
	 * Removes this children.
	 * 
	 * @param child
	 * @return
	 */
	public void removeChild(ITreeObject child) throws ChildrenNotFoundException;

	/**
	 * Removes the children in the specific position.
	 * 
	 * @param index
	 * @return
	 */
	public void removeChild(int index) throws ChildrenNotFoundException;

	/**
	 * Removes this element.
	 */
	public void remove();

	/**
	 * Changes the parent of the element.
	 * 
	 * @param parent
	 */
	public void setParent(ITreeObject parent) throws NotValidParentException;

	/**
	 * Gets the parent of the element.
	 * 
	 * @return
	 */
	public ITreeObject getParent();

	/**
	 * Check if the child exists or not.
	 * 
	 * @param child
	 * @return
	 */
	public boolean contains(ITreeObject child);

	/**
	 * Gets the user that has created this object.
	 * 
	 * @return
	 */
	public User getCreatedBy();

	/**
	 * Gets the time when this object was created.
	 * 
	 * @return
	 */
	public Timestamp getCreationTime();

	/**
	 * Sets the date of last modification of this object.
	 */
	public void setUpdateTime(Timestamp dateCreated);

	/**
	 * Sets the date of last modification of this object.
	 */
	public void setUpdateTime();

	/**
	 * Updates the date of updating.
	 * 
	 * @return
	 */
	public Timestamp getUpdateTime();

	/**
	 * Gets the last user who has updated the element.
	 * 
	 * @return
	 */
	public User getUpdatedBy();

	/**
	 * Defines the user who has created the object.
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(User createdBy);

	/**
	 * Sets the time of creation of this objeect.
	 * 
	 * @param dateCreated
	 */
	public void setCreationTime(Timestamp dateCreated);

	/**
	 * Defines the last user who has updated this object.
	 * 
	 * @param updatedBy
	 */
	public void setUpdatedBy(User updatedBy);

	/**
	 * Exchanges the position of two children.
	 * 
	 * @param indexChild1
	 * @param indexChild2
	 * @param user
	 */
	public void switchChildren(int indexChild1, int indexChild2, User user) throws ChildrenNotFoundException;
}
