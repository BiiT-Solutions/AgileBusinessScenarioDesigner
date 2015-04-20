package com.biit.abcd.core.drools.utils;

import java.util.List;

import com.biit.abcd.core.drools.json.globalvariables.AbcdGlobalVariablesToJson;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.global.variables.DroolsGlobalVariable;
import com.biit.drools.global.variables.json.DroolsGlobalVariablesFromJson;

public class AbcdDroolsUtils {

	public static List<DroolsGlobalVariable> convertGlobalVariablesToDroolsGlobalVariables(List<GlobalVariable> globalVariables) {
		// We use the json serializer/deserializer to transform the variables
		return DroolsGlobalVariablesFromJson.fromJson(AbcdGlobalVariablesToJson.toJson(globalVariables));
	}

}
