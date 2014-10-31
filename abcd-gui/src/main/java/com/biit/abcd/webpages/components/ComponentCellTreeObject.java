package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.form.TreeObject;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	public void update(TreeObject treeObject) {
		clear();
		String name = null;
		if ((treeObject instanceof Form) || (treeObject instanceof TestScenarioForm)) {
			name = treeObject.getLabel();
		} else {
			name = treeObject.getName();
		}
		addLabel(name);
		addIcon(getIcon(treeObject));
	}

	protected static ThemeIcon getIcon(TreeObject element) {
		if (element instanceof Question) {
			Question question = (Question) element;
			switch (question.getAnswerType()) {
			case MULTI_CHECKBOX:
				return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_CHECKLIST;
			case RADIO:
				return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_RADIOBUTTON;
			case INPUT:
				if (question.getAnswerFormat() != null) {
					switch (question.getAnswerFormat()) {
					case DATE:
						return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_DATE;
					case NUMBER:
						return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_NUMBER;
					case POSTAL_CODE:
						return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_POSTALCODE;
					case TEXT:
						return ThemeIcon.TREE_DESIGNER_QUESTION_TYPE_TEXT;
					}
				}
			}
		} else if (element instanceof Group) {
			Group group = (Group) element;
			if (group.isRepeatable()) {
				return ThemeIcon.TREE_DESIGNER_GROUP_LOOP;
			}
		}
		return null;
	}

}
