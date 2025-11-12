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

import com.biit.abcd.serialization.expressions.ExpressionValueNumberDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueNumberSerializer;
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
 * Defines values as a double.
 *
 */
@Entity
@JsonDeserialize(using = ExpressionValueNumberDeserializer.class)
@JsonSerialize(using = ExpressionValueNumberSerializer.class)
@Table(name = "expression_value_number")
public class ExpressionValueNumber extends ExpressionValue<Double> {
	private static final long serialVersionUID = -8379130602709661373L;

	@Column(name = "expression_value")
	private Double value;

	public ExpressionValueNumber() {
		super();
		value = 0d;
	}

	public ExpressionValueNumber(Double value) {
		super();
		setValue(value);
	}

	@Override
	public String getRepresentation(boolean showWhiteCharacter) {
		return getValueWithoutTrailingZeroes();
	}

	@Override
	public Double getValue() {
		return new Double(value);
	}

	@Override
	public String getExpression() {
		return getValueWithoutTrailingZeroes();
	}

	public String getValueWithoutTrailingZeroes() {
		Double convertedValue = new Double(value);
		return convertedValue.toString().indexOf(".") < 0 ? convertedValue.toString() : convertedValue.toString().replaceAll("0*$", "").replaceAll("\\.$", "");
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof ExpressionValueNumber) {
			super.copyData(object);
			ExpressionValueNumber expressionValueNumber = (ExpressionValueNumber) object;
			this.setValue(expressionValueNumber.getValue());
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of ExpressionValueNumber.");
		}
	}

}
