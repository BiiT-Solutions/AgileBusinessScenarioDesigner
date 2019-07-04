package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow;
import com.biit.abcd.webpages.components.SelectFormAnswerTable;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.SimpleExpressionEditorComponent;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewAnswerExpressionWindow extends AcceptCancelClearWindow {
	private static final long serialVersionUID = 7699690992550597244L;
	private SelectFormAnswerTable answerTable = null;
	private ExpressionEditorComponent expressionEditorComponent;
	protected ExpressionChain expressionChain;

	/**
	 * Constructor.
	 * 
	 * @param reference
	 *            Question or element to apply the expression.
	 * @param expressionChain
	 *            Expression applied
	 */
	public AddNewAnswerExpressionWindow(ExpressionValueTreeObjectReference reference, ExpressionChain expressionChain) {
		super();
		this.expressionChain = (ExpressionChain) expressionChain.generateCopy();
		setFirstElementNotEditable(this.expressionChain);
		if (reference instanceof ExpressionValueCustomVariable) {
			// Custom variable
			setContent(generateExpression());
		} else {
			TreeObject treeObject = reference.getReference();
			if (treeObject != null) {
				if ((treeObject instanceof Question) && (((Question) treeObject).getAnswerType() != AnswerType.INPUT)) {
					Question question = (Question) treeObject;
					setContent(generateTreeObjectTable(question));
				} else {
					setContent(generateExpression());
				}
			}
		}
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	public Component generateTreeObjectTable(Question question) {
		setWidth("50%");
		setHeight("75%");
		answerTable = new SelectFormAnswerTable();
		answerTable.setRootElement(question);
		answerTable.setNullSelectionAllowed(true);
		answerTable.setSelectable(true);
		answerTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4771701909112677308L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setSelectedTableElement();
			}
		});

		answerTable.setSizeFull();
		if (!expressionChain.getExpressions().isEmpty()
				&& (expressionChain.getExpressions().get(expressionChain.getExpressions().size() - 1) instanceof ExpressionValueTreeObjectReference)) {
			answerTable.setValue(((ExpressionValueTreeObjectReference) expressionChain.getExpressions().get(
					expressionChain.getExpressions().size() - 1)).getReference());
		} else {
			answerTable.setValue(null);
		}
		setSelectedTableElement();
		return answerTable;
	}

	private void setSelectedTableElement() {
		expressionChain.removeAllExpressions();
		if (answerTable.getValue() != null) {
			expressionChain.addExpression(new ExpressionValueTreeObjectReference(answerTable.getValue()));
		}
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
		if (answerTable == null) {
			return getExpressionWithoutFirstElement();
		}
		return expressionChain;
	}

	private ExpressionChain getExpressionWithoutFirstElement() {
		ExpressionChain expressionChain = (ExpressionChain) this.expressionChain.generateCopy();
		if ((expressionChain != null) && !expressionChain.getExpressions().isEmpty()) {
			expressionChain.removeFirstExpression();
		}
		return expressionChain;
	}
	
	private void setFirstElementNotEditable(ExpressionChain expressionChain){
		if ((expressionChain != null) && !expressionChain.getExpressions().isEmpty()) {
			expressionChain.getExpressions().get(0).setEditable(false);
		}
	}

	public void clearSelection() {
		if (answerTable != null) {
			answerTable.setValue(null);
		}
		if (expressionEditorComponent != null) {
			((SimpleExpressionEditorComponent) expressionEditorComponent).clear();
		}
		expressionChain = new ExpressionChain();
	}

	protected ExpressionEditorComponent getExpressionEditorComponent() {
		return expressionEditorComponent;
	}

	protected void setExpressionEditorComponent(ExpressionEditorComponent expressionEditorComponent) {
		this.expressionEditorComponent = expressionEditorComponent;
	}
}