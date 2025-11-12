package com.biit.abcd.webpages.components;

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

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SelectTableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -3835385087067694004L;

	private VerticalLayout rootLayout;
	private SelectTableRuleTable selectRuleTable;

	public SelectTableWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	private Component generateComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		
		selectRuleTable = new SelectTableRuleTable();
		selectRuleTable.setSizeFull();
		
		for(TableRule tableRule: UserSessionHandler.getFormController().getForm().getTableRules()){
			selectRuleTable.addRow(tableRule);
		}

		selectRuleTable.sort();
		rootLayout.addComponent(selectRuleTable);

		return rootLayout;
	}
	
	public TableRule getSelectedTableRule(){
		return selectRuleTable.getSelectedTableRule();
	}
}
