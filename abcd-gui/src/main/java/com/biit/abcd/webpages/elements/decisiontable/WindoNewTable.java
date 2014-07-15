package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.webpages.DecisionTableEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindoNewTable extends WindowCreateNewObject {
	private static final long serialVersionUID = -466962195753116776L;

	public WindoNewTable(DecisionTableEditor parentWindow, LanguageCodes windowCaption, LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		for (TableRule existingTableRule : UserSessionHandler.getFormController().getForm().getTableRules()) {
			if (existingTableRule.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_TABLE_RULE_NAME);
				return;
			}
		}
		TableRule tableRule = new TableRule();
		tableRule.setName(inputTextField.getValue());
		tableRule.setCreatedBy(UserSessionHandler.getUser());
		tableRule.setUpdatedBy(UserSessionHandler.getUser());
		tableRule.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getTableRules().add(tableRule);
		((DecisionTableEditor) getParentWindow()).addTablefromWindow(tableRule);
		((DecisionTableEditor) getParentWindow()).sortTableMenu();
		close();
	}

}
