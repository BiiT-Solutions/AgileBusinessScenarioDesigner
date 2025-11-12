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

public class NewRuleTableWindow extends TextFieldAcceptCancelWindow{

	private static final String RULE_TABLE_NAME = "Table Name";
	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.WindoNewTable";
	private static final String CREATE_BUTTON_CAPTION = "Accept";
		
	public String getAcceptCaption(){
		return CREATE_BUTTON_CAPTION;
	}

	@Override
	public String getTextFieldCaption() {
		return RULE_TABLE_NAME;
	}

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}

}
