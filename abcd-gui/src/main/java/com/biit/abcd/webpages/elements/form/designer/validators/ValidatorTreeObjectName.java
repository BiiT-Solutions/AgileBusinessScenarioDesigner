package com.biit.abcd.webpages.elements.form.designer.validators;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.util.regex.Pattern;

import com.vaadin.data.Validator;

public class ValidatorTreeObjectName implements Validator {
	private static final long serialVersionUID = 8591363723969902840L;

	private Pattern pattern;

	public ValidatorTreeObjectName(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!pattern.matcher((String) value).matches()) {
			throw new InvalidValueException("Text '" + value + "' is not valid.");
		}
	}

}
