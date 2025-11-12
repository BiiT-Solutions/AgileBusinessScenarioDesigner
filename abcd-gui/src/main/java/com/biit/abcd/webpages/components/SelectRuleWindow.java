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
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SelectRuleWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -493933078596849550L;

	private SelectDroolsRule formRuleTable;

	public SelectRuleWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	public Component generateComponent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);

		VerticalLayout firstComponent = new VerticalLayout();
		firstComponent.setSizeFull();
		firstComponent.setImmediate(true);

		rootLayout.addComponent(firstComponent);

		initializeFormQuestionTable();
		firstComponent.addComponent(formRuleTable);

		// Initialize value of formQuestionTable.
		formRuleTable.setValue(UserSessionHandler.getFormController().getForm().getRules());
		formRuleTable.sort();

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formRuleTable = new SelectDroolsRule();
		formRuleTable.setCaption("Rules");
		formRuleTable.setSizeFull();
		formRuleTable.setSelectable(true);
		for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
			formRuleTable.addRow(rule);
		}
		formRuleTable.sort();
	}

	public Rule getValue() {
		return formRuleTable.getSelectedRule();
	}

}
