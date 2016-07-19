package com.biit.abcd.webpages.elements.decisiontable;

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
					ExpressionChain actionChain = row.getAction();
					for (Expression expression : conditionChain.getExpressions()) {
						tableRuleString += expression.getRepresentation(false) + ";";
					}
					tableRuleString += actionChain.getRepresentation(false) + ";";
					tableRuleString += "\n";
				}
				return tableRuleString.getBytes();
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