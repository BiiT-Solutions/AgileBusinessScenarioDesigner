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

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.LabelElement;

public class WarningUnsavedData extends AcceptCancelWindow {

	private static final Object WARNING_LABEL = "Warning! Any unsaved data will be lost.";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.components.AlertMessageWindow";

	public boolean isVisible() {
		if(getWindow()==null){
			return false;
		}
		
		ElementQuery<LabelElement> label = getWindow().$(LabelElement.class);
		if (label.exists()) {
			LabelElement labelElement = label.first();
			if (labelElement.getText().equals(WARNING_LABEL)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
