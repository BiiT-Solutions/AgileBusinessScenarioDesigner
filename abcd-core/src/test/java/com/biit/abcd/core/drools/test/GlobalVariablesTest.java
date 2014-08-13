package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.biit.abcd.core.drools.Form2DroolsNoDrl;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.NotValidChildException;

/**
 * Tests the correct creation of the global variables <br>
 * It also test the introduction of the variables in the drools engine
 */
public class GlobalVariablesTest {

	@Test(groups = { "rules" })
	public void testGlobVarsInDroolsEngine() throws ExpressionInvalidException, RuleInvalidException, FieldTooLongException, NotValidTypeInVariableData, NotValidChildException  {
		Form2DroolsNoDrl formDrools = new Form2DroolsNoDrl();
		formDrools.parse(this.createBasicForm(), this.createGlobalvariables());
		// Empty form to force the engine to load the global variables values
		formDrools.go(new SubmittedForm("", ""));
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private Form createBasicForm() throws FieldTooLongException, NotValidChildException{
		// The form needs at least one child to be executed in the tests
		Form form = new Form("testForm");
		form.addChild(new Category("testCategory"));
		return form;
	}

	private List<GlobalVariable> createGlobalvariables() throws NotValidTypeInVariableData{
		List<GlobalVariable> globalVarList = new ArrayList<GlobalVariable>();
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

		return globalVarList;
	}
}
