package com.biit.abcd.persistence.entity.rules;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.persistence.entity.StorableObject;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "rule_decision_table_row")
public class TableRuleRow extends StorableObject {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain conditions;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain action;

	public TableRuleRow() {
		conditions = new ExpressionChain();
		action = new ExpressionChain();
	}

	// Simple (Question : Answer => Action) builder
	public TableRuleRow(Expression question, Expression answer, ExpressionChain action){
		conditions = new ExpressionChain();
		this.action = new ExpressionChain();
		getConditions().add(question);
		getConditions().add(answer);
		getActionChain().setExpressions(action.getExpressions());
	}

	public void addCondition(Expression expression) {
		conditions.addExpression(expression);
	}

	public void removeCondition(Expression expression) {
		conditions.removeExpression(expression);
	}

	public List<Expression> getConditions() {
		return conditions.getExpressions();
	}

	public ExpressionChain getConditionChain() {
		return conditions;
	}

	public void removeConditions() {
		conditions.removeAllExpressions();
	}

	public void setAction(ExpressionChain action) {
		this.action = action;
	}

	public ExpressionChain getActionChain() {
		return action;
	}

	@Override
	public String toString() {
		return conditions.toString();
	}

	public int getConditionNumber() {
		return conditions.getExpressions().size();
	}

	public TableRuleRow generateCopy() {
		TableRuleRow copy = new TableRuleRow();
		copy.conditions = conditions.generateCopy();
		copy.action = action.generateCopy();
		return copy;
	}

	public void addEmptyExpressionPair() {
		addCondition(new ExpressionValueTreeObjectReference());
		addCondition(new ExpressionChain());
	}

	public void setExpression(int position, Expression expression){
		//TODO exception if bad expression?
		getConditions().set(position, expression);
	}
}
