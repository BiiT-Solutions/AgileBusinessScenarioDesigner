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

import com.biit.abcd.serialization.expressions.ExpressionValueBooleanDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueBooleanSerializer;
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
 * Defines boolean values.
 */
@Entity
@JsonDeserialize(using = ExpressionValueBooleanDeserializer.class)
@JsonSerialize(using = ExpressionValueBooleanSerializer.class)
@Table(name = "expression_value_boolean")
public class ExpressionValueBoolean extends ExpressionValue<Boolean> {
    private static final long serialVersionUID = 4438705376703075628L;

    @Column(name = "expression_value")
    private Boolean value;

    protected ExpressionValueBoolean() {
        super();
        value = true;
    }

    public ExpressionValueBoolean(Boolean value) {
        super();
        setValue(value);
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        return value.toString();
    }

    // public T getValue() {
    // return value;
    // }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public String getExpression() {
        if (value) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueBoolean) {
            super.copyData(object);
            ExpressionValueBoolean expressionValueBoolean = (ExpressionValueBoolean) object;
            this.setValue(expressionValueBoolean.getValue());
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionValueBoolean.");
        }
    }

}
