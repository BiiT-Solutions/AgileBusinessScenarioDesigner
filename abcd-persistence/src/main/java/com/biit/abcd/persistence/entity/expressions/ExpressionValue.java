package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;

/**
 * Defines a particular value in a expression.
 * 
 */
@Entity
@Table(name = "expression_value")
public abstract class ExpressionValue extends Expression {

	public abstract Object getValue();

	public abstract void setValue(Object value) throws NotValidExpressionValue;
}
