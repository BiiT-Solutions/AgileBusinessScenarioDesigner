package com.biit.abcd.persistence.utils;

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

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;

/**
 * Compares two forms. Must be equals (but with different IDs and ComparationIds).
 */
public class FormVersionComparator extends FormComparator {

	public FormVersionComparator(boolean checkIds) {
		super(checkIds);
	}

	@Override
	protected void compareVersions(Form form1, Form form2) throws FormNotEqualsException {
		if (form1.getVersion() + 1 != (int) form2.getVersion()) {
			throw new FormNotEqualsException("Form's versions are incorrect!");
		}
	}

	@Override
	protected void compareFormDates(Form form1, Form form2) throws FormNotEqualsException {
		// Previous version ends when the other starts, unless finish the same day.
		if ((form1.getAvailableTo() == null && form2.getAvailableFrom() != null)
				|| (form1.getAvailableTo() != null && form2.getAvailableFrom() == null)
				|| ((form1.getAvailableTo() != null && form2.getAvailableFrom() != null) && form1.getAvailableTo()
						.getTime() < form2.getAvailableFrom().getTime())) {
			throw new FormNotEqualsException("Form's validTo and ValidFrom are incorrect!");
		}
	}

}
