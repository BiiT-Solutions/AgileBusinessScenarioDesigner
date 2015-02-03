package com.biit.abcd;

public enum TestingId {

	DIAGRAM_TABLE("diagram-table"),
	EXPRESSION_TABLE("expression-table"),
	RULE_TABLE("rule-table"),
	RULE_TABLES_TABLE("rule-tables-table"),
	;
	
	private final String value;
	
	TestingId(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
