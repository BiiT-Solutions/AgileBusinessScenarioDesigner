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

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.entity.TreeObject;
import com.vaadin.ui.TreeTable;

public class SelectFormAnswerTable extends TreeObjectTable {
	private static final long serialVersionUID = 6558723176678770970L;

	public SelectFormAnswerTable() {
		super();
	}

	@Override
	public Answer getValue() {
		if (!(super.getValue() instanceof Answer)) {
			return null;
		}
		return (Answer) super.getValue();
	}

	/**
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItem(Object)} for form members. in
	 * this table only answers are shown.
	 *
	 * @param element
	 */
	@Override
	public void addItem(TreeObject element, TreeObject parent, boolean select) {
		// Only add answers.
		if ((element instanceof Answer)) {
			super.addItem(element, parent, select);
		}
	}
}
