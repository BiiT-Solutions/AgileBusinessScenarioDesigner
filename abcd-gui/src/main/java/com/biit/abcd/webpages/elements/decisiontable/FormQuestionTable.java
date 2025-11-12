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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.entity.TreeObject;
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
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItem(Object)} for form members. in
	 * this table answers are not shown.
	 *
	 * @param element
	 */
	@Override
	public void addItem(TreeObject element, TreeObject parent, boolean select) {
		if (!(element instanceof Answer)) {
			super.addItem(element, parent, select);
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
