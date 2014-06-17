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

	public AddNewConditionWindow(Form form, boolean multiselect) {
		super(form, multiselect);
		cancelButton.setCaption(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
		setCaption(ServerTranslate.tr(LanguageCodes.CONDITION_TABLE_NEW_CONDITION_CAPTION));
		setModal(true);
	}

	public Question getSelectedQuestion() {
		return (Question) getTable().getValue();
	}

	public Set<Question> getSelectedQuestions() {
		Set<TreeObject> selectedObjects = getTable().getTreeObjectsSelected();
		Set<Question> selectedQuestions = new HashSet<Question>();
		for (TreeObject object : selectedObjects) {
			if (object instanceof Question) {
				selectedQuestions.add((Question) object);
			}
		}
		return selectedQuestions;
	}

	public void disableQuestion(Question question) {
		((FormQuestionTable) getTable()).disableQuestion(question);
	}

	public void disableQuestions(Collection<Question> collection) {
		((FormQuestionTable) getTable()).disableQuestions(collection);
	}

	@Override
	public Component generateContent(TreeObject treeObject) {
		setTable(new FormQuestionTable());
		getTable().setMultiSelect(isMultiselect());
		getTable().setSizeFull();
		if (treeObject instanceof Form) {
			getTable().setRootElement((Form) treeObject);
			getTable().setSelectable(true);
		}

		return getTable();
	}

	public boolean isElementFiltered(Object itemId) {
		return true;
	}
}
