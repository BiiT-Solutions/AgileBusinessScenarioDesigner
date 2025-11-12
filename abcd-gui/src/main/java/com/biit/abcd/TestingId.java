package com.biit.abcd;

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
