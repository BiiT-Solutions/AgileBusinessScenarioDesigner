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

import com.biit.abcd.serialization.expressions.ExpressionValueStringDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueStringSerializer;
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
 * Defines a value as string.
 */
@Entity
@JsonDeserialize(using = ExpressionValueStringDeserializer.class)
@JsonSerialize(using = ExpressionValueStringSerializer.class)
@Table(name = "expression_value_string")
public class ExpressionValueString extends ExpressionValue<String> {
    private static final long serialVersionUID = -6973952458832840106L;
    @Column(columnDefinition = "TEXT")
    private String text = "";

    public ExpressionValueString() {
        super();
    }

    public ExpressionValueString(String value) {
        super();
        setValue(value);
    }

    public String getValue() {
        return text;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        return getValue();
    }

    @Override
    protected String getExpression() {
        return getValue();
    }

    @Override
    public void setValue(String value) {
        this.text = value;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueString) {
            super.copyData(object);
            ExpressionValueString expressionValueString = (ExpressionValueString) object;
            this.setValue(expressionValueString.getValue());
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueString.");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
