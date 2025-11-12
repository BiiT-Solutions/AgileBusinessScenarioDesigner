package com.biit.abcd.core.drools.rules.validators;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import java.sql.Date;

/**
 * Enum that combines the answer type and answer format (Used in the expression
 * validator to check value types)
 *
 */
public enum ValueType {

	RADIO(String.class),

	MULTI_CHECKBOX(String.class),

	TEXT(String.class),

	NUMBER(Double.class),

	DATE(Date.class),

	POSTAL_CODE(String.class);

	// Used in the plug-in call validation
	// The reflected method needs to know the class of the parameter called
	private Class<?> classType;

	private ValueType(Class<?> classType) {
		this.classType = classType;
	}

	public Class<?> getClassType() {
		return classType;
	}
}
