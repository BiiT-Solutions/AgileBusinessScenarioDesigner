package com.biit.abcd.persistence.entity.rules;

import java.util.HashSet;
import java.util.List;
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
	public TableRuleRow(Expression question, Expression answer, ExpressionChain action) {
		conditions = new ExpressionChain();
		this.action = new ExpressionChain();
		getConditions().add(question);
		getConditions().add(answer);
		getActionChain().setExpressions(action.getExpressions());
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

	public List<Expression> getConditions() {
		return conditions.getExpressions();
	}

	public ExpressionChain getConditionChain() {
		return conditions;
	}

	public void setConditionsChain(ExpressionChain conditions) {
		this.conditions = conditions;
	}

	public void removeConditions() {
		conditions.removeAllExpressions();
	}

	public void setActionChain(ExpressionChain action) {
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
		// TODO exception if bad expression?
		getConditions().set(position, expression);
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
			condition.copyData(tableRuleRow.getConditionChain());
			this.setConditionsChain(condition);
			ExpressionChain action = new ExpressionChain();
			action.copyData(tableRuleRow.getActionChain());
			this.setActionChain(action);
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TableRuleRow.");
		}
	}
}
