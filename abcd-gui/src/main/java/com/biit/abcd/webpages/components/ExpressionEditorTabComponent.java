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

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * Component for editing an expression. Is composed by a viewer and a properties
 * menu in tabs.
 */
public abstract class ExpressionEditorTabComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private TabSheet tabMenu;

	public ExpressionEditorTabComponent() {
		initComponents(false);
	}

	public ExpressionEditorTabComponent(boolean questionEditor) {
		initComponents(questionEditor);
	}

	public void initComponents(boolean questionEditor) {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		tabMenu = new TabSheet();
		tabMenu.setHeight("100%");

		setCompositionRoot(rootLayout);
	}

	public void setTab(Component component, String description, Resource icon){
		Tab tab = tabMenu.addTab(component);
		tab.setDescription("");
		tab.setIcon(icon);
	}

	public TabSheet getTabSheet(){
		return tabMenu;
	}

	public HorizontalLayout getRootLayout(){
		return rootLayout;
	}
}
