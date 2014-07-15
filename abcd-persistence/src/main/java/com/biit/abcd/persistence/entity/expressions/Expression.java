package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Basic class for defining an expression. Any other expression must inherit from this class.
 * 
 */
@Entity
@Table(name = "EXPRESSION_BASIC")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Expression extends StorableObject {

	/**
	 * Returns a text representation of the Expression
	 * 
	 * @return
	 */
	public abstract String getRepresentation();

	/**
	 * Returns the expression in string format that can be evaluated by a Expression Evaluator.
	 * 
	 * @return
	 */
	protected abstract String getExpression();

	@Override
	public String toString() {
		return getExpression();
	}

	public abstract Expression generateCopy();
}
