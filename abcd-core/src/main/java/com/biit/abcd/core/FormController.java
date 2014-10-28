package com.biit.abcd.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.core.utils.TableRuleUtils;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.FormComparator;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.liferay.portal.model.User;

public class FormController {
	private User user;
	private Form form;
	private TreeObject lastAccessTreeObject;
	private Diagram lastAccessDiagram;
	private ExpressionChain lastAccessExpression;
	private TableRule lastAccessTable;
	private Rule lastAccessRule;
	private List<TableRuleRow> copiedRows;
	private Form originalForm;

	private IFormDao formDao;

	public FormController(User user, SpringContextHelper helper) {
		this.setUser(user);
		this.formDao = (IFormDao) helper.getBean("formDao");
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void save() throws DuplicatedVariableException {
		this.checkDuplicatedVariables();
		if (this.getForm() != null) {
			this.formDao.makePersistent(this.getForm());
		}
	}

	public void checkDuplicatedVariables() throws DuplicatedVariableException {
		Set<CustomVariable> customVariablesList = this.getForm().getCustomVariables();
		Iterator<CustomVariable> startComparator = customVariablesList.iterator();
		while (startComparator.hasNext()) {
			CustomVariable comparedVariable = startComparator.next();
			Iterator<CustomVariable> comparedWithIterator = startComparator;
			while (comparedWithIterator.hasNext()) {
				CustomVariable comparedWithVariable = comparedWithIterator.next();
				if (comparedVariable.hasSameNameAndScope(comparedWithVariable)) {
					throw new DuplicatedVariableException("Duplicated variable in form variables.");
				}
			}
		}
	}

	public void remove() {
		if (this.getForm() != null) {
			this.formDao.makeTransient(this.getForm());
		}
	}

	public void remove(TreeObject treeObject) throws DependencyExistException {
		if ((this.getForm() != null) && (treeObject != null) && (treeObject.getParent() != null)) {
			treeObject.remove();
		}
	}

	public void moveUp(TreeObject treeObject) throws ChildrenNotFoundException {
		if (this.getForm() != null) {
			if ((treeObject.getParent() != null) && (treeObject.getParent().getChildren().indexOf(treeObject) > 0)) {
				treeObject.getParent().switchChildren(treeObject.getParent().getChildren().indexOf(treeObject),
						treeObject.getParent().getChildren().indexOf(treeObject) - 1, this.user);
			}
		}
	}

	public void moveDown(TreeObject object) throws ChildrenNotFoundException {
		if (this.getForm() != null) {
			if ((object.getParent() != null)
					&& (object.getParent().getChildren().indexOf(object) < (object.getParent().getChildren().size() - 1))) {
				object.getParent().switchChildren(object.getParent().getChildren().indexOf(object),
						object.getParent().getChildren().indexOf(object) + 1, this.user);
			}
		}
	}

	public Form getForm() {
		return this.form;
	}

	public void setForm(Form form) {
		this.form = form;
		try {
			this.originalForm = (Form) form.generateCopy(true, true);
		} catch (NotValidStorableObjectException | CharacterNotAllowedException e) {
		}
		this.clearWorkVariables();
	}

	public void checkUnsavedChanges() throws TreeObjectNotEqualsException, StorableObjectNotEqualsException,
			FormNotEqualsException, GroupNotEqualsException, QuestionNotEqualsException,
			CustomVariableNotEqualsException, ExpressionNotEqualsException, TableRuleNotEqualsException,
			RuleNotEqualsException, DiagramNotEqualsException, DiagramObjectNotEqualsException, NodeNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException {
		new FormComparator().compare(form, originalForm);
	}

	public User getUser() {
		return this.user;
	}

	public TreeObject getLastAccessTreeObject() {
		return this.lastAccessTreeObject;
	}

	public void setLastAccessTreeObject(TreeObject lastAccessTreeObject) {
		this.lastAccessTreeObject = lastAccessTreeObject;
	}

	public Diagram getLastAccessDiagram() {
		return this.lastAccessDiagram;
	}

	public void setLastAccessDiagram(Diagram lastAccessDiagram) {
		this.lastAccessDiagram = lastAccessDiagram;
	}

	public ExpressionChain getLastAccessExpression() {
		return this.lastAccessExpression;
	}

	public void setLastAccessExpression(ExpressionChain lastAccessExpression) {
		this.lastAccessExpression = lastAccessExpression;
	}

	public TableRule getLastAccessTable() {
		return this.lastAccessTable;
	}

	public void setLastAccessTable(TableRule lastAccessTable) {
		this.lastAccessTable = lastAccessTable;
	}

	public Rule getLastAccessRule() {
		return this.lastAccessRule;
	}

	public void setLastAccessRule(Rule lastAccessRule) {
		this.lastAccessRule = lastAccessRule;
	}

	/**
	 * Gets rules with treeObject as the common element for all the references in the rule.
	 * 
	 * @param treeObject
	 * @return
	 */
	public Set<Rule> getRulesAssignedToTreeObject(TreeObject treeObject) {
		Set<Rule> assignedRules = new HashSet<>();

		Set<Rule> rules = this.getForm().getRules();
		for (Rule rule : rules) {
			if (rule.isAssignedTo(treeObject)) {
				assignedRules.add(rule);
			}
		}
		return assignedRules;
	}

	/**
	 * Gets expressionChains that reference to a particular element.
	 * 
	 * @param element
	 * @return
	 */
	public Set<ExpressionChain> getFormExpressionChainsAssignedToTreeObject(TreeObject element) {
		Set<ExpressionChain> expressionChains = new HashSet<>();

		Set<ExpressionChain> expressions = this.getForm().getExpressionChains();
		for (ExpressionChain expression : expressions) {
			if (expression.isAssignedTo(element)) {
				expressionChains.add(expression);
			}
		}
		return expressionChains;
	}

	public void copyTableRuleRows(final TableRule origin, Collection<TableRuleRow> rowsToCopy) {
		copiedRows = TableRuleUtils.copyTableRuleRows(origin, rowsToCopy);
	}

	public void pasteTableRuleRowsAsNew(TableRule selectedTableRule) {
		TableRuleUtils.pasteTableRuleRows(selectedTableRule, copiedRows);
	}

	public int rowsToCopy() {
		if (copiedRows == null) {
			return 0;
		}
		return copiedRows.size();
	}

	private void clearWorkVariables() {
		this.lastAccessTreeObject = null;
		this.lastAccessDiagram = null;
		this.lastAccessExpression = null;
		this.lastAccessTable = null;
		this.lastAccessRule = null;
		this.copiedRows = null;
	}
}