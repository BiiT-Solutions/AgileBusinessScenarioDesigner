package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow;
import com.biit.form.TreeObject;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewQuestionEditorWindow extends AcceptCancelClearWindow {

	private static final long serialVersionUID = -4090805671578721633L;
	private QuestionEditorComponent questionEditorComponent;

	public AddNewQuestionEditorWindow(Form form, boolean multiselect) {
		super();
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
		cancelButton.setCaption(ServerTranslate.translate(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.translate(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NEW_CONDITION_CAPTION));
		setModal(true);
		setContent(generateContent(new ExpressionValueCustomVariable(form, null)));
	}

	// Not used.
	public AddNewQuestionEditorWindow(ExpressionValueCustomVariable reference) {
		super();
		setContent(generateContent(reference));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	private Component generateContent(ExpressionValueCustomVariable reference) {
		VerticalLayout layout = new VerticalLayout();
		// Create content
		questionEditorComponent = new QuestionEditorComponent(reference);
		questionEditorComponent.setSizeFull();

		layout.addComponent(questionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public void select(TreeObject selected) {
		if (questionEditorComponent != null) {
			questionEditorComponent.setSelected(selected);
		}
	}

	public void select(ExpressionValueCustomVariable customVariable) {
		if (questionEditorComponent != null) {
			questionEditorComponent.setSelected(customVariable);
		}
	}

	public Object getSelectedCondition() {
		return questionEditorComponent.getSelectedObject();
	}

	public Object getSelectedCustomVariable() {
		return questionEditorComponent.getSelectedCustomVariable();
	}

	public Object getSelectedFormElement() {
		return questionEditorComponent.getSelectedFormElement();
	}

	public void clearSelection() {
		if (questionEditorComponent != null) {
			questionEditorComponent.clearSelection();
		}
	}
}
