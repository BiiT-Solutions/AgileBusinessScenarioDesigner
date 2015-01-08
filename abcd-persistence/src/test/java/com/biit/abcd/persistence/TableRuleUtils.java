package com.biit.abcd.persistence;

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
