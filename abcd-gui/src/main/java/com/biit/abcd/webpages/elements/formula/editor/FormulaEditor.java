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

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class FormulaEditor extends CustomComponent {
	private static final long serialVersionUID = 6180990439200708260L;
	private static final String CLASSNAME_FORMULA_LAYOUT = "v-formula-layout";

	private VerticalLayout rootLayout;
	private VerticalLayout formulaLayout;
	private FormulaPortComponent clickedFormulaPort;

	public FormulaEditor() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		formulaLayout = new VerticalLayout();
		formulaLayout.setSizeFull();
		formulaLayout.setMargin(true);
		formulaLayout.setStyleName(CLASSNAME_FORMULA_LAYOUT);

		addFormulaExpression(new ExpresionRoot());

		rootLayout.addComponent(formulaLayout);

		setCompositionRoot(rootLayout);
		setSizeFull();
		setImmediate(true);
	}

	public void addFormulaPort(FormulaPortComponent formulaPort) {
		formulaPort.addFormulaPortClickListener(new FormulaPortClickListener() {
			@Override
			public void formulaPortClicked(FormulaPortComponent formulaPort, LayoutClickEvent listener) {
				setFormulaPortClicked(formulaPort);
			}
		});
		formulaLayout.addComponent(formulaPort);
	}

	public void addFormulaExpression(FormulaExpressionComponent formulaExpressionComponent) {
		formulaExpressionComponent.addFormulaPortClickListener(new FormulaPortClickListener() {
			@Override
			public void formulaPortClicked(FormulaPortComponent formulaPort, LayoutClickEvent listener) {
				setFormulaPortClicked(formulaPort);
			}
		});
		formulaLayout.addComponent(formulaExpressionComponent);
	}

	private void setFormulaPortClicked(FormulaPortComponent formulaPort) {
		// Removed selected state from old clicked port, and add state to new
		// port.
		if (clickedFormulaPort != null) {
			clickedFormulaPort.setSelected(false);
		}
		clickedFormulaPort = formulaPort;

		if (clickedFormulaPort != null) {
			clickedFormulaPort.setSelected(true);
		}
	}

}
