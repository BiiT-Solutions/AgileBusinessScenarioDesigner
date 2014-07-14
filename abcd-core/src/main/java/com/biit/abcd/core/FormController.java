package com.biit.abcd.core;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.DependencyExistException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.liferay.portal.model.User;

public class FormController {
	private User user;
	private Form form;
	private TreeObject lastAccessTreeObject;
	private Diagram lastAccessDiagram;
	private ExpressionChain lastAccessExpression;
	private TableRule lastAccessTable;

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

	public void remove(TreeObject treeObject) throws DependencyExistException {
		if ((getForm() != null) && (treeObject != null) && (treeObject.getParent() != null)) {
			treeObject.remove();
		}
	}

	public void moveUp(TreeObject treeObject) throws ChildrenNotFoundException {
		if (getForm() != null) {
			if ((treeObject.getParent() != null) && (treeObject.getParent().getChildren().indexOf(treeObject) > 0)) {
				treeObject.getParent().switchChildren(treeObject.getParent().getChildren().indexOf(treeObject),
						treeObject.getParent().getChildren().indexOf(treeObject) - 1, user);
			}
		}
	}

	public void moveDown(TreeObject object) throws ChildrenNotFoundException {
		if (getForm() != null) {
			if ((object.getParent() != null)
					&& (object.getParent().getChildren().indexOf(object) < (object.getParent().getChildren().size() - 1))) {
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

	public TreeObject getLastAccessTreeObject() {
		return lastAccessTreeObject;
	}

	public void setLastAccessTreeObject(TreeObject lastAccessTreeObject) {
		this.lastAccessTreeObject = lastAccessTreeObject;
	}

	public Diagram getLastAccessDiagram() {
		return lastAccessDiagram;
	}

	public void setLastAccessDiagram(Diagram lastAccessDiagram) {
		this.lastAccessDiagram = lastAccessDiagram;
	}

	public ExpressionChain getLastAccessExpression() {
		return lastAccessExpression;
	}

	public void setLastAccessExpression(ExpressionChain lastAccessExpression) {
		this.lastAccessExpression = lastAccessExpression;
	}

	public TableRule getLastAccessTable() {
		return lastAccessTable;
	}

	public void setLastAccessTable(TableRule lastAccessTable) {
		this.lastAccessTable = lastAccessTable;
	}

	/**
	 * Gets rules with treeObject as the common element for all the references
	 * in the rule.
	 * 
	 * @param treeObject
	 * @return
	 */
	public List<Rule> getRulesAssignedToTreeObject(TreeObject treeObject) {
		List<Rule> assignedRules = new ArrayList<>();

		List<Rule> rules = getForm().getRules();
		for (Rule rule : rules) {
			if (rule.isAssignedTo(treeObject)) {
				assignedRules.add(rule);
			}
		}
		return assignedRules;
	}
}