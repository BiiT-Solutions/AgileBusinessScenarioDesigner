package com.biit.abcd.persistence.entity.expressions;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "expression_value_tree_object_reference")
public class ExpressionValueTreeObjectReference extends ExpressionValue {

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

	@Override
	public void resetIds() {
		super.resetIds();
		// if (reference != null) {
		// reference.resetIds();
		// }
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
			try {
				this.setUnit(expressionValueTreeObjectReference.getUnit());
				this.setValue(expressionValueTreeObjectReference.getValue());
			} catch (NotValidExpressionValue e) {
				throw new NotValidStorableObjectException("Object '" + object
						+ "' is not a valid instance of ExpressionValueTreeObjectReference.");
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueTreeObjectReference.");
		}
	}

}
