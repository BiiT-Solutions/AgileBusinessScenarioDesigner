package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;

/**
 * Defines a drools rule.
 * 
 */
@Entity
@Table(name = "rule")
public class Rule extends StorableObject implements INameAttribute {

	private String name;

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain condition;
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain actions;

	public Rule() {
		super();
		setCondition(new ExpressionChain());
		setActions(new ExpressionChain());
	}

	public Rule(String name) {
		super();
		setCondition(new ExpressionChain());
		setActions(new ExpressionChain());
		setName(name);
	}

	public Rule(String name, ExpressionChain conditions, ExpressionChain actions) {
		super();
		setCondition(conditions);
		setActions(actions);
		setName(name);
	}

	// public ExpressionChain getCondition() {
	// return condition;
	// }

	/**
	 * Add more conditions to the existing one (with 'AND' operator)
	 */
	public void addConditions(ExpressionChain extraConditions) {
		if (extraConditions != null) {
			if (condition.getExpressions().isEmpty()) {
				condition.getExpressions().addAll(extraConditions.getExpressions());
			} else {
				condition.getExpressions().add(new ExpressionOperatorLogic(AvailableOperator.AND));
				condition.getExpressions().addAll(extraConditions.getExpressions());
			}
		}
	}

	public List<Expression> getConditions() {
		return condition.getExpressions();
	}

	public ExpressionChain getConditionChain() {
		return condition;
	}

	public void setCondition(ExpressionChain condition) {
		this.condition = condition;
	}

	public ExpressionChain getActionChain() {
		return actions;
	}

	public void setActions(ExpressionChain actions) {
		this.actions = actions;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = new HashSet<>();
		references.addAll(getConditionChain().getReferencedTreeObjects());
		references.addAll(getActionChain().getReferencedTreeObjects());
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if (commonTreeObject.equals(treeObject)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.add(condition);
		innerStorableObjects.addAll(condition.getAllInnerStorableObjects());
		innerStorableObjects.add(actions);
		innerStorableObjects.addAll(actions.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

	public Rule generateCopy() {
		Rule copy = new Rule();
		if (name != null) {
			copy.name = new String(name);
		}
		copy.setCondition(this.condition.generateCopy());
		copy.setActions(this.actions.generateCopy());

		return copy;
	}
}
