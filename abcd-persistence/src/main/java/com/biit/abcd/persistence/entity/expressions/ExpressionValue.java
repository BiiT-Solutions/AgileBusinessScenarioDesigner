package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a particular value in a expression.
 *
 */
@Entity
@Table(name = "expression_value")
public abstract class ExpressionValue extends Expression {

}
