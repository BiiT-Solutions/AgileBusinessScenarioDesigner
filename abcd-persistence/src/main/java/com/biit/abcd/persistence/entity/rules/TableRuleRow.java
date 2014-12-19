package com.biit.abcd.persistence.entity.rules;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "rule_decision_table_row")
public class TableRuleRow extends StorableObject implements Comparable<TableRuleRow> {

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain conditions;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain action;

	public TableRuleRow() {
		conditions = new ExpressionChain();
		conditions.setName("TableRuleRowCondition");
		action = new ExpressionChain();
		action.setName("TableRuleRowAction");
	}

	// Simple (Question : Answer => Action) builder
	public TableRuleRow(Expression question, Expression answer, ExpressionChain action) {
		conditions = new ExpressionChain();
		conditions.setName("TableRuleRowCondition");
		this.action = new ExpressionChain();
		this.action.setName("TableRuleRowAction");
		getConditions().addExpression(question);
		getConditions().addExpression(answer);
		getAction().setExpressions(action.getExpressions());
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (conditions != null) {
			conditions.resetIds();
		}
		if (action != null) {
			action.resetIds();
		}
	}

	public void addCondition(Expression expression) {
		conditions.addExpression(expression);
	}

	public void removeCondition(Expression expression) {
		conditions.removeExpression(expression);
	}

	public ExpressionChain getConditions() {
		return conditions;
	}

	public void setConditions(ExpressionChain conditions) {
		this.conditions = conditions;
	}

	public void removeConditions() {
		conditions.removeAllExpressions();
	}

	public void setAction(ExpressionChain action) {
		this.action = action;
	}

	public ExpressionChain getAction() {
		return action;
	}

	@Override
	public String toString() {
		return conditions.toString() + " -> " + action.toString();
	}

	public int getConditionNumber() {
		return conditions.getExpressions().size();
	}

	public TableRuleRow generateCopy() {
		TableRuleRow copy = null;
		try {
			copy = this.getClass().newInstance();
			copy.copyData(this);
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return copy;
	}

	public void addEmptyExpressionPair() {
		addCondition(new ExpressionValueTreeObjectReference());
		addCondition(new ExpressionChain());
	}

	public void setExpression(int position, Expression expression) {
		getConditions().getExpressions().set(position, expression);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(conditions);
		innerStorableObjects.addAll(conditions.getAllInnerStorableObjects());
		innerStorableObjects.add(action);
		innerStorableObjects.addAll(action.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TableRuleRow) {
			super.copyBasicInfo(object);
			TableRuleRow tableRuleRow = (TableRuleRow) object;

			ExpressionChain condition = new ExpressionChain();
			condition.copyData(tableRuleRow.getConditions());
			setConditions(condition);

			ExpressionChain action = new ExpressionChain();
			action.copyData(tableRuleRow.getAction());
			setAction(action);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TableRuleRow.");
		}
	}

	@Override
	public int compareTo(TableRuleRow otherRow) {
		if (this.getConditions().getExpressions().size() > 0 && otherRow.getConditions().getExpressions().size() > 0) {
			Expression expression1 = this.getConditions().getExpressions().get(0);
			Expression expression2 = otherRow.getConditions().getExpressions().get(0);
			if (expression1 instanceof ExpressionValueTreeObjectReference) {
				if (expression2 instanceof ExpressionValueTreeObjectReference) {
					return ((ExpressionValueTreeObjectReference) expression1).getReference().compareTo(
							((ExpressionValueTreeObjectReference) expression2).getReference());
				}
				// First null values.
				return 1;
			} else {
				if (expression2 instanceof ExpressionValueTreeObjectReference) {
					// First null values.
					return -1;
				}
				return 0;
			}
		} else {
			// First empty expressions.
			return this.getConditions().getExpressions().size() - otherRow.getConditions().getExpressions().size();
		}
	}
}
