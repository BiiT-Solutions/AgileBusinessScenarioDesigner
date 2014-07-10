package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
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

}
