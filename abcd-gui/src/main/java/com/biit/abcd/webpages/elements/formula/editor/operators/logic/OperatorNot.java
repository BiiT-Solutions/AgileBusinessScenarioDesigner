package com.biit.abcd.webpages.elements.formula.editor.operators.logic;

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

public class OperatorNot extends FormulaExpressionComponent {
	private static final long serialVersionUID = -6555193604954455261L;
	private FormulaPortComponent port;

	public OperatorNot() {
		super();
		port = new FormulaPortComponent(Type.getComparisonAndLogic());
		port.addFormulaPortClickListener(new CustomFormulaPortClickListener(port));

		addText("(!");
		addPort(port);
		addText(")");
	}

	@Override
	public Type getReturnType() {
		return Type.LOGIC;
	}
}
