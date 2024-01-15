package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.serialization.expressions.ExpressionValueStringDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueStringSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a value as string.
 */
@Entity
@JsonDeserialize(using = ExpressionValueStringDeserializer.class)
@JsonSerialize(using = ExpressionValueStringSerializer.class)
@Table(name = "expression_value_string")
public class ExpressionValueString extends ExpressionValue<String> {
    private static final long serialVersionUID = -6973952458832840106L;
    @Column(columnDefinition = "TEXT")
    private String text = "";

    protected ExpressionValueString() {
        super();
    }

    public ExpressionValueString(String value) {
        super();
        setValue(value);
    }

    public String getValue() {
        return text;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        return getValue();
    }

    @Override
    protected String getExpression() {
        return getValue();
    }

    @Override
    public void setValue(String value) {
        this.text = value;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueString) {
            super.copyData(object);
            ExpressionValueString expressionValueString = (ExpressionValueString) object;
            this.setValue(expressionValueString.getValue());
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueString.");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
