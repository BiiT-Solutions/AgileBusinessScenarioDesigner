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

import java.nio.charset.StandardCharsets;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.components.SaveAction;
import com.biit.abcd.webpages.components.SettingsWindow;

/**
 * Transforms a table rule into a csv file and allows the user to download it
 *
 */
public class SaveTableToCsvAction implements SaveAction {

	@Override
	public String getMimeType() {
		return "text/csv";
	}

	@Override
	public String getExtension() {
		return "csv";
	}

	@Override
	public byte[] getInformationData() {
		try {
			String tableRuleString = "";
			TableRule tableRule = UserSessionHandler.getFormController().getLastAccessTable();
			if (tableRule != null) {
				for (TableRuleRow row : tableRule.getRules()) {
					ExpressionChain conditionChain = row.getConditions();
					ExpressionChain actionChain = row.getActions();
					for (Expression expression : conditionChain.getExpressions()) {
						tableRuleString += expression.getRepresentation(false) + ";";
					}
					tableRuleString += actionChain.getRepresentation(false) + ";";
					tableRuleString += "\n";
				}
				return tableRuleString.getBytes(StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		}
		return null;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getFileName() {
		return "test." + getExtension();
	}

}
