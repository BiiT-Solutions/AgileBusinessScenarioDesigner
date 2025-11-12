package com.biit.abcd.webpages.elements.formula.editor;

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

import java.util.HashSet;
import java.util.Set;

public enum Type {

	VOID,
	
	COMPARISON,
	
	LOGIC,
	
	ASSIGNATION,
	
	CALCULATION,
	
	TREE_OBJECT_REFERENCE,
	
	VARIABLE,
	
	EXPRESSION;	

	public static Set<Type> getAnyType() {
		Set<Type> types = new HashSet<Type>();
		for (Type type : Type.values()) {
			types.add(type);
		}
		return types;
	}
	
	public static Set<Type> getComparisonAndLogic(){
		Set<Type> types = new HashSet<Type>();
		types.add(COMPARISON);
		types.add(LOGIC);
		return types;
	}
	
	public static Set<Type> getVoidAssignationCalculation(){
		Set<Type> types = new HashSet<Type>();
		types.add(VOID);
		types.add(ASSIGNATION);
		types.add(CALCULATION);
		return types;
	}
	
	public static Set<Type> getVoidComparisonLogicOrReference(){
		Set<Type> types = new HashSet<Type>();
		types.add(VOID);
		types.add(COMPARISON);
		types.add(LOGIC);
		types.add(TREE_OBJECT_REFERENCE);
		return types;
	}
}
