package com.biit.abcd.core.drools.rules;

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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.biit.form.entity.TreeObject;

public class TreeObjectDroolsIdMap {
	
	public static final ConcurrentMap<TreeObject, String> treeObjectDroolsIdMap = new ConcurrentHashMap<TreeObject, String>();

	public static void clearMap() {
		treeObjectDroolsIdMap.clear();
	}

	public static void put(TreeObject treeObject, String droolsId) {
		treeObjectDroolsIdMap.put(treeObject, droolsId);
	}

	public static String get(TreeObject treeObject) {
		return treeObjectDroolsIdMap.get(treeObject);
	}

	public static boolean containsKey(TreeObject treeObject) {
		return treeObjectDroolsIdMap.containsKey(treeObject);
	}

	public static boolean containsValue(String droolsId) {
		return treeObjectDroolsIdMap.containsValue(droolsId);
	}
}
