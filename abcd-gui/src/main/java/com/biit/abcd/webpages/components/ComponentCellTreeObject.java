package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;

public class ComponentCellTreeObject extends ComponentCell {
	private static final long serialVersionUID = 76595396879136434L;

	public void update(TreeObject treeObject) {
		clear();
		addLabel(treeObject.getName());
		addIcon(getIcon(treeObject));
	}

	protected static ThemeIcon getIcon(TreeObject element) {
		if (element instanceof Question) {
			Question question = (Question) element;
			switch (question.getAnswerType()) {
			case MULTI_CHECKBOX:
				return ThemeIcon.TREE_DESIGNER_QUESTION_CHECKLIST;
			case RADIO:
				return ThemeIcon.TREE_DESIGNER_QUESTION_RADIOBUTTON;
			case INPUT:
				switch (question.getAnswerFormat()) {
				case DATE:
					return ThemeIcon.TREE_DESIGNER_QUESTION_DATE;
				case NUMBER:
					return ThemeIcon.TREE_DESIGNER_QUESTION_NUMBER;
				case POSTAL_CODE:
					return ThemeIcon.TREE_DESIGNER_QUESTION_POSTALCODE;
				case TEXT:
					return ThemeIcon.TREE_DESIGNER_QUESTION_TEXT;
				}
			}
		} else if (element instanceof Group) {
			Group group = (Group) element;
			if (group.isRepetable()) {
				return ThemeIcon.TREE_DESIGNER_GROUP_LOOP;
			}
		}
		return null;
	}

}
