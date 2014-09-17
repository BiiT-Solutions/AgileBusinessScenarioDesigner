package com.biit.abcd.core.drools.json.globalvariables;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class takes a java class and transforms it to a json string<br>
 * Used to convert the global variables array to a json string and store it
 */
public class JSonConverter {

	public static String convertGlobalVariableListToJson(List<GlobalVariable> globalVariables) {
		// create the gson object
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(globalVariables);
	}

	public static List<GlobalVariable> convertJsonToGlobalVariableList(String json) {
		// create the gson object with the specific deserializer for the
		// variable data class
		Gson gson = new GsonBuilder().registerTypeAdapter(VariableData.class, new VariableDataDeserializer()).create();
		GlobalVariable[] globalVariablesArray = gson.fromJson(json, GlobalVariable[].class);
		List<GlobalVariable> globalVariablesList = Arrays.asList(globalVariablesArray);
		return globalVariablesList;
	}
}
