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
	private Expressions condition;
	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private Expressions actions;

	public Rule() {
		setActions(new Expressions());
		setCondition(new Expressions());
	}

	public Expressions getCondition() {
		return condition;
	}

	public void setCondition(Expressions condition) {
		this.condition = condition;
	}

	public Expressions getActions() {
		return actions;
	}

	public void setActions(Expressions actions) {
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Set<TreeObject> getReferencedTreeObjects() {
		Expressions condition = getCondition();
		List<Expression> expressions = condition.getExpressions();
		Set<TreeObject> references = new HashSet<>();
		for (Expression expression : expressions) {
			if (expression instanceof ExpressionValueFormCustomVariable) {
				references.add(((ExpressionValueFormCustomVariable) expression).getQuestion());
				continue;
			}
			if (expression instanceof ExpressionValueTreeObjectReference) {
				references.add(((ExpressionValueTreeObjectReference) expression).getReference());
				continue;
			}
		}
		return references;
	}

	public boolean isAssignedTo(TreeObject treeObject) {
		Set<TreeObject> references = getReferencedTreeObjects();
		if (!references.isEmpty()) {
			TreeObject commonTreeObject = TreeObject.getCommonTreeObject(references);
			if(commonTreeObject.equals(treeObject)){
				return true;
			}
		}
		return false;
	}
}
