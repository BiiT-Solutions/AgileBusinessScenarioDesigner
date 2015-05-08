package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Basic class for defining an expression. Any other expression must inherit from this class.
 * 
 */
@Entity
@Table(name = "expression_basic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class Expression extends StorableObject {
	private static final long serialVersionUID = 4816405922566823101L;

	@Transient
	private boolean editable;

	// For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
	// we cannot use the list of children with
	// @Orderby or @OrderColumn we use our own order manager.
	@Column(nullable = false)
	private long sortSeq = 0;

	public Expression() {
		super();
		editable = true;
	}

	public void copyBasicExpressionInfo(Expression expression) {
		setCreatedBy(expression.getCreatedBy());
		setCreationTime(expression.getCreationTime());
		setEditable(expression.isEditable());
		setSortSeq(expression.getSortSeq());
	}

	public final Expression generateCopy() {
		Expression copy = null;
		try {
			copy = this.getClass().newInstance();
			copy.copyData(this);
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return copy;
	}

	/**
	 * Returns the expression in string format that can be evaluated by a Expression Evaluator. Not allowed characters
	 * are ',', '.', ':', operators, ... that must filtered of the expression if necessary.
	 * 
	 * @return
	 */
	protected abstract String getExpression();

	/**
	 * Returns a text representation of the Expression
	 * 
	 * @return
	 */
	public abstract String getRepresentation();

	public long getSortSeq() {
		return sortSeq;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean isEditable) {
		this.editable = isEditable;
	}

	public void setSortSeq(long sortSeq) {
		this.sortSeq = sortSeq;
	}

	@Override
	public String toString() {
		return getExpression();
	}

	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Expression) {
			super.copyBasicInfo(object);
			Expression expression = (Expression) object;
			setEditable(expression.isEditable());
			setSortSeq(expression.getSortSeq());
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of Expression.");
		}
	}

	public abstract Object getValue();

}
