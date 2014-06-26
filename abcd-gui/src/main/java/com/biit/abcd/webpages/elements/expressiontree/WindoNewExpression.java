package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.ExpressionEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindoNewExpression extends WindowCreateNewObject {
	private static final long serialVersionUID = -466962195753116776L;

	public WindoNewExpression(ExpressionEditor parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		for (FormExpression existingExpressions : UserSessionHandler.getFormController().getForm().getFormExpressions()) {
			if (existingExpressions.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_EXPRESSION_NAME);
				return;
			}
		}
		FormExpression expression = new FormExpression();
		expression.setName(inputTextField.getValue());
		expression.setCreatedBy(UserSessionHandler.getUser());
		expression.setUpdatedBy(UserSessionHandler.getUser());
		expression.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getFormExpressions().add(expression);
		((ExpressionEditor) getParentWindow()).addExpressionToMenu(expression);
		((ExpressionEditor) getParentWindow()).sortTableMenu();
		close();
	}

}
