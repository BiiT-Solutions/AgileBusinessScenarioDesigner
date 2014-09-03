package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.ElementUpdatedListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Basic code and listeners for the layouts inside the tab menu of the expression viewer.
 */
public abstract class TabLayout extends VerticalLayout {
	private static final long serialVersionUID = -7278902256434884118L;
	protected static final String BUTTON_WIDTH = "100%";
	protected static final String FORM_BUTTON_WIDTH = "160px";

	private List<ElementAddedListener> elementAddedListener;
	private List<ElementUpdatedListener> elementUpdatedListener;

	protected TabLayout() {
		elementAddedListener = new ArrayList<ElementAddedListener>();
		elementUpdatedListener = new ArrayList<ElementUpdatedListener>();
		setHeight(null);
		setWidth("100%");
		setMargin(true);
	}

	protected void addExpression(Expression expression) {
		fireElementAddedListener(expression);
	}

	protected void updateExpression(Expression expression) {
		fireElementUpdatedListener(expression);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		elementAddedListener.add(listener);
	}

	public void addUpdateElementListener(ElementUpdatedListener listener) {
		elementUpdatedListener.add(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		elementAddedListener.remove(listener);
	}

	public void removeUpdateElementListener(ElementUpdatedListener listener) {
		elementUpdatedListener.remove(listener);
	}

	protected void fireElementAddedListener(Object element) {
		for (ElementAddedListener listener : elementAddedListener) {
			listener.elementAdded(element);
		}
	}

	protected void fireElementUpdatedListener(Object element) {
		for (ElementUpdatedListener listener : elementUpdatedListener) {
			listener.elementUpdated(element);
		}
	}

}
