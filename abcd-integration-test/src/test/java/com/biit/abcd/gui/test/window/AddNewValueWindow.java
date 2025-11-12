package com.biit.abcd.gui.test.window;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
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

import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AddNewValueWindow extends AcceptCancelWindow {

	private static final String VALUE_CAPTION = "Value";
	private static final String VALID_FROM = "Valid from";
	private static final String VALID_TO = "Valid to";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.elements.global.variables.VariableDataWindow";

	public void setValue(String value) {
		try {
			$(TextFieldElement.class).caption(VALUE_CAPTION).first().setValue(value);
			$(TextFieldElement.class).caption(VALUE_CAPTION).first().waitForVaadin();
		} catch (Exception e) {
			$(DateFieldElement.class).caption(VALUE_CAPTION).first().setValue(value);
			$(DateFieldElement.class).caption(VALUE_CAPTION).first().waitForVaadin();
		}
	}

	public void setValidFrom(String value) {
		$(DateFieldElement.class).caption(VALID_FROM).first().setValue(value);
	}

	public void setValidTo(String value) {
		$(DateFieldElement.class).caption(VALID_TO).first().setValue(value);
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
