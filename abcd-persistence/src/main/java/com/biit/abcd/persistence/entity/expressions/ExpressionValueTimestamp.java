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

import com.biit.abcd.serialization.expressions.ExpressionValueTimestampDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueTimestampSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.date.DateManager;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a value as a timestamp.
 */
@Entity
@JsonDeserialize(using = ExpressionValueTimestampDeserializer.class)
@JsonSerialize(using = ExpressionValueTimestampSerializer.class)
@Table(name = "expression_value_timestamp")
public class ExpressionValueTimestamp extends ExpressionValue<Timestamp> {
    private static final long serialVersionUID = -5688942686213064713L;
    private Timestamp value;
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    protected ExpressionValueTimestamp() {
        super();
    }

    public ExpressionValueTimestamp(String value) throws ParseException {
        super();
        Date date = getFormatter().parse(value);
        setValue(new Timestamp(date.getTime()));
    }

    public ExpressionValueTimestamp(Timestamp value) {
        super();
        setValue(value);
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        if (value != null) {
            return DateManager.convertDateToString(value);
        } else {
            return "";
        }
    }

    @Override
    public Timestamp getValue() {
        return value;
    }

    @Override
    public void setValue(Timestamp value) {
        this.value = value;
    }

    @Override
    public String getExpression() {
        if (value != null) {
            return DateManager.convertDateToString(value);
        } else {
            return "";
        }
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueTimestamp) {
            super.copyData(object);
            ExpressionValueTimestamp expressionValueTimestamp = (ExpressionValueTimestamp) object;
            this.setValue(expressionValueTimestamp.getValue());
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueTimestamp.");
        }
    }

    public static SimpleDateFormat getFormatter() {
        return new SimpleDateFormat(DATE_FORMAT);
    }
}
