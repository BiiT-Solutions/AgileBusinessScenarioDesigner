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

import com.biit.abcd.serialization.expressions.ExpressionValueSystemDateDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueSystemDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Defines current time when used in a rule.
 */
@Entity
@JsonDeserialize(using = ExpressionValueSystemDateDeserializer.class)
@JsonSerialize(using = ExpressionValueSystemDateSerializer.class)
@Table(name = "expression_value_systemdate")
public class ExpressionValueSystemDate extends ExpressionValueTimestamp {
    private static final long serialVersionUID = -8660891361751270777L;

    public ExpressionValueSystemDate() {
        super();
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        return "SystemDate";
    }

    @Override
    public Timestamp getValue() {
        return new Timestamp(System.currentTimeMillis());

    }

    @Override
    public String getExpression() {
        return "SystemDate";
    }
}
