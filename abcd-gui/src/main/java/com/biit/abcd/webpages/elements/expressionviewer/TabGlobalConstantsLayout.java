package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.webpages.FormManager;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class TabGlobalConstantsLayout extends TabLayout {
	private static final long serialVersionUID = 3488733953726761594L;

	public TabGlobalConstantsLayout() {
		try {
			createGlobalConstantsElements(UserSessionHandler.getGlobalVariablesController().getGlobalVariables());
		} catch (UnexpectedDatabaseException e) {
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	private void createGlobalConstantsElements(List<GlobalVariable> globalVariables) {
		removeAllComponents();
		// Add the system date variable
		addSystemDate();
		// Add the other variables
		if (globalVariables != null) {
			for (GlobalVariable globalVariable : globalVariables) {
				addItem(globalVariable);
			}
		}
	}

	private void addItem(final GlobalVariable globalVariable) {
		Button globalVariableButton = new Button(globalVariable.getName(), new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExpressionValueGlobalConstant exprValue = new ExpressionValueGlobalConstant(globalVariable);
				addExpression(exprValue);
			}
		});
		globalVariableButton.setWidth("100%");
		// This style hides text overflow.
		globalVariableButton.addStyleName("v-expression-button-selector");
		addComponent(globalVariableButton);
		setComponentAlignment(globalVariableButton, Alignment.MIDDLE_CENTER);
	}

	private void addSystemDate() {
		Button globalVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_STRING_GLOBAL_VARIABLE_SYSTEM_DATE),
				new ClickListener() {
					private static final long serialVersionUID = 6377436020520436619L;

					@Override
					public void buttonClick(ClickEvent event) {
						ExpressionValueSystemDate exprValue = new ExpressionValueSystemDate();
						addExpression(exprValue);
					}
				});
		globalVariableButton.setWidth("100%");
		// This style hides text overflow.
		globalVariableButton.addStyleName("v-expression-button-selector");
		addComponent(globalVariableButton);
		setComponentAlignment(globalVariableButton, Alignment.MIDDLE_CENTER);
	}

}
