package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprAtomic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public abstract class ExprAtomicProperties<T extends ExprAtomic> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -5417522321259478890L;
	protected static final String buttonWidth = "180px";

	public ExprAtomicProperties(Class<? extends T> type) {
		super(type);
	}

	protected FormLayout getCommonFormLayout(final ExprAtomic exprWoChild) {

		Button addParenthesis = new Button("Add Parenthesis", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				exprWoChild.addParenthesis();
				// Add parenthesis is done by adding a parent element
				// parenthesis, so we need to update the parent as it still
				// doesn't exist on the tree table.
				firePropertyUpdateListener(exprWoChild.getParent().getParent());
			}
		});
		addParenthesis.setWidth(buttonWidth);

		Button removeParenthesis = new Button("Remove Parenthesis", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				exprWoChild.removeParenthesis();
				// Remove parenthesis is done by removing a parent element
				// parenthesis and flattening the elements into the upper level.
				// At this point the parenthesis no longer exists, so we have to
				// update the parent of this element.
				firePropertyUpdateListener(exprWoChild.getParent());
			}
		});
		removeParenthesis.setEnabled(exprWoChild.isParenthised());
		removeParenthesis.setWidth(buttonWidth);

		Button addExpression = new Button("Add Expression", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				exprWoChild.addExpression();
				firePropertyUpdateListener(exprWoChild.getParent());
			}
		});
		addExpression.setWidth(buttonWidth);
		
		Button deleteExpression = new Button("Delete", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				exprWoChild.delete();
				firePropertyUpdateListener(exprWoChild.getParent());
			}
		});
		deleteExpression.setWidth(buttonWidth);

		FormLayout exprJointForm = new FormLayout();
		exprJointForm.setWidth(null);
		exprJointForm.addComponent(addParenthesis);
		exprJointForm.addComponent(removeParenthesis);
		exprJointForm.addComponent(addExpression);
		exprJointForm.addComponent(deleteExpression);

		return exprJointForm;

	}

}
