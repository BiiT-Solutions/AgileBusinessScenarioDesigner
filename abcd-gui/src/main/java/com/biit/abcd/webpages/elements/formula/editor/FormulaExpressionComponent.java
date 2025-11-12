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
import java.util.Iterator;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public abstract class FormulaExpressionComponent extends CustomComponent {
	private static final long serialVersionUID = 1350097467868000964L;
	private static final String CLASSNAME = "v-formulaExpressionComponent";
	private static final String CLASSNAME_ROOT_LAYOUT = "v-formulaExpressionComponentRoot-layout";
	private static final String CLASSNAME_LAYOUT = "v-formulaExpressionComponent-layout";
	private static final String CLASSNAME_SECOND_LINE = "v-second-line";

	private CssLayout rootLayout;
	private CssLayout fileLayout;

	public FormulaExpressionComponent() {
		super();

		rootLayout = new CssLayout();
		rootLayout.setStyleName(CLASSNAME_ROOT_LAYOUT);
		rootLayout.setSizeUndefined();

		addLine();

		setImmediate(true);
		setCompositionRoot(rootLayout);
		setStyleName(CLASSNAME);
	}

	public void addLine() {
		fileLayout = new CssLayout();
		fileLayout.addStyleName(CLASSNAME_LAYOUT);
		if (rootLayout.getComponentCount() != 0) {
			fileLayout.addStyleName(CLASSNAME_LAYOUT + " " + CLASSNAME_SECOND_LINE);
		}
		fileLayout.setWidth(null);
		fileLayout.setHeight(null);

		rootLayout.addComponent(fileLayout);
	}

	public void setValueToPort(FormulaPortComponent port, FormulaExpressionComponent expresion) {
		port.setValue(expresion);
	}

	protected Label addText(String text) {
		Label textLabel = new Label(text);
		textLabel.setImmediate(true);
		textLabel.setWidth(null);
		fileLayout.addComponent(textLabel);
		return textLabel;
	}

	protected void addPort(FormulaPortComponent port) {
		fileLayout.addComponent(port);
	}

	protected List<CssLayout> getLines() {
		List<CssLayout> lines = new ArrayList<CssLayout>();
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			lines.add((CssLayout) itr.next());
		}
		return lines;
	}

	protected List<FormulaPortComponent> getPorts() {
		List<FormulaPortComponent> ports = new ArrayList<FormulaPortComponent>();
		List<CssLayout> lines = getLines();
		for (CssLayout line : lines) {
			Iterator<Component> itr = line.iterator();
			while (itr.hasNext()) {
				Component component = itr.next();
				if (component instanceof FormulaPortComponent) {
					ports.add((FormulaPortComponent) component);
				}
			}
		}
		return ports;
	}

	public void addFormulaPortClickListener(FormulaPortClickListener listener) {
		List<FormulaPortComponent> ports = getPorts();
		for (FormulaPortComponent formulaPortComponent : ports) {
			formulaPortComponent.addFormulaPortClickListener(listener);
		}
	}

	public boolean isChildComponent(Component clickedComponent) {
		Iterator<Component> layoutItr = rootLayout.iterator();
		while (layoutItr.hasNext()) {
			CssLayout layout = (CssLayout) layoutItr.next();
			if (layout.getComponentIndex(clickedComponent) != -1) {
				return true;
			}
		}
		return false;
	}

	public abstract Type getReturnType();
}
