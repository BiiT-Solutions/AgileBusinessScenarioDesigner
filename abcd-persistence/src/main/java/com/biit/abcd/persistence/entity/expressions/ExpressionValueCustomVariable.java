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
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
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

    @Transient
    private transient String variableId;

    public ExpressionValueCustomVariable() {
        super();
    }

    @Override
    public void resetIds() {
        super.resetIds();
        // Custom variables cannot be reset, if they are, we duplicate it when storing into database.
    }

    public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable) {
        super();
        setReference(reference);
        setVariable(variable);
    }

    public ExpressionValueCustomVariable(TreeObject reference, CustomVariable variable, QuestionDateUnit dateUnit) {
        super();
        setReference(reference);
        setUnit(dateUnit);
        setVariable(variable);
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        String expressionString = "";
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
        this.variableId = (variable != null ? variable.getComparationId() : null);
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

    public String getVariableId() {
        return variableId;
    }

    public void setVariableId(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, variableId, getReference());
    }

    @Override
    public String toString() {
        if (getReference() != null) {
            return getReference().getName() + "." + (getVariable() != null ? getVariable().getName() : null);
        }
        return super.toString();
    }
}
