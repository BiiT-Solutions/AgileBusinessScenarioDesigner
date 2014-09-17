package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

/**
 * Basic class for defining an expression. Any other expression must inherit
 * from this class.
 * 
 */
@Entity
@Table(name = "expression_basic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Expression extends StorableObject {

	private transient boolean isEditable = true;

	// For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
	// we cannot use the list of children with
	// @Orderby or @OrderColumn we use our own order manager.
	@Column(nullable = false)
	private long sortSeq = 0;

	public void copy(Expression expression) {
		setCreatedBy(expression.getCreatedBy());
		setCreationTime(expression.getCreationTime());
		setEditable(expression.isEditable());
		setSortSeq(expression.getSortSeq());
	}

	public abstract Expression generateCopy();

	/**
	 * Returns the expression in string format that can be evaluated by a
	 * Expression Evaluator. Not allowed characters are ',', '.', ':',
	 * operators, ... that must filtered of the expression if necessary.
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
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setSortSeq(long sortSeq) {
		this.sortSeq = sortSeq;
	}

	@Override
	public String toString() {
		return getExpression();
	}
}
