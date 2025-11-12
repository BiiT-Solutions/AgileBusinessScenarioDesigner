package com.biit.abcd.webpages.elements.expression.viewer;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
		elementAddedListener = new ArrayList<>();
		elementUpdatedListener = new ArrayList<>();
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
