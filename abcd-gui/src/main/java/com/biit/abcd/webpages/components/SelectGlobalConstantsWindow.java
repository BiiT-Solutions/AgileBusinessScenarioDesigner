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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SelectGlobalConstantsWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout selectionComponent;

	private GlobalConstantsListSelect constantSelection;

	public SelectGlobalConstantsWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(true);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	public Component generateComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_GLOBAL_CONSTANT_WINDOW_CAPTION));

		selectionComponent = new VerticalLayout();
		selectionComponent.setSizeFull();
		selectionComponent.setImmediate(true);

		rootLayout.addComponent(selectionComponent);

		initializeVariableSelection();
		selectionComponent.addComponent(constantSelection);

		return rootLayout;
	}

	private void initializeVariableSelection() {
		constantSelection = new GlobalConstantsListSelect();
	}

	public GlobalVariable getValue() {
		if (constantSelection.getValue() == null) {
			return null;
		}
		return (GlobalVariable) constantSelection.getValue();
	}

	public void setValue(GlobalVariable globalVariable) {
		constantSelection.setValue(globalVariable);
	}

}
