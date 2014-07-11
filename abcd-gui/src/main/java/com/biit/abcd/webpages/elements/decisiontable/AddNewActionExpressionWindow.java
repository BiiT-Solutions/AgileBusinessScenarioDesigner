package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.Action;
import com.biit.abcd.persistence.entity.rules.ActionExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.SelectFormAnswerTable;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.SimpleExpressionEditorComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewActionExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private SelectFormAnswerTable formAnswerTable;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionChain expression;

	public AddNewActionExpressionWindow(Action action) throws NotValidExpression {
		super();
		if (!(action instanceof ActionExpression)) {
			throw new NotValidExpression("Only Action Expressions allowed.");
		}
		setWidth("90%");
		setHeight("90%");
		setContent(generateContent((ActionExpression) action));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(ActionExpression action) {
		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new SimpleExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();

		if (action.getExpression() == null) {
			expression = new ExpressionChain();
			expression.setCreatedBy(UserSessionHandler.getUser());
			expression.setUpdatedBy(UserSessionHandler.getUser());
			expression.setUpdateTime();
		} else {
			expression = action.getExpression();
		}
		((SimpleExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(expression);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public Answer getSelectedTableValue() {
		return formAnswerTable.getValue();
	}

	public ExpressionChain getExpression() {
		return expression;
	}
}
