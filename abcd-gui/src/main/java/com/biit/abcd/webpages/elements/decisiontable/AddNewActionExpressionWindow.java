package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.SimpleExpressionEditorComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewActionExpressionWindow extends AcceptCancelClearWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionChain expressionChain;

	public AddNewActionExpressionWindow(ExpressionChain action) {
		super();

		setWidth("90%");
		setHeight("90%");
		setContent(generateContent(action));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(ExpressionChain action) {
		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new SimpleExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();

		expressionChain = (ExpressionChain) action.generateCopy();

		((SimpleExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(expressionChain);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}

	public void clearSelection() {
		if (expressionEditorComponent != null) {
			((SimpleExpressionEditorComponent) expressionEditorComponent).clear();
		}
		expressionChain = null;
	}
}
