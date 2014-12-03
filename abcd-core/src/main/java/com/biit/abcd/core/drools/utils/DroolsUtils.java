package com.biit.abcd.core.drools.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biit.abcd.core.drools.DroolsGlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

public class DroolsUtils {

	/**
	 * Receives a list of global variables and returns a list of drools global variables.
	 * @param globalVariables
	 * @return
	 */
	public static List<DroolsGlobalVariable> calculateDroolsGlobalVariables(List<GlobalVariable> globalVariables) {
		List<DroolsGlobalVariable> droolsGlobalVariables = new ArrayList<DroolsGlobalVariable>();
		if ((globalVariables != null) && !globalVariables.isEmpty()) {
			for (GlobalVariable globalVariable : globalVariables) {
				// First check if the data inside the variable has a valid date
				List<VariableData> varDataList = globalVariable.getVariableData();
				if ((varDataList != null) && !varDataList.isEmpty()) {
					for (VariableData variableData : varDataList) {
						Timestamp currentTime = new Timestamp(new Date().getTime());
						Timestamp initTime = variableData.getValidFrom();
						Timestamp endTime = variableData.getValidTo();
						// Sometimes endtime can be null, meaning that the
						// variable data has no ending time
						if ((currentTime.after(initTime) && (endTime == null))
								|| (currentTime.after(initTime) && currentTime.before(endTime))) {
							droolsGlobalVariables.add(new DroolsGlobalVariable(globalVariable.getName(), globalVariable
									.getFormat(), variableData.getValue()));
							break;
						}
					}
				}
			}
		}
		return droolsGlobalVariables;
	}

}
