package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExpressionThen;
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
		for (ExprBasic existingExpressions : UserSessionHandler.getFormController().getForm().getExpressions()) {
			if (((ExpressionThen) existingExpressions).getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_EXPRESSION_NAME);
				return;
			}
		}
		ExpressionThen expression = new ExpressionThen();
		expression.addDefaultChild();
		expression.setName(inputTextField.getValue());
		expression.setCreatedBy(UserSessionHandler.getUser());
		expression.setUpdatedBy(UserSessionHandler.getUser());
		expression.setUpdateTime();
		UserSessionHandler.getFormController().getForm().getExpressions().add(expression);
		((ExpressionEditor) getParentWindow()).addExpressionToMenu(expression);
		((ExpressionEditor) getParentWindow()).sortTableMenu();
		close();
	}

}
