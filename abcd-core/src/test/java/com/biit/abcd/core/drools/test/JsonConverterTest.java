package com.biit.abcd.core.drools.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.json.globalvariables.JSonConverter;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class JsonConverterTest {

	private List<GlobalVariable> globalVarList = null;

	// Simple table question answer
	@Test(groups = { "gson" })
	public void testJsonConverter() throws NotValidTypeInVariableData, FieldTooLongException {
		createGlobalVariables();
		String globalVariablesJson = JSonConverter.convertGlobalVariableListToJson(globalVarList);
		List<GlobalVariable> jsonGlobalVariablesList = JSonConverter
				.convertJsonToGlobalVariableList(globalVariablesJson);

		Assert.assertEquals(jsonGlobalVariablesList.size(), 4);
		Assert.assertEquals(jsonGlobalVariablesList.get(2).getName(), "TestPC");
		Assert.assertEquals(jsonGlobalVariablesList.get(0).getVariableData().get(1).getValue(), 21.0);
	}

	private void createGlobalVariables() throws NotValidTypeInVariableData, FieldTooLongException {
		globalVarList = new ArrayList<GlobalVariable>();
		Timestamp validFrom = Timestamp.valueOf("2007-09-23 0:0:0.0");
		Timestamp validFromFuture = Timestamp.valueOf("2016-09-23 0:0:0.0");
		Timestamp validToPast = Timestamp.valueOf("2008-09-23 0:0:0.0");
		Timestamp validToFuture = Timestamp.valueOf("2018-09-23 0:0:0.0");

		// Should get the second value
		GlobalVariable globalVariableNumber = new GlobalVariable(AnswerFormat.NUMBER);
		globalVariableNumber.setName("IVA");
		globalVariableNumber.addVariableData(19.0, validFrom, validToPast);
		globalVariableNumber.addVariableData(21.0, validToPast, null);
		// Should not represent this constant
		GlobalVariable globalVariableText = new GlobalVariable(AnswerFormat.TEXT);
		globalVariableText.setName("TestText");
		globalVariableText.addVariableData("Hello", validFromFuture, validToFuture);
		// Should get the value
		GlobalVariable globalVariablePostalCode = new GlobalVariable(AnswerFormat.POSTAL_CODE);
		globalVariablePostalCode.setName("TestPC");
		globalVariablePostalCode.addVariableData("Postal", validFrom, validToFuture);
		// Should enter a valid date as constant
		GlobalVariable globalVariableDate = new GlobalVariable(AnswerFormat.DATE);
		globalVariableDate.setName("TestDate");
		globalVariableDate.addVariableData(new Date(), validFrom, validToFuture);

		globalVarList.add(globalVariableNumber);
		globalVarList.add(globalVariableText);
		globalVarList.add(globalVariablePostalCode);
		globalVarList.add(globalVariableDate);
	}
}
