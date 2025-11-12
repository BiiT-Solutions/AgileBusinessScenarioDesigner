package com.biit.abcd.webpages.components;

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

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.form.entity.TreeObject;

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
					case MULTI_TEXT:
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
