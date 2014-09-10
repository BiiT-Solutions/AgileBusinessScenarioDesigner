package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.form.TreeObject;

@Entity
@Table(name = "expression_value_tree_object_reference")
public class ExpressionValueTreeObjectReference extends ExpressionValue {

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject reference;

	@Enumerated(EnumType.STRING)
	private QuestionUnit unit = null;

	public ExpressionValueTreeObjectReference() {
		super();
	}

	public ExpressionValueTreeObjectReference(TreeObject reference) {
		super();
		this.reference = reference;
	}

	public ExpressionValueTreeObjectReference(TreeObject reference, boolean editable) {
		super();
		this.reference = reference;
		setEditable(editable);
	}

	public ExpressionValueTreeObjectReference(TreeObject reference, QuestionUnit unit) {
		super();
		this.reference = reference;
		this.unit = unit;
	}

	public ExpressionValueTreeObjectReference(TreeObject reference, QuestionUnit unit, boolean editable) {
		super();
		this.reference = reference;
		this.unit = unit;
		setEditable(editable);
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
			return reference + " (" + unit.getAbbreviature() + ")";
		}
		return "" + reference;
	}

	@Override
	protected String getExpression() {
		return "" + reference;
	}

	@Override
	public Expression generateCopy() {
		ExpressionValueTreeObjectReference copy = new ExpressionValueTreeObjectReference();
		copy.reference = reference;
		copy.unit = unit;
		copy.setEditable(isEditable());
		return copy;
	}

	@Override
	public Object getValue() {
		return getReference();
	}

	@Override
	public void setValue(Object value) throws NotValidExpressionValue {
		if (!(value instanceof TreeObject)) {
			throw new NotValidExpressionValue("Expected TreeObject object in '" + value + "'");
		}
		setReference((TreeObject) value);
	}

}
