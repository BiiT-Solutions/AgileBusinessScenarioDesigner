package com.biit.abcd.persistence.entity.expressions;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Defines a drools rule.
 * 
 */
@Entity
@Table(name = "RULE")
public class Rule extends StorableObject {
	
	private String name;

	@OneToOne(fetch = FetchType.EAGER)
	private Expression condition;
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Expression> actions;

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public List<Expression> getActions() {
		return actions;
	}

	public void setActions(List<Expression> actions) {
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
