package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.expressions.ExpressionThen;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.Action;
import com.biit.abcd.persistence.entity.rules.ActionExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressiontree.ExpressionTreeTable;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewActionExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private FormAnswerTable formAnswerTable;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionThen expression;

	public AddNewActionExpressionWindow(Action action) throws NotValidExpression {
		super();
		if (!(action instanceof ActionExpression)) {
			throw new NotValidExpression("Only Action Expressions allowed.");
		}
		setWidth("80%");
		setHeight("80%");
		setContent(generateContent((ActionExpression) action));
		setResizable(false);
		setCaption(ServerTranslate.tr(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(ActionExpression action) {
		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new ExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();

		ExpressionTreeTable thenTable = expressionEditorComponent.addThenExpression(ServerTranslate
				.tr(LanguageCodes.FORM_EXPRESSION_TABLE_NAME));

		expression = new ExpressionThen();
		expression.addDefaultChild();
		expression.setCreatedBy(UserSessionHandler.getUser());
		expression.setUpdatedBy(UserSessionHandler.getUser());
		expression.setUpdateTime();
		thenTable.addExpression(expression);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public Answer getSelectedTableValue() {
		return formAnswerTable.getValue();
	}

	public ExpressionThen getExpression() {
		return expression;
	}
}
