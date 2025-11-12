package com.biit.abcd.gui.test.webpage;

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

import com.biit.abcd.gui.test.window.NewRuleWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class RuleEditor extends LeftTreeTableWebpage{

	private static final String SAVE_BUTTON = "Save";
	private static final String ID = "rule-table";
	
	private NewRuleWindow newRuleWindow;
	
	public RuleEditor() {
		super();
		newRuleWindow = new NewRuleWindow();
		addWindow(newRuleWindow);
	}

	@Override
	public TableElement getTable() {
		TableElement query = $(TableElement.class).id(getTableId());
		return query;
	}

	@Override
	protected String getTableId() {
		return ID;
	}

	public void newRule(String name) {
		getNewButton().click();
		newRuleWindow.setNameAndAccept(name);
	}

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}
}
