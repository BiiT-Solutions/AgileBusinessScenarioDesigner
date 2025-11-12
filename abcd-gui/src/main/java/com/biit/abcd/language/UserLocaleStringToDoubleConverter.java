package com.biit.abcd.language;

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

import java.text.NumberFormat;
import java.util.Locale;

import com.biit.abcd.authentication.UserSessionHandler;
import com.vaadin.data.util.converter.StringToDoubleConverter;

public class UserLocaleStringToDoubleConverter extends StringToDoubleConverter {
	private static final long serialVersionUID = -4819509538529371129L;

	/**
	 * Locale is overridden by user option selected in Liferay.
	 */
	@Override
	protected NumberFormat getFormat(Locale locale) {
		locale = UserSessionHandler.getUser().getLocale();
		NumberFormat format = super.getFormat(locale);
		// TO override the limitation of three decimal digits for the doubles
		format.setMaximumFractionDigits(10);
		//Remove the separator for each three digits (i.e. 1.000.000). 
		format.setGroupingUsed(false);
		return format;
	}

}
