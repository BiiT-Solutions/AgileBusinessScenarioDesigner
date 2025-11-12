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

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpressionValue;
import com.biit.abcd.serialization.expressions.ExpressionValueNumberDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueNumberSerializer;
import com.biit.abcd.serialization.expressions.ExpressionValuePostalCodeDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValuePostalCodeSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Defines a value as postal code.
 *
 */
@Entity
@JsonDeserialize(using = ExpressionValuePostalCodeDeserializer.class)
@JsonSerialize(using = ExpressionValuePostalCodeSerializer.class)
@Table(name = "expression_value_postal_code")
public class ExpressionValuePostalCode extends ExpressionValueString {
	private static final long serialVersionUID = -4770567829915607298L;

	public ExpressionValuePostalCode() {
		super();
	}

	public ExpressionValuePostalCode(String value) throws NotValidExpressionValue {
		if (!value.matches(AbcdConfigurationReader.getInstance().getPostalCodeMask())) {
			throw new NotValidExpressionValue("Value '" + value + "' is not a valid postal code.");
		}
		setValue(value);
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValuePostalCode) {
			super.copyData(object);
			ExpressionValuePostalCode expressionValuePostalCode = (ExpressionValuePostalCode) object;
			this.setValue(expressionValuePostalCode.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of ExpressionValueString.");
		}
	}
}
