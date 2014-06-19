package com.biit.abcd.core;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.liferay.portal.model.User;

public class FormController {
	private User user;
	private Form form;

	private IFormDao formDao;

	public FormController(User user, SpringContextHelper helper) {
		setUser(user);
		formDao = (IFormDao) helper.getBean("formDao");
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void save() {
		if (getForm() != null) {
			formDao.makePersistent(getForm());
		}
	}

	public void remove() {
		if (getForm() != null) {
			formDao.makeTransient(getForm());
		}
	}

	/**
	 * Reloads the selected form from database.
	 */
	public void reload() {
		if (getForm() != null) {
			setForm(formDao.read(getForm().getId()));
		}
	}

	public void remove(TreeObject treeObject) {
		if (getForm() != null && treeObject != null && treeObject.getParent() != null) {
			treeObject.remove();
		}
	}

	public void moveUp(TreeObject treeObject) throws ChildrenNotFoundException {
		if (getForm() != null) {
			if (treeObject.getParent() != null && treeObject.getParent().getChildren().indexOf(treeObject) > 0) {
				treeObject.getParent().switchChildren(treeObject.getParent().getChildren().indexOf(treeObject),
						treeObject.getParent().getChildren().indexOf(treeObject) - 1, user);
			}
		}
	}

	public void moveDown(TreeObject object) throws ChildrenNotFoundException {
		if (getForm() != null) {
			if (object.getParent() != null
					&& object.getParent().getChildren().indexOf(object) < object.getParent().getChildren().size() - 1) {
				object.getParent().switchChildren(object.getParent().getChildren().indexOf(object),
						object.getParent().getChildren().indexOf(object) + 1, user);
			}
		}
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public User getUser() {
		return user;
	}
}
