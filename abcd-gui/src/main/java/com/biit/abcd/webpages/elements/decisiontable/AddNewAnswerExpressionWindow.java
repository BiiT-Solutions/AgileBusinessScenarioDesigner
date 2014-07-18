package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.SelectFormAnswerTable;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;

public class AddNewAnswerExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 7699690992550597244L;
	private SelectFormAnswerTable formAnswerTable;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionChain expressionChain;

	//TODO
/*
	public AddNewAnswerExpressionWindow(AnswerExpression answer) throws NotValidExpression {
		super();
		if (!(answer instanceof AnswerExpression)) {
			throw new NotValidExpression("Only expression chains allowed.");
		}
		setWidth("90%");
		setHeight("90%");
		setContent(generateContent(answer));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	public Component generateContent(AnswerExpression answer) {
		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new AnswerExpressionEditorComponent(true);
		expressionEditorComponent.setSizeFull();

		if (answer.getExpressionChain() == null) {
			expressionChain = new ExpressionChain();
			expressionChain.setCreatedBy(UserSessionHandler.getUser());
			expressionChain.setUpdatedBy(UserSessionHandler.getUser());
			expressionChain.setUpdateTime();
		} else {
			expressionChain = answer.getExpressionChain();
		}
		((AnswerExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(expressionChain);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}*/

	public Answer getSelectedTableValue() {
		return formAnswerTable.getValue();
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}
}