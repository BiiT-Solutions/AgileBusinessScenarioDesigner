package com.biit.abcd.core.drools.rules;

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
