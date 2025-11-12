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

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionChainHierarchyComparator;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.EditCellComponent;
import com.biit.abcd.webpages.components.SecuredEditCellComponent;
import com.biit.form.entity.TreeObject;

public class EditCellSortableByExpression extends SecuredEditCellComponent {
	private static final long serialVersionUID = -3538277564149518358L;
	private ExpressionChain expression;

	public void setExpression(ExpressionChain expression) {
		this.expression = expression;
	}

	private ExpressionChain getExpression() {
		return expression;
	}

	@Override
	public int compareTo(EditCellComponent otherCell) {
		if (otherCell instanceof EditCellSortableByExpression) {
			return new ExpressionChainHierarchyComparator().compare(getExpression(),
					((EditCellSortableByExpression) otherCell).getExpression());
		}
		return super.compareTo(otherCell);
	}

	public TreeObject getQuestion() {
		if (!getExpression().getExpressions().isEmpty()) {
			Expression expression1 = getExpression().getExpressions().get(0);
			if (expression1 instanceof ExpressionValueTreeObjectReference) {
				return ((ExpressionValueTreeObjectReference) expression1).getReference();
			}
		}
		return null;
	}

}
