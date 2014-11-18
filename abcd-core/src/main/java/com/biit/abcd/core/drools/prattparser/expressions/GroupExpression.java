package com.biit.abcd.core.drools.prattparser.expressions;

import java.util.UUID;

import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;

public class GroupExpression implements ITreeElement {

	private final ITreeElement element;
	private final String treeElementId;

	public GroupExpression(ITreeElement element) {
		this.element = element;
		this.treeElementId = UUID.randomUUID().toString().replace("-", "").replace(" ", "");
	}

	@Override
	public void accept(ITreeElementVisitor visitor) throws NotCompatibleTypeException {
		visitor.visit(this);
	}
	
	public ITreeElement getElement() {
		return this.element;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain();
		expChain.addExpressions(new ExpressionSymbol(AvailableSymbol.LEFT_BRACKET));
		if (element != null) {
			expChain.addExpressions(element.getExpressionChain());
		}
		expChain.addExpressions(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		expChain.setName(this.treeElementId);
		return expChain;
	}

}
