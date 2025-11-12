package com.biit.abcd.webpages.elements.formula.editor;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

public class FormulaPortComponent extends CustomComponent {
	private static final long serialVersionUID = -7211660100593264284L;
	private static final String CLASSNAME_FORMULA_PORT = "v-formula-port";
	private static final String CLASSNAME_FORMULA_PORT_LAYOUT = "v-formula-port-layout";
	private static final String CLASSNAME_FORMULA_PORT_SELECTED = "v-selected";
	private static final String emptyWidth = "60px";
	private static final String emptyHeight = "20px";

	private FormulaExpressionComponent value;

	private CssLayout rootLayout;
	private Set<Type> acceptedTypes;

	private List<FormulaPortClickListener> listeners;

	public FormulaPortComponent(Type acceptedType) {
		Set<Type> acceptedTypes = new HashSet<Type>();
		acceptedTypes.add(acceptedType);
		initializate(acceptedTypes);
	}

	public FormulaPortComponent(Set<Type> acceptedTypes) {
		initializate(acceptedTypes);
	}

	private void initializate(Set<Type> acceptedTypes) {
		listeners = new ArrayList<FormulaPortClickListener>();
		this.acceptedTypes = new HashSet<Type>();
		this.acceptedTypes.addAll(acceptedTypes);

		rootLayout = new CssLayout();
		rootLayout.setWidth(emptyWidth);
		rootLayout.setHeight(emptyHeight);
		rootLayout.setStyleName(CLASSNAME_FORMULA_PORT_LAYOUT);
		rootLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 5673848977203821194L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.getClickedComponent() != null) {
					if (event.getClickedComponent().getParent().getParent() instanceof FormulaExpressionComponent) {
						if (getValue().isChildComponent(event.getClickedComponent())) {
							// Only fires if the element is a child of the
							// Formula Expression.
							firePortClickListeners(event);
						}
					}
				} else {
					firePortClickListeners(event);
				}
			}
		});

		setImmediate(true);
		setCompositionRoot(rootLayout);
		setSizeUndefined();
		setStyleName(CLASSNAME_FORMULA_PORT);
	}

	public void addFormulaPortClickListener(FormulaPortClickListener listener) {
		listeners.add(listener);
	}

	public void removeFormulaPortClickListener(FormulaPortClickListener listener) {
		listeners.remove(listener);
	}

	private void firePortClickListeners(LayoutClickEvent event) {
		for (FormulaPortClickListener listener : listeners) {
			listener.formulaPortClicked(this, event);
		}
	}

	public void setSelected(boolean value) {
		if (value) {
			setStyleName(CLASSNAME_FORMULA_PORT + " " + CLASSNAME_FORMULA_PORT_SELECTED);
		} else {
			setStyleName(CLASSNAME_FORMULA_PORT);
		}
	}

	public Set<Type> getAcceptedTypes() {
		return acceptedTypes;
	}

	public void setValue(FormulaExpressionComponent value) {
		this.value = value;
		if (value != null) {
			rootLayout.removeAllComponents();
			rootLayout.addComponent(value);
			rootLayout.setWidth(null);
			rootLayout.setHeight(null);
			// Add click listeners to descendants.
			for (FormulaPortClickListener listener : listeners) {
				if (listener instanceof ChangeValuePortClickListener) {
					continue;
				}
				value.addFormulaPortClickListener(listener);
			}
		} else {
			rootLayout.removeAllComponents();
			rootLayout.setWidth(emptyWidth);
			rootLayout.setHeight(emptyHeight);
		}
	}

	public FormulaExpressionComponent getValue() {
		return value;
	}
}
