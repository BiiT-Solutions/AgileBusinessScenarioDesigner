package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.FormTreeTable;

public class FormQuestionTable extends FormTreeTable {
	private static final long serialVersionUID = -715631213528124119L;
	private List<Question> disabledQuestions;

	public FormQuestionTable() {
		super();
		disabledQuestions = new ArrayList<Question>();
		setImmediate(true);
		// Remove innecesary properties.
		removeContainerProperty(FormTreeTableProperties.RULES);

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

	@Override
	public Question getValue() {
		if (disabledQuestions.contains(super.getValue())) {
			return null;
		} else {
			if (!(super.getValue() instanceof Question)) {
				return null;
			}
			return (Question) super.getValue();
		}
	}
}
