package com.biit.abcd.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.DependencyExistException;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.liferay.portal.model.User;

public class FormController {
	private User user;
	private Form form;
	private TreeObject lastAccessTreeObject;
	private Diagram lastAccessDiagram;
	private ExpressionChain lastAccessExpression;
	private TableRule lastAccessTable;
	private List<TableRuleRow> copiedRows;
	private Rule lastAccessRule;
	private boolean saveAllowed = true;

	private IFormDao formDao;

	public FormController(User user, SpringContextHelper helper) {
		setUser(user);
		formDao = (IFormDao) helper.getBean("formDao");
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void save() throws DuplicatedVariableException {
		checkDuplicatedVariables();
		if (saveAllowed && (getForm() != null)) {
			formDao.makePersistent(getForm());
		}
	}

	public void checkDuplicatedVariables() throws DuplicatedVariableException{
		List<CustomVariable> customVariablesList = getForm().getCustomVariables();
		for(int i=0; i<(customVariablesList.size()-1); i++){
			CustomVariable cv = customVariablesList.get(i);
			for(int j=i+1; j<(getForm().getCustomVariables().size()); j++){
				if(cv.duplicatedCustomVariable(getForm().getCustomVariables().get(j))){
					saveAllowed = false;
					throw new DuplicatedVariableException("Duplicated variable in form variables.");
				}
			}
		}
		saveAllowed = true;
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

	public Rule getLastAccessRule() {
		return lastAccessRule;
	}

	public void setLastAccessRule(Rule lastAccessRule) {
		this.lastAccessRule = lastAccessRule;
	}

	public void copyTableRuleRows(final TableRule origin, Collection<TableRuleRow> rowsToCopy) {
		List<TableRuleRow> listOfRowsToCopy = new ArrayList<TableRuleRow>(rowsToCopy);
		Collections.sort(listOfRowsToCopy, new Comparator<TableRuleRow>() {
			@Override
			public int compare(TableRuleRow arg0, TableRuleRow arg1) {
				Integer rule0 = origin.getRules().indexOf(arg0);
				Integer rule1 = origin.getRules().indexOf(arg1);
				return rule0.compareTo(rule1);
			}
		});
		copiedRows = new ArrayList<TableRuleRow>();
		for (TableRuleRow rowToCopy : listOfRowsToCopy) {
			copiedRows.add(rowToCopy.generateCopy());
		}
	}

	public void pasteTableRuleRowsAsNew(TableRule selectedTableRule) {
		if ((copiedRows == null) || copiedRows.isEmpty()) {
			return;
		}
		List<TableRuleRow> rowsToPaste = getNewInstanceOfCopiedElements();
		for (TableRuleRow rowToPaste : rowsToPaste) {
			selectedTableRule.addRow(rowToPaste);
		}
	}

	private List<TableRuleRow> getNewInstanceOfCopiedElements() {
		List<TableRuleRow> newCopiedRows = new ArrayList<TableRuleRow>();
		for (TableRuleRow row : copiedRows) {
			newCopiedRows.add(row.generateCopy());
		}
		return newCopiedRows;
	}
}