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

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.expressions.interfaces.IExpressionType;
import com.biit.abcd.serialization.expressions.ExpressionOperatorDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionOperatorSerializer;
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
import java.util.List;

/**
 * Generic class for creating operators logical and mathematical.
 */
@Entity
@JsonDeserialize(using = ExpressionOperatorDeserializer.class)
@JsonSerialize(using = ExpressionOperatorSerializer.class)
@Table(name = "expression_operator")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExpressionOperator extends Expression implements IExpressionType<AvailableOperator> {
    private static final long serialVersionUID = 122060488311950177L;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_value")
    private AvailableOperator currentValue;

    public ExpressionOperator() {
        super();
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        if ((currentValue == null) || (currentValue == AvailableOperator.NULL)) {
            return " ";
        } else {
            return " " + currentValue.getCaption() + " ";
        }
    }

    public abstract List<AvailableOperator> getAcceptedValues();

    @Override
    public AvailableOperator getValue() {
        return currentValue;
    }

    /**
     * Set a value.
     *
     * @param exprOpvalue the value.
     * @throws NotValidOperatorInExpression If this exception is launched, check
     *                                      ALLOWED_OPERATORS of the class.
     */
    @Override
    public void setValue(AvailableOperator exprOpvalue) throws NotValidOperatorInExpression {
        if (getAcceptedValues().contains(exprOpvalue)) {
            currentValue = exprOpvalue;
        } else {
            throw new NotValidOperatorInExpression(
                    "The operator '" + exprOpvalue + "' is not allowed in this expression.");
        }
    }

    @Override
    public String getExpression() {
        return currentValue.getValue();
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionOperator) {
            super.copyData(object);
            currentValue = ((ExpressionOperator) object).getValue();
        } else {
            throw new NotValidStorableObjectException(
                    "Object '" + object + "' is not an instance of ExpressionOperator.");
        }
    }
}
