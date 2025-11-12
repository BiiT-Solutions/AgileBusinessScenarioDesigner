package com.biit.abcd.core.drools.rules.exceptions;

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

import java.util.List;

public class TableRuleWarningException extends Exception {
	private static final long serialVersionUID = 7401211046559148275L;
	private String tableRuleName;
	private List<Integer> invalidRows;

	public TableRuleWarningException(String message, String tableRuleName, List<Integer> invalidRows) {
		super(message);
		this.tableRuleName = tableRuleName;
		this.invalidRows = invalidRows;
	}

	public String getTableRuleName() {
		return tableRuleName;
	}

	public String getInvalidRows() {
		String invalidRowsString = "";
		for (Integer rowIndex : invalidRows) {
			invalidRowsString += rowIndex + ",";
		}
		return invalidRowsString.substring(0, invalidRowsString.length() - 1);
	}
}
