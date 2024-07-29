package com.biit.abcd.webpages.elements.decisiontable;

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
