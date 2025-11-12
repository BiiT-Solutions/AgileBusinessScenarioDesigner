package com.biit.abcd.core.drools.prattparser.visitor;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import com.biit.abcd.core.drools.prattparser.expressions.AssignExpression;
import com.biit.abcd.core.drools.prattparser.expressions.CallExpression;
import com.biit.abcd.core.drools.prattparser.expressions.ConditionalExpression;
import com.biit.abcd.core.drools.prattparser.expressions.GroupExpression;
import com.biit.abcd.core.drools.prattparser.expressions.NameExpression;
import com.biit.abcd.core.drools.prattparser.expressions.OperatorExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PostfixExpression;
import com.biit.abcd.core.drools.prattparser.expressions.PrefixExpression;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.entity.TreeObject;

/**
 * Visitor used only for debugging purposes 
 */
public class TreeElementPrintVisitor implements ITreeElementVisitor {

	private StringBuilder builder;

	public TreeElementPrintVisitor() {
		this.builder = new StringBuilder();
	}

	@Override
	public void visit(AssignExpression assign) throws NotCompatibleTypeException {
		this.builder.append("(").append(assign.getName()).append(" = ");
		assign.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(CallExpression call) throws NotCompatibleTypeException {
		call.getLeftElement().accept(this);
		this.builder.append("(");
		for (int i = 0; i < call.getArgs().size(); i++) {
			call.getArgs().get(i).accept(this);
			if (i < (call.getArgs().size() - 1)) {
				this.builder.append(", ");
			}
		}
		this.builder.append(")");
	}

	@Override
	public void visit(ConditionalExpression condition) throws NotCompatibleTypeException {
		this.builder.append("(");
		condition.getCondition().accept(this);
		this.builder.append(" ? ");
		condition.getThenArm().accept(this);
		this.builder.append(" : ");
		condition.getElseArm().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(NameExpression name) {
		// The answers have a label not a technical name
		if (name.getExpressionChain().getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
			ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) name.getExpressionChain()
					.getExpressions().get(0);
			TreeObject treeObject = expVal.getReference();
			if (treeObject instanceof Answer) {
				this.builder.append(((Answer) treeObject).getLabel());
			}
		}
		// For everything else, get the technical name
		this.builder.append(name.getName());
	}

	@Override
	public void visit(OperatorExpression operator) throws NotCompatibleTypeException {
		this.builder.append("(");
		operator.getLeftElement().accept(this);
		this.builder.append(" ").append(operator.getOperator().getPunctuator()).append(" ");
		operator.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(PostfixExpression postfix) throws NotCompatibleTypeException {
		this.builder.append("(");
		postfix.getLeftElement().accept(this);
		this.builder.append(postfix.getOperator().getPunctuator()).append(")");
	}

	@Override
	public void visit(PrefixExpression prefix) throws NotCompatibleTypeException {
		this.builder.append("(").append(prefix.getOperator().getPunctuator());
		prefix.getRightElement().accept(this);
		this.builder.append(")");
	}

	@Override
	public void visit(GroupExpression group) throws NotCompatibleTypeException {
		this.builder.append("(");
		group.getElement().accept(this);
		this.builder.append(")");
	}

	public StringBuilder getBuilder() {
		return this.builder;
	}
}
