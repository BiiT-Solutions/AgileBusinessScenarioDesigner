package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.serialization.expressions.ExpressionValueCustomVariableDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueCustomVariableSerializer;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a value as a already defined form custom variable.
 */
@Entity
@JsonDeserialize(using = ExpressionValueCustomVariableDeserializer.class)
@JsonSerialize(using = ExpressionValueCustomVariableSerializer.class)
@Table(name = "expression_value_custom_variable")
public class ExpressionValueCustomVariable extends ExpressionValueTreeObjectReference {
    private static final long serialVersionUID = -5934937557607551025L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_variable")
    private CustomVariable variable;

    public ExpressionValueCustomVariable() {
        super();
    }

    @Override
    public void resetIds() {
        super.resetIds();
        // Custom variables cannot be reseted, if they are, we duplicate it when storing into database.
    }

    public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable) {
        super();
        setReference(reference);
        this.variable = variable;
    }

    public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable, QuestionDateUnit dateUnit) {
        super();
        setReference(reference);
        setUnit(dateUnit);
        this.variable = variable;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        String expressionString = new String();
        if ((getReference() != null) && (variable != null)) {
            expressionString += getReference().getName();
            if (getUnit() != null) {
                expressionString += "." + variable.getName() + " (" + getUnit().getAbbreviature() + ")";
            } else {
                expressionString += "." + variable.getName();
            }
        }
        return expressionString;
    }

    public CustomVariable getVariable() {
        return variable;
    }

    public void setVariable(CustomVariable variable) {
        this.variable = variable;
    }

    @Override
    protected String getExpression() {
        String expressionString = new String();
        if (getReference() != null) {
            expressionString += getReference().getName();
            if (variable != null) {
                expressionString += "." + variable.getName();
            }
        }
        return expressionString;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (variable != null) {
            innerStorableObjects.add(variable);
            innerStorableObjects.addAll(variable.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueCustomVariable) {
            super.copyData(object);
            ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) object;
            this.setVariable(expressionValueCustomVariable.getVariable());
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueCustomVariable.");
        }
    }

}
