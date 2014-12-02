package com.biit.abcd.core.drools.json.globalvariables;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	/**
	 * Reads the global variable from a file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<GlobalVariable> importGlobalVariables(String file) throws IOException {
		Path newFile = Paths.get(file);
		try (BufferedReader reader = Files.newBufferedReader(newFile, Charset.defaultCharset())) {
			StringBuilder globalVariablesJson = new StringBuilder();
			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				globalVariablesJson.append(lineFromFile);
			}
			return convertJsonToGlobalVariableList(globalVariablesJson.toString());
		}
	}
}
