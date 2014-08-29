package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.SelectFormAnswerTable;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.SimpleExpressionEditorComponent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewAnswerExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 7699690992550597244L;
	private SelectFormAnswerTable answerTable;
	private ExpressionEditorComponent expressionEditorComponent;
	private ExpressionChain expressionChain;

	public AddNewAnswerExpressionWindow(ExpressionValueTreeObjectReference reference, ExpressionChain expressionChain) {
		super();
		this.expressionChain = expressionChain.generateCopy();

		if (reference instanceof ExpressionValueCustomVariable) {
			// Custom variable
			setContent(generateExpression());
		} else {
			// Question
			Question question = (Question) reference.getReference();
			if (question.getAnswerType() != AnswerType.INPUT) {
				setContent(generateTable(question));
			} else {
				setContent(generateExpression());
			}
		}

		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	public Component generateTable(Question question) {
		setWidth("50%");
		setHeight("75%");
		answerTable = new SelectFormAnswerTable();
		answerTable.setRootElement(question);
		answerTable.setSelectable(true);
		answerTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4771701909112677308L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				expressionChain.removeAllExpressions();
				if (answerTable.getValue() != null) {
					expressionChain.addExpression(new ExpressionValueTreeObjectReference(answerTable.getValue()));
				}
			}
		});
		answerTable.setSizeFull();
		if (!expressionChain.getExpressions().isEmpty()
				&& (expressionChain.getExpressions().get(0) instanceof ExpressionValueTreeObjectReference)) {
			answerTable.setValue(((ExpressionValueTreeObjectReference) expressionChain.getExpressions().get(0))
					.getReference());
		} else {
			answerTable.setValue(null);
		}

		return answerTable;
	}

	public Component generateExpression() {
		setWidth("90%");
		setHeight("90%");

		VerticalLayout layout = new VerticalLayout();

		// Create content
		expressionEditorComponent = new SimpleExpressionEditorComponent();
		expressionEditorComponent.setSizeFull();

		((SimpleExpressionEditorComponent) expressionEditorComponent).refreshExpressionEditor(expressionChain);

		layout.addComponent(expressionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public ExpressionChain getExpressionChain() {
		return expressionChain;
	}
}