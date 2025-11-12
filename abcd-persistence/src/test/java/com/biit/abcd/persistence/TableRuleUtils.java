package com.biit.abcd.persistence;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public class TableRuleUtils {

	public static List<TableRuleRow> copyTableRuleRows(final TableRule origin, Collection<TableRuleRow> rowsToCopy) {
		List<TableRuleRow> copiedRows;
		List<TableRuleRow> listOfRowsToCopy = new ArrayList<TableRuleRow>(rowsToCopy);
		Collections.sort(listOfRowsToCopy, new Comparator<TableRuleRow>() {
			@Override
			public int compare(TableRuleRow arg0, TableRuleRow arg1) {
				Integer rule0 = origin.getRules().indexOf(arg0);
				Integer rule1 = origin.getRules().indexOf(arg1);
				return rule0.compareTo(rule1);
			}
		});
		copiedRows = new ArrayList<TableRuleRow>();
		for (TableRuleRow rowToCopy : listOfRowsToCopy) {
			TableRuleRow copiedRow = rowToCopy.generateCopy();
			copiedRow.resetIds();
			copiedRows.add(copiedRow);
		}
		return copiedRows;
	}

	public static void pasteTableRuleRows(TableRule selectedTableRule, List<TableRuleRow> copiedRows) {
		if ((copiedRows == null) || copiedRows.isEmpty()) {
			return;
		}
		List<TableRuleRow> rowsToPaste = getNewInstanceOfCopiedElements(copiedRows);
		for (TableRuleRow rowToPaste : rowsToPaste) {
			selectedTableRule.addRow(rowToPaste);
		}
	}

	private static List<TableRuleRow> getNewInstanceOfCopiedElements(List<TableRuleRow> copiedRows) {
		List<TableRuleRow> newCopiedRows = new ArrayList<TableRuleRow>();
		for (TableRuleRow row : copiedRows) {
			TableRuleRow copiedRow = row.generateCopy();
			copiedRow.resetIds();
			newCopiedRows.add(copiedRow);
		}
		return newCopiedRows;
	}
}
