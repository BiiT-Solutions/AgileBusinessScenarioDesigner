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
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ActionValueEditCell extends EditCellSortableByExpression {
	private static final long serialVersionUID = 5033744155212556036L;	

	public ActionValueEditCell() {
		super();
		setOnlyEdit(true);
		setCellBehaviour();
	}

	public void setLabel(ExpressionChain action) {
		if ((action == null) || (action.toString().length() == 0)) {
			setLabel("<div style=\"background-color: rgb(179, 46, 46); color: rgb(255,255,255); display: inline;\">"
					+ ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE) + "</div>");
		} else {
			setLabel(action.toString());
		}
	}

	private void setCellBehaviour() {
		addRemoveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 6253961924451407630L;

			@Override
			public void buttonClick(ClickEvent event) {
				setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
			}
		});
	}

	
}
