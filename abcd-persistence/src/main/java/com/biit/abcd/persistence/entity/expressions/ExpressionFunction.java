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
