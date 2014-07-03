package com.biit.abcd.webpages.elements.diagrambuilder;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.decisiontable.FormAnswerTable;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private FormAnswerTable formAnswerTable;
	private ExpressionEditorComponent expressionEditorComponent;
	private FormExpression expression;

	public AddNewExpressionWindow(FormExpression expression) {
		super();
		setWidth("90%");
		setHeight("90%");
		setContent(generateContent(expression));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(FormExpression expression) {
		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new ExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();

		if (expression == null) {
			this.expression = new FormExpression();
			this.expression.setCreatedBy(UserSessionHandler.getUser());
			this.expression.setUpdatedBy(UserSessionHandler.getUser());
			this.expression.setUpdateTime();
		} else {
			this.expression = expression;
		}
		expressionEditorComponent.refreshExpressionEditor(expression);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public Answer getSelectedTableValue() {
		return formAnswerTable.getValue();
	}

	public FormExpression getExpression() {
		return expression;
	}
}