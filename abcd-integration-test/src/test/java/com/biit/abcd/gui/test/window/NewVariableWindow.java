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

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewVariableWindow extends AcceptCancelWindow{

	private static final String NAME_CAPTION = "Name";
	private static final String NAME_TYPE = "Type";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.elements.global.variables.VariableWindow";

	public void setName(String name){
		$(TextFieldElement.class).caption(NAME_CAPTION).first().setValue(name);
		$(TextFieldElement.class).caption(NAME_CAPTION).first().waitForVaadin();
	}
	
	public void setType(AnswerFormat format){
		$(ComboBoxElement.class).caption(NAME_TYPE).first().selectByText(format.getValue());
		$(ComboBoxElement.class).caption(NAME_TYPE).first().waitForVaadin();
	}
	
	public boolean isEnabledType(){
		return $(ComboBoxElement.class).caption(NAME_TYPE).first().isEnabled();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
	
}
