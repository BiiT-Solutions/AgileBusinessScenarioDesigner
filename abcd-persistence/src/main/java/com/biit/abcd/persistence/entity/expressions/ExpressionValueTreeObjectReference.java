package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.TreeObject;

@Entity
@Table(name = "EXPRESSION_VALUE_TREE_OBJECT_REFERENCE")
public class ExpressionValueTreeObjectReference extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject reference;

	public ExpressionValueTreeObjectReference() {
		super();
	}

	public ExpressionValueTreeObjectReference(TreeObject reference) {
		super();
		this.reference = reference;
	}

	public TreeObject getReference() {
		return reference;
	}

	public void setReference(TreeObject reference) {
		this.reference = reference;
	}

	@Override
	public String getExpression() {
		return "" + reference;
	}

}
