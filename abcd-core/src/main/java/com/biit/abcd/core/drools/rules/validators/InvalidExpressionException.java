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

import com.biit.abcd.persistence.entity.expressions.ExpressionValue;

/**
 * Exception created for the expression validator
 *
 */
public class InvalidExpressionException extends Exception {
	private static final long serialVersionUID = -9031196357941801998L;

	private ExpressionValue<?> expressionValue = null;
	private String description = "";

	public InvalidExpressionException(String message, ExpressionValue<?> expressionValue) {
		super(message);
		this.expressionValue = expressionValue;
		this.description = message;
	}

	public InvalidExpressionException(String message) {
		this(message, (ExpressionValue<?>) null);
	}

	public InvalidExpressionException(String message, Exception e) {
		super(message, e);
		this.description = message;
	}

	public ExpressionValue<?> getExpressionValue() {
		return expressionValue;
	}

	public String getDescription() {
		return description;
	}
}
