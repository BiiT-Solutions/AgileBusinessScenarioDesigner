package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.utils.INameAttribute;

/**
 * Defines a drools rule.
 * 
 */
@Entity
@Table(name = "RULE")
public class Rule extends StorableObject implements INameAttribute {

	private String name;

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain condition;
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain actions;

	public Rule() {
		this.setCondition(new ExpressionChain());
		this.setActions(new ExpressionChain());
	}

	public Rule(String name) {
		this.setCondition(new ExpressionChain());
		this.setActions(new ExpressionChain());
		this.setName(name);
	}

	public Rule(String name, ExpressionChain conditions, ExpressionChain actions) {
		this.setCondition(conditions);
		this.setActions(actions);
		this.setName(name);
	}

	public ExpressionChain getCondition() {
		return this.condition;
	}

	public List<Expression> getConditions() {
		return this.condition.getExpressions();
	}

	public void setCondition(ExpressionChain condition) {
		this.condition = condition;
	}

	public ExpressionChain getActions() {
		return this.actions;
	}

	public void setActions(ExpressionChain actions) {
		this.actions = actions;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = new HashSet<>();
		references.addAll(this.getCondition().getReferencedTreeObjects());
		references.addAll(this.getActions().getReferencedTreeObjects());
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if (commonTreeObject.equals(treeObject)) {
				return true;
			}
		}
		return false;
	}
}
