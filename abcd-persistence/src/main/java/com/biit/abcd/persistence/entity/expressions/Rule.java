package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.utils.ITableCellEditable;

/**
 * Defines a drools rule.
 * 
 */
@Entity
@Table(name = "RULE")
public class Rule extends StorableObject implements ITableCellEditable {

	private String name;

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain condition;
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private ExpressionChain actions;

	public Rule() {
		setActions(new ExpressionChain());
		setCondition(new ExpressionChain());
	}

	public ExpressionChain getCondition() {
		return condition;
	}

	public void setCondition(ExpressionChain condition) {
		this.condition = condition;
	}

	public ExpressionChain getActions() {
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
		references.addAll(getCondition().getReferencedTreeObjects());
		references.addAll(getActions().getReferencedTreeObjects());
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if (commonTreeObject.equals(treeObject)) {
				return true;
			}
		}
		return false;
	}
}
