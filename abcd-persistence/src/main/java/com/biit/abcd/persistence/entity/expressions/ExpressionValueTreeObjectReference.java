package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.TreeObject;

@Entity
@Table(name = "EXPRESSION_VALUE_TREE_OBJECT_REFERENCE")
public class ExpressionValueTreeObjectReference extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject reference;

	@Enumerated(EnumType.STRING)
	private QuestionUnit unit;

	public ExpressionValueTreeObjectReference() {
		super();
	}

	public ExpressionValueTreeObjectReference(TreeObject reference) {
		super();
		this.reference = reference;
	}

	public ExpressionValueTreeObjectReference(TreeObject reference, QuestionUnit unit) {
		super();
		this.reference = reference;
		this.unit = unit;
	}

	public TreeObject getReference() {
		return reference;
	}

	public void setReference(TreeObject reference) {
		this.reference = reference;
	}

	public synchronized QuestionUnit getUnit() {
		return unit;
	}

	public synchronized void setUnit(QuestionUnit unit) {
		this.unit = unit;
	}

	@Override
	public String getRepresentation() {
		if (unit != null) {
			return reference.toString() + " (" + unit.getAbbreviature() + ")";
		}
		return reference.toString();
	}

	@Override
	protected String getExpression() {
		return reference.toString();
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueTreeObjectReference copy = new ExpressionValueTreeObjectReference();
		copy.reference = reference;
		copy.unit = unit;
		return copy;
	}

}
