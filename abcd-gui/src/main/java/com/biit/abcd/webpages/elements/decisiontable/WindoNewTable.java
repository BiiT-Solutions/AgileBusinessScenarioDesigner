package com.biit.abcd.webpages.elements.decisiontable;

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

import java.util.Objects;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.TableRuleEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindoNewTable extends WindowCreateNewObject {
	private static final long serialVersionUID = -466962195753116776L;

	public WindoNewTable(TableRuleEditor parentWindow, LanguageCodes windowCaption, LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void concreteAcceptAction(TextField inputTextField) {
		for (TableRule existingTableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
			if (Objects.equals(existingTableRule.getName().trim(), inputTextField.getValue().trim())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_TABLE_RULE_NAME);
				return;
			}
		}
		TableRule tableRule = new TableRule();
		tableRule.setName(inputTextField.getValue().trim());
		tableRule.setCreatedBy(UserSessionHandler.getUser());
		tableRule.setUpdatedBy(UserSessionHandler.getUser());
		tableRule.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getTableRules().add(tableRule);
		((TableRuleEditor) getParentWindow()).addTablefromWindow(tableRule);
		((TableRuleEditor) getParentWindow()).sortTableMenu();

		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress() + "' has created a " + tableRule.getClass()
				+ " with 'Name: " + tableRule.getName() + "'.");

		close();
	}

}
