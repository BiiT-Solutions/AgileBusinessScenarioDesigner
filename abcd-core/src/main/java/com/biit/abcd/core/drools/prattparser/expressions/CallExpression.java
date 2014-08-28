package com.biit.abcd.core.drools.prattparser.expressions;

import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionToken;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

/**
 * A function call like "a(b, c, d)".
 */
public class CallExpression implements ITreeElement {

	private final ITreeElement leftElement;
	private final ExpressionToken function;
	private final List<ITreeElement> args;

	public CallExpression(ExpressionToken token, ITreeElement leftElement, List<ITreeElement> args) {
		this.function = token;
		this.leftElement = leftElement;
		this.args = args;
	}

	@Override
	public void accept(ITreeElementVisitor visitor) {
		visitor.visit(this);
	}

	public ITreeElement getFunction() {
		return this.leftElement;
	}

	public List<ITreeElement> getArgs() {
		return this.args;
	}

	@Override
	public ExpressionChain getExpressionChain() {
		ExpressionChain expChain = new ExpressionChain(this.leftElement.getExpressionChain(), this.function.getExpression());
		for(ITreeElement treeElem : this.args){
			expChain.addExpressions(treeElem.getExpressionChain());
		}
		return expChain;
	}
}
