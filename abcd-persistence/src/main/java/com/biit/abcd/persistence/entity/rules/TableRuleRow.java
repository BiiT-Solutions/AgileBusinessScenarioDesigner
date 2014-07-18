package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE_ROW")
public class TableRuleRow extends StorableObject {

	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain conditions;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ActionExpression> actions;

	public TableRuleRow() {
		conditions = new ExpressionChain();
		actions = new ArrayList<>();
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

	public void removeConditions() {
		conditions.removeAllExpressions();
	}

	public void addAction(ActionExpression action) {
		actions.add(action);
	}

	public List<ActionExpression> getActions() {
		return actions;
	}

	@Override
	public String toString() {
		return conditions.toString();
	}

	public int getConditionNumber(){
		return conditions.getExpressions().size();
	}

	public TableRuleRow generateCopy() {
		TableRuleRow copy = new TableRuleRow();
		copy.conditions = conditions.generateCopy();
		for(ActionExpression action: actions){
			ActionExpression actionCopy = action.generateCopy();
			copy.actions.add(actionCopy);
		}
		
		return copy;
	}
	
	public void addEmptyExpressionPair(){
		addCondition(new ExpressionValueTreeObjectReference());
		addCondition(new ExpressionChain());
	}
}
