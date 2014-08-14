package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.TreeObject;
import com.vaadin.ui.TreeTable;

public class FormQuestionTable extends TreeObjectTable {
	private static final long serialVersionUID = -715631213528124119L;
	private List<Question> disabledQuestions;

	public FormQuestionTable() {
		super();
		disabledQuestions = new ArrayList<Question>();

		setCellStyleGenerator(new FormQuestionTableCellGenerator());
	}

	public List<Question> getDisabledQuestions() {
		return disabledQuestions;
	}

	public void disableQuestion(Question question) {
		disabledQuestions.add(question);
		refreshRowCache();
	}

	public void disableQuestions(Collection<Question> collection) {
		disabledQuestions.addAll(collection);
		refreshRowCache();
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItem(Object)} for form members. in this table answers
	 * are not shown.
	 * 
	 * @param element
	 */
	@Override
	public void addItem(TreeObject element, TreeObject parent) {
		if (!(element instanceof Answer)) {
			super.addItem(element, parent);
		}
	}

	@Override
	public boolean isElementFiltered(Object itemId) {
		if (itemId instanceof Question) {
			return false;
		}
		return true;
	}
}
