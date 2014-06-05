package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.SelectionTableWindow;
import com.vaadin.ui.Component;

public class AddNewConditionWindow extends SelectionTableWindow {
	private static final long serialVersionUID = 6781910083959136654L;
	private FormQuestionTable formQuestionTable;

	public AddNewConditionWindow(Form form, boolean multiselect) {
		super(form, multiselect);
		cancelButton.setCaption(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
	}

	public Question getSelectedQuestion() {
		return (Question) formQuestionTable.getValue();
	}

	public Set<Question> getSelectedQuestions() {
		Set<TreeObject> selectedObjects = formQuestionTable.getTreeObjectsSelected();
		Set<Question> selectedQuestions = new HashSet<Question>();
		for (TreeObject object : selectedObjects) {
			if (object instanceof Question) {
				selectedQuestions.add((Question) object);
			}
		}
		return selectedQuestions;
	}

	public void disableQuestion(Question question) {
		formQuestionTable.disableQuestion(question);
	}

	public void disableQuestions(Collection<Question> collection) {
		formQuestionTable.disableQuestions(collection);
	}

	@Override
	public Component generateContent(TreeObject treeObject) {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setMultiSelect(isMultiselect());
		formQuestionTable.setSizeFull();
		if (treeObject instanceof Form) {
			formQuestionTable.setRootElement((Form) treeObject);
			formQuestionTable.setSelectable(true);
		}

		return formQuestionTable;
	}
	
	public boolean isElementFiltered(Object itemId) {
		return true;
	}
}
