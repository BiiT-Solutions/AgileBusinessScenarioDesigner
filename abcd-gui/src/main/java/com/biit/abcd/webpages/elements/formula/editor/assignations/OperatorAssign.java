package com.biit.abcd.webpages.elements.formula.editor.assignations;

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

import com.biit.abcd.webpages.elements.formula.editor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formula.editor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formula.editor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formula.editor.Type;
import com.vaadin.ui.Label;

public class OperatorAssign extends FormulaExpressionComponent {

	private static final long serialVersionUID = 550860447581821953L;

	private FormulaPortComponent leftPort;
	protected Label assignText;
	private FormulaPortComponent rightPort;

	public OperatorAssign() {
		super();
		leftPort = new FormulaPortComponent(Type.VARIABLE);
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.CALCULATION);
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addPort(leftPort);
		assignText = addText("=");
		addPort(rightPort);
	}

	@Override
	public Type getReturnType() {
		return Type.CALCULATION;
	}

}
