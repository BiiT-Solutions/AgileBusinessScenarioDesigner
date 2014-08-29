package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.form.TreeObject;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class AddNewQuestionEditorWindow extends AcceptCancelWindow {

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
		setContent(generateContent(form));
	}

	public AddNewQuestionEditorWindow(ExpressionValueTreeObjectReference reference) {
		super();
		setContent(generateContent(reference.getReference()));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	private Component generateContent(TreeObject treeObject) {
		VerticalLayout layout = new VerticalLayout();
		// Create content
		questionEditorComponent = new QuestionEditorComponent(treeObject);
		questionEditorComponent.setSizeFull();

		layout.addComponent(questionEditorComponent);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public Object getSelectedCondition() {
		return questionEditorComponent.getSelectedObject();
	}

	public Object getSelectedCustomVariable(){
		return questionEditorComponent.getSelectedCustomVariable();
	}

	public Object getSelectedFormElement(){
		return questionEditorComponent.getSelectedFormElement();
	}
}
