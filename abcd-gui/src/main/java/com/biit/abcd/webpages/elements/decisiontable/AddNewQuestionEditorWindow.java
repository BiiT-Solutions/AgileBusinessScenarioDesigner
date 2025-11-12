package com.biit.abcd.webpages.elements.decisiontable;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.webpages.components.AcceptCancelClearWindow;
import com.biit.form.entity.TreeObject;
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
