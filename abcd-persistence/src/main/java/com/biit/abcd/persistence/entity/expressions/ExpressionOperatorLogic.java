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

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.serialization.expressions.ExpressionOperatorLogicDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionOperatorLogicSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines any logical operator.
 */
@Entity
@JsonDeserialize(using = ExpressionOperatorLogicDeserializer.class)
@JsonSerialize(using = ExpressionOperatorLogicSerializer.class)
@Table(name = "expression_operator_logic")
public class ExpressionOperatorLogic extends ExpressionOperator {
    private static final long serialVersionUID = 5253554560944956269L;
    private static final List<AvailableOperator> ALLOWED_OPERATORS = new ArrayList<>(Arrays.asList(
            AvailableOperator.NULL, AvailableOperator.AND, AvailableOperator.OR, AvailableOperator.EQUALS,
            AvailableOperator.NOT_EQUALS, AvailableOperator.LESS_EQUALS, AvailableOperator.LESS_THAN,
            AvailableOperator.GREATER_EQUALS, AvailableOperator.GREATER_THAN));

    public ExpressionOperatorLogic() {
        super();
        try {
            setValue(AvailableOperator.NULL);
        } catch (NotValidOperatorInExpression e) {
            // This should never happen
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    public ExpressionOperatorLogic(AvailableOperator operator) {
        super();
        try {
            setValue(operator);
        } catch (NotValidOperatorInExpression e) {
            // This should never happen
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    @Override
    public List<AvailableOperator> getAcceptedValues() {
        return ALLOWED_OPERATORS;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionOperatorLogic) {
            super.copyData(object);
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionOperatorLogic.");
        }
    }

}
