package com.biit.abcd.webpages.elements.expressiontree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExprFunction;
import com.biit.abcd.persistence.entity.expressions.ExprGroup;
import com.biit.abcd.persistence.entity.expressions.ExprPort;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TreeTable;

public class ExpressionTreeTable extends TreeTable {
	private static final long serialVersionUID = 1654696802740736098L;

	public enum Properties {
		FORMULA_TEXT
	};

	public ExpressionTreeTable() {
		super();

		addContainerProperty(Properties.FORMULA_TEXT, Label.class, "", "text", null, Align.LEFT);
		setSelectable(true);
		setImmediate(true);
	}

	// public void addExpression(ChildBrotherExpression expression){
	// addExpression(expression,null);
	// }

	// @SuppressWarnings("unchecked")
	// public void addExpression(ChildBrotherExpression expression,
	// ChildBrotherExpression parentExpression){
	// Item item = addItem(expression);
	// if(parentExpression!=null){
	// setParent(expression, parentExpression);
	// }
	// item.getItemProperty(Properties.FORMULA_TEXT).setValue(expression.getExpressionTableString());
	// for(ChildExpression childExpression: expression.getChilds()){
	// addExpression(childExpression,expression);
	// }
	// }

	public void addExpression(ExprBasic expression) {
		removeAll();
		addExpression(expression, true, true);
	}

	@SuppressWarnings("unchecked")
	public void addExpression(ExprBasic expression, boolean alsoChilds, boolean alsoParents) {
		if (expression != null) {
			Item item = addItem(expression);
			// Could not create the item, then its an update.
			if (item == null) {
				item = getItem(expression);
				// If we are updating the children we need to delete them to handle
				// the reorder, and delete problems.
				if (alsoChilds) {
					removeAllChildrenOf(expression);
				}
			} else {
				setChildrenAllowed(expression, false);
			}
			if (expression.getParent() != null) {
				setChildrenAllowed(expression.getParent(), true);
				setParent(expression, expression.getParent());
			}
			item.getItemProperty(Properties.FORMULA_TEXT).setValue(
					new Label(expression.getExpressionTableString(), ContentMode.HTML));

			if (alsoChilds) {
				addExpressionChilds(expression);
			}

			if (alsoParents) {
				addExpressionParents(expression);
			}
		}
	}

	/**
	 * Remove the child expressions of a expression element. This is used to fix the order of elements in table.
	 * 
	 * @param expression
	 */
	private void removeAllChildrenOf(Object itemId) {
		if (hasChildren(itemId)) {
			Set<Object> children = new HashSet<Object>(getChildren(itemId));
			for (Object child : children) {
				removeAllChildrenOf(child);
				removeItem(child);
			}
		}
	}

	private void addExpressionChilds(ExprBasic expression) {
		// If it is a function, then add the ports
		if (expression instanceof ExprFunction) {
			ExprFunction expressionFunction = (ExprFunction) expression;
			for (ExprPort port : expressionFunction.getPorts()) {
				addExpression(port);
			}
		}
		// If it is a expression with childs
		if (expression instanceof ExprGroup) {
			ExprGroup expressionWChilds = (ExprGroup) expression;
			for (ExprBasic childExpression : expressionWChilds.getChilds()) {
				addExpression(childExpression);
			}
		}
	}

	private void addExpressionParents(ExprBasic expression) {
		if (expression.getParent() != null) {
			addExpression(expression.getParent(), false, true);
		}
	}

	public void removeExpressions(List<ExprBasic> expressions) {
		for (ExprBasic expression : expressions) {
			removeItem(expression);
		}
	}

	public void setTitle(String value) {
		setColumnHeader(Properties.FORMULA_TEXT, value);
	}

	public void removeAll() {
		removeAllItems();
	}
}
