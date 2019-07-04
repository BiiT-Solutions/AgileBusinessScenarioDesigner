package com.biit.abcd.webpages.elements.drools.rule;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.DroolsRuleEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindowNewRule extends WindowCreateNewObject {
	private static final long serialVersionUID = 449099000293784033L;

	public WindowNewRule(DroolsRuleEditor parentWindow, LanguageCodes windowCaption, LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void concreteAcceptAction(TextField inputTextField) {
		for (Rule rule : UserSessionHandler.getFormController().getForm().getRules()) {
			if (rule.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_EXPRESSION_NAME);
				return;
			}
		}
		Rule rule = new Rule();
		rule.setName(inputTextField.getValue());
		rule.setCreatedBy(UserSessionHandler.getUser());
		rule.setUpdatedBy(UserSessionHandler.getUser());
		rule.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getRules().add(rule);
		((DroolsRuleEditor) getParentWindow()).addRulefromWindow(rule);
		((DroolsRuleEditor) getParentWindow()).sortTableMenu();

		AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' has created a " + rule.getClass() + " with 'Name: " + rule.getName() + "'.");

		close();
	}
}
