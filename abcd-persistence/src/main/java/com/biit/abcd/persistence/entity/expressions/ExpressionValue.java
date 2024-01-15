package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.serialization.expressions.ExpressionValueDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Defines a particular value in a expression.
 */
@Entity
@JsonDeserialize(using = ExpressionValueDeserializer.class)
@JsonSerialize(using = ExpressionValueSerializer.class)
@Table(name = "expression_value")
public abstract class ExpressionValue<T> extends Expression {
    private static final long serialVersionUID = -3969530622756101122L;

    public abstract T getValue();

    public abstract void setValue(T value);
}
