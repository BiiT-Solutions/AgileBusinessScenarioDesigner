package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.serialization.expressions.ExpressionValueSystemDateDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueSystemDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Defines current time when used in a rule.
 */
@Entity
@JsonDeserialize(using = ExpressionValueSystemDateDeserializer.class)
@JsonSerialize(using = ExpressionValueSystemDateSerializer.class)
@Table(name = "expression_value_systemdate")
public class ExpressionValueSystemDate extends ExpressionValueTimestamp {
    private static final long serialVersionUID = -8660891361751270777L;

    public ExpressionValueSystemDate() {
        super();
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        return "SystemDate";
    }

    @Override
    public Timestamp getValue() {
        return new Timestamp(System.currentTimeMillis());

    }

    @Override
    public String getExpression() {
        return "SystemDate";
    }
}
