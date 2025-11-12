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

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.serialization.expressions.ExpressionValueGlobalVariableDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueGlobalVariableSerializer;
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
 * Defines a value as a already defined Global Constant.
 */
@Entity
@JsonDeserialize(using = ExpressionValueGlobalVariableDeserializer.class)
@JsonSerialize(using = ExpressionValueGlobalVariableSerializer.class)
@Table(name = "expression_value_global_variable")
public class ExpressionValueGlobalVariable extends ExpressionValue<GlobalVariable> {
    private static final long serialVersionUID = 3063006330916018596L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "global_variable")
    private GlobalVariable globalVariable;

    protected ExpressionValueGlobalVariable() {
        super();
    }

    public ExpressionValueGlobalVariable(GlobalVariable globalVariable) {
        super();
        this.globalVariable = globalVariable;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        String expressionString = "";
        if (globalVariable != null) {
            expressionString += globalVariable.getName();
        }
        return expressionString;
    }

    public void setVariable(GlobalVariable variable) {
        globalVariable = variable;
    }

    @Override
    protected String getExpression() {
        return getRepresentation(false);
    }

    @Override
    public GlobalVariable getValue() {
        return globalVariable;
    }

    @Override
    public void setValue(GlobalVariable globalVariable) {
        this.globalVariable = globalVariable;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (globalVariable != null) {
            innerStorableObjects.add(globalVariable);
            innerStorableObjects.addAll(globalVariable.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueGlobalVariable) {
            super.copyData(object);
            ExpressionValueGlobalVariable expressionValueGlobalConstant = (ExpressionValueGlobalVariable) object;
            this.setValue(expressionValueGlobalConstant.getValue());
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionValueGlobalConstant.");
        }
    }
}
