package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.serialization.expressions.ExpressionValueGenericVariableDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueGenericVariableSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = ExpressionValueGenericVariableDeserializer.class)
@JsonSerialize(using = ExpressionValueGenericVariableSerializer.class)
@Table(name = "expression_value_generic_variable")
public class ExpressionValueGenericVariable extends ExpressionValue<GenericTreeObjectType> {
    private static final long serialVersionUID = -559535098261955545L;
    @Enumerated(EnumType.STRING)
    private GenericTreeObjectType type;

    public ExpressionValueGenericVariable() {
        super();
    }

    public ExpressionValueGenericVariable(GenericTreeObjectType variable) {
        super();
        setType(variable);
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        String expressionString = new String();
        if ((getType() != null)) {
            expressionString += getType().getExpressionName();
        }
        return expressionString;
    }

    public GenericTreeObjectType getType() {
        return type;
    }

    public void setType(GenericTreeObjectType type) {
        this.type = type;
    }

    @Override
    protected String getExpression() {
        String expressionString = new String();
        if (getType() != null) {
            expressionString += getType().getExpressionName().replace(".", "_");
        }
        return expressionString;
    }

    @Override
    public GenericTreeObjectType getValue() {
        return getType();
    }

    @Override
    public void setValue(GenericTreeObjectType value) {
        setType((GenericTreeObjectType) value);
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueGenericVariable) {
            super.copyData(object);
            ExpressionValueGenericVariable expressionValueGenericVariable = (ExpressionValueGenericVariable) object;
            setType(expressionValueGenericVariable.getType());
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueGenericVariable.");
        }
    }

}
