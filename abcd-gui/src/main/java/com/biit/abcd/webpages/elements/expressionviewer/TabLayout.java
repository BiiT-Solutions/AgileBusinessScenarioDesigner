package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Basic code and listeners for the layouts inside the tab menu of the expression viewer.
 */
public abstract class TabLayout extends VerticalLayout {
	private static final long serialVersionUID = -7278902256434884118L;
	protected static final String BUTTON_WIDTH = "100%";
	protected static final String FORM_BUTTON_WIDTH = "160px";

	private List<ElementAddedListener> elementAddedListener;

	protected TabLayout() {
		elementAddedListener = new ArrayList<ElementAddedListener>();
		setHeight(null);
		setWidth("100%");
		setMargin(true);
	}

	protected void addExpression(Expression expression) {
		// formExpression.addExpression(expression);
		// firePropertyUpdateListener(expression);
		fireElementAddedListener(expression);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		elementAddedListener.add(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		elementAddedListener.remove(listener);
	}

	protected void fireElementAddedListener(Object element) {
		for (ElementAddedListener listener : elementAddedListener) {
			listener.elementAdded(element);
		}
	}

}
