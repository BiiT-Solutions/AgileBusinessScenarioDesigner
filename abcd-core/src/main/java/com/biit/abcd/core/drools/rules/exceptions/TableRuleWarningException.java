package com.biit.abcd.core.drools.rules.exceptions;

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
