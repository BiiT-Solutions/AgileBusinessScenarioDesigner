package com.biit.abcd.persistence.entity.expressions;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a particular value in a expression. 
 *
 */
@Entity
@Table(name = "EXPRESSION_VALUE")
public abstract class ExpressionValue extends Expression {

}
