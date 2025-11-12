package com.biit.abcd.persistence.entity.expressions;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.serialization.expressions.ExpressionValueGenericCustomVariableDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueGenericCustomVariableSerializer;
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
import java.util.Set;

/**
 * Defines a value as a already defined form custom variable.
 */
@Entity
@JsonDeserialize(using = ExpressionValueGenericCustomVariableDeserializer.class)
@JsonSerialize(using = ExpressionValueGenericCustomVariableSerializer.class)
@Table(name = "expression_value_generic_custom_variable")
public class ExpressionValueGenericCustomVariable extends ExpressionValueGenericVariable {
    private static final long serialVersionUID = -5189487388656499107L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "custom_variable")
    private CustomVariable variable;

    @Transient
    private transient String variableId;

    public ExpressionValueGenericCustomVariable() {
        super();
    }

    public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable) {
        super();
        setType(type);
        setVariable(variable);
    }

    public ExpressionValueGenericCustomVariable(GenericTreeObjectType type, CustomVariable variable, boolean editable) {
        super();
        setType(type);
        setVariable(variable);
        setEditable(editable);
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (getVariable() != null) {
            getVariable().resetIds();
        }
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        String expressionString = new String();
        if ((getType() != null) && (getVariable() != null)) {
            expressionString += getType().getExpressionName() + "." + getVariable().getName();
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
        if ((getType() != null) && (getVariable() != null)) {
            expressionString += getType().getExpressionName().replace(".", "_") + "_" + getVariable().getName();
        }
        return expressionString;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (getVariable() != null) {
            innerStorableObjects.add(getVariable());
            innerStorableObjects.addAll(getVariable().getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueGenericCustomVariable) {
            super.copyData(object);
            ExpressionValueGenericCustomVariable expressionValueGenericCustomVariable = (ExpressionValueGenericCustomVariable) object;
            this.setVariable(expressionValueGenericCustomVariable.getVariable());
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionValueGenericCustomVariable.");
        }
    }


    public String getVariableId() {
        return variableId;
    }

    public void setVariableId(String variableId) {
        this.variableId = variableId;
    }
}
