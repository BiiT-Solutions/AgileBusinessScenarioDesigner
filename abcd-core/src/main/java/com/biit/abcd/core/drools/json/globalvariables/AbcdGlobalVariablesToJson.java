package com.biit.abcd.core.drools.json.globalvariables;

import java.util.List;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataDate;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataNumber;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataPostalCode;
import com.biit.abcd.persistence.entity.globalvariables.VariableDataText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class takes a java class and transforms it to a json string<br>
 * Used to convert the global variables array to a json string and store it
 */
public class AbcdGlobalVariablesToJson {

	public static String toJson(List<GlobalVariable> globalVariables) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(GlobalVariable.class, new AbcdGlobalVariableSerializer());
		gsonBuilder.registerTypeAdapter(VariableDataText.class, new AbcdVariableDataTextSerializer());
		gsonBuilder.registerTypeAdapter(VariableDataPostalCode.class, new AbcdVariableDataPostalCodeSerializer());
		gsonBuilder.registerTypeAdapter(VariableDataNumber.class, new AbcdVariableDataNumberSerializer());
		gsonBuilder.registerTypeAdapter(VariableDataDate.class, new AbcdVariableDataDateSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(globalVariables);
	}
}
