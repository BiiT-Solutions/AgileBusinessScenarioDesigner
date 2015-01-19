package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "expression_value_tree_object_reference")
public class ExpressionValueTreeObjectReference extends ExpressionValue<TreeObject> {
	private static final long serialVersionUID = 3933694492937877414L;

	@ManyToOne(fetch = FetchType.EAGER)
	private TreeObject reference;

	@Enumerated(EnumType.STRING)
	private QuestionDateUnit unit = null;

	public ExpressionValueTreeObjectReference() {
		super();
	}

	public ExpressionValueTreeObjectReference(TreeObject reference) {
		super();
		this.reference = reference;
	}

	public ExpressionValueTreeObjectReference(TreeObject reference, QuestionDateUnit unit) {
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

	public synchronized QuestionDateUnit getUnit() {
		return unit;
	}

	public synchronized void setUnit(QuestionDateUnit unit) {
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
		return "" + getReference();
	}

	@Override
	public TreeObject getValue() {
		return getReference();
	}

	@Override
	public void setValue(TreeObject value) {
		this.reference = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		if (reference != null) {
			innerStorableObjects.add(reference);
			innerStorableObjects.addAll(reference.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueTreeObjectReference) {
			super.copyData(object);
			ExpressionValueTreeObjectReference expressionValueTreeObjectReference = (ExpressionValueTreeObjectReference) object;
			setUnit(expressionValueTreeObjectReference.getUnit());
			// Rule tables can have empty expressions with null inside
			if (expressionValueTreeObjectReference.getValue() != null) {
				// Later the reference must be updated with current
				// TreeObject
				setValue(expressionValueTreeObjectReference.getValue());
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueTreeObjectReference.");
		}
	}

}
