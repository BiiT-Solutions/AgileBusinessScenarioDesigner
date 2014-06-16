package com.biit.abcd.webpages.elements.expressiontree;

import java.util.List;

import com.biit.abcd.webpages.elements.expressiontree.expression.ExprBasic;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprFunction;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprPort;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprWChild;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;

public class ExpressionTreeTable extends TreeTable {
	private static final long serialVersionUID = 1654696802740736098L;

	public enum Properties {
		FORMULA_TEXT
	};

	public ExpressionTreeTable() {
		super();

		addContainerProperty(Properties.FORMULA_TEXT, String.class, "", "text", null, Align.LEFT);
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
		addExpression(expression, true, false);
	}

	@SuppressWarnings("unchecked")
	public void addExpression(ExprBasic expression, boolean alsoChilds, boolean alsoParents) {
		Item item = addItem(expression);
		// Could not create the item, then its an update.
		if (item == null) {
			item = getItem(expression);
			// If we are updating the childs, then we need to delete them to get
			// a correct order
			if (alsoChilds) {
				removeExpressionChilds(expression);
			}
		} else {
			setChildrenAllowed(expression, false);
		}
		if (expression.getParent() != null) {
			setChildrenAllowed(expression.getParent(), true);
			setParent(expression, expression.getParent());
		}
		item.getItemProperty(Properties.FORMULA_TEXT).setValue(expression.getExpressionTableString());

		if (alsoChilds) {
			addExpressionChilds(expression);
		}

		if (alsoParents) {
			addExpressionParents(expression);
		}
	}

	/**
	 * Remove the child expressions of a expression element. This is used to fix
	 * the order of elements in table.
	 * 
	 * @param expression
	 */
	private void removeExpressionChilds(ExprBasic expression) {
		// If it is a function, then add the ports
		if (expression instanceof ExprFunction) {
			ExprFunction expressionFunction = (ExprFunction) expression;
			for (ExprPort port : expressionFunction.getPorts()) {
				removeItem(port);
				removeExpressionChilds(port);
			}
		}
		// If it is a expression with childs
		if (expression instanceof ExprWChild) {
			ExprWChild expressionWChilds = (ExprWChild) expression;
			for (ExprBasic childExpression : expressionWChilds.getChilds()) {
				removeItem(childExpression);
				removeExpressionChilds(childExpression);
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
		if (expression instanceof ExprWChild) {
			ExprWChild expressionWChilds = (ExprWChild) expression;
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
		for(ExprBasic expression: expressions){
			removeItem(expression);
		}
	}
}
