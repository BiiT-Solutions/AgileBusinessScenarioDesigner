package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.persistence.entity.expressions.interfaces.IExpressionType;
import com.biit.abcd.serialization.expressions.ExpressionFunctionDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionFunctionSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * User for defining functions as MAX, MIN, AVERAGE, ABS, ...
 */
@Entity
@JsonDeserialize(using = ExpressionFunctionDeserializer.class)
@JsonSerialize(using = ExpressionFunctionSerializer.class)
@Table(name = "expression_function")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ExpressionFunction extends Expression implements IExpressionType<AvailableFunction> {
    private static final long serialVersionUID = -4646054850756194839L;

    @Enumerated(EnumType.STRING)
    @Column(name = "expression_value")
    private AvailableFunction value;

    public ExpressionFunction() {
    }

    public ExpressionFunction(AvailableFunction function) {
        value = function;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        if (value == null) {
            return "";
        } else {
            return value.getValue();
        }
    }

    @Override
    public AvailableFunction getValue() {
        return value;
    }

    @Override
    public void setValue(AvailableFunction function) {
        value = function;
    }

    @Override
    public String getExpression() {
        return value.getValue();
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionFunction) {
            super.copyData(object);
            ExpressionFunction expressionFunction = (ExpressionFunction) object;
            value = expressionFunction.getValue();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionFunction.");
        }
    }

}
