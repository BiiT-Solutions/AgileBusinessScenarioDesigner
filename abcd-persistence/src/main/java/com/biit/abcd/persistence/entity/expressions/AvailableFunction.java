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

import com.biit.abcd.persistence.entity.AnswerFormat;

/**
 * Already defined functions. Used in JExVal.
 *
 */
public enum AvailableFunction {

	NOT("NOT("),

	MAX("MAX("),

	MIN("MIN("),

	ABS("ABS("),

	SQRT("SQRT("),

	IN("IN("),

	BETWEEN("BETWEEN("),

	ROUND("ROUND("),

	AVG("AVG("),

	IF("IF("),

	PMT("PMT("),
	
	LOG("LOG("),

	SUM("SUM("),
	
	CONCAT("CONCAT("),
	
	CONCAT_SEPARATOR("CONCAT_SEP("),
	
	ELEMENT_XPATH("ELEMENT_XPATH("),
	
	ELEMENT_PATH("ELEMENT_PATH("),
	
	ELEMENT_NAME("ELEMENT_NAME("),
	
	ELEMENT_ID("ELEMENT_ID(");

	private String value;

	private AvailableFunction(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static AvailableFunction get(String availableFunction) {
		for (AvailableFunction function : AvailableFunction.values()) {
			if (function.name().equalsIgnoreCase(availableFunction)) {
				return function;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}

}
