package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.webpages.ExpressionEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.ui.TextField;

public class WindowNewExpression extends WindowCreateNewObject {
	private static final long serialVersionUID = -466962195753116776L;

	public WindowNewExpression(ExpressionEditor parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		for (ExpressionChain existingExpressions : UserSessionHandler.getFormController().getForm().getExpressionChain()) {
			if (existingExpressions.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_EXPRESSION_NAME);
				return;
			}
		}
		ExpressionChain expression = new ExpressionChain();
		expression.setName(inputTextField.getValue());
		expression.setCreatedBy(UserSessionHandler.getUser());
		expression.setUpdatedBy(UserSessionHandler.getUser());
		expression.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getExpressionChain().add(expression);
		((ExpressionEditor) getParentWindow()).addExpressionToMenu(expression);
		((ExpressionEditor) getParentWindow()).sortTableMenu();
		close();
	}

}
