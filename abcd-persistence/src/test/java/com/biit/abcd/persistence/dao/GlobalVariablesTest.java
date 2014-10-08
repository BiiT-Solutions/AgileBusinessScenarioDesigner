package com.biit.abcd.persistence.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "globalVariablesDao" })
public class GlobalVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String BASIC_GLOBAL_VARIABLE_NAME = "Variable";
	private static final String BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME = "Variable with data";
	private static final String VARIABLE_DATA_VALUE_1 = "Value1";
	private static final String VARIABLE_DATA_VALUE_2 = "Value2";
	private static final Double VARIABLE_DATA_VALUE_3 = 3.7;
	private static final Double VARIABLE_DATA_VALUE_4 = 46.2;
	private static final Timestamp VARIABLE_DATA_VALUE_5 = new Timestamp((new Date().getTime() / 1000) * 1000);
	private static final Timestamp VARIABLE_DATA_VALUE_6 = new Timestamp((new Date().getTime() / 1000) * 1000);
	private static final String VARIABLE_DATA_VALUE_7 = "1234AD";
	private static final String VARIABLE_DATA_VALUE_8 = "6543FD";
	// Mysql database cannot store miliseconds. We round it first.
	private static final Timestamp VARIABLE_DATA_VALID_FROM = new Timestamp((new Date().getTime() / 1000) * 1000);
	private static final Timestamp VARIABLE_DATA_VALID_TO = new Timestamp(VARIABLE_DATA_VALID_FROM.getTime()
			+ (3600 * 1000));
	private static final Timestamp VARIABLE_DATA_VALID_TO_2 = new Timestamp(VARIABLE_DATA_VALID_TO.getTime()
			+ (3600 * 1000));

	@Autowired
	private IGlobalVariablesDao globalVariablesDao;

	@Autowired
	private IVariableDataDao variableDataDao;

	@Test
	public void storeBasicVariables() throws NotValidFormException, FieldTooLongException {
		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.TEXT);
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_NAME);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_NAME);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
	}

	@Test
	public void storeBasicVariablesWithTextData() throws NotValidFormException, NotValidTypeInVariableData,
			FieldTooLongException {
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.TEXT);
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);

		globalVariable.addVariableData(VARIABLE_DATA_VALUE_1, VARIABLE_DATA_VALID_FROM, VARIABLE_DATA_VALID_TO);
		globalVariable.addVariableData(VARIABLE_DATA_VALUE_2, VARIABLE_DATA_VALID_TO, VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);
		Assert.assertEquals(variableDataDao.getRowCount(), 2);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		Assert.assertEquals(persistedList.get(0).getVariableData().size(), 2);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValue(), VARIABLE_DATA_VALUE_1);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidFrom(), VARIABLE_DATA_VALID_FROM);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidTo(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValue(), VARIABLE_DATA_VALUE_2);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidFrom(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidTo(), VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		Assert.assertEquals(variableDataDao.getRowCount(), 0);
	}

	@Test
	public void storeBasicVariablesWithNumberData() throws NotValidFormException, NotValidTypeInVariableData,
			FieldTooLongException {
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.NUMBER);
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);

		globalVariable.addVariableData(VARIABLE_DATA_VALUE_3, VARIABLE_DATA_VALID_FROM, VARIABLE_DATA_VALID_TO);
		globalVariable.addVariableData(VARIABLE_DATA_VALUE_4, VARIABLE_DATA_VALID_TO, VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);
		Assert.assertEquals(variableDataDao.getRowCount(), 2);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		Assert.assertEquals(persistedList.get(0).getVariableData().size(), 2);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValue(), VARIABLE_DATA_VALUE_3);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidFrom(), VARIABLE_DATA_VALID_FROM);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidTo(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValue(), VARIABLE_DATA_VALUE_4);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidFrom(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidTo(), VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		Assert.assertEquals(variableDataDao.getRowCount(), 0);
	}

	@Test
	public void storeBasicVariablesWithDateData() throws NotValidFormException, NotValidTypeInVariableData,
			FieldTooLongException {
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.DATE);
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);

		globalVariable.addVariableData(VARIABLE_DATA_VALUE_5, VARIABLE_DATA_VALID_FROM, VARIABLE_DATA_VALID_TO);
		globalVariable.addVariableData(VARIABLE_DATA_VALUE_6, VARIABLE_DATA_VALID_TO, VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);
		Assert.assertEquals(variableDataDao.getRowCount(), 2);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		Assert.assertEquals(persistedList.get(0).getVariableData().size(), 2);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValue(), VARIABLE_DATA_VALUE_5);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidFrom(), VARIABLE_DATA_VALID_FROM);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidTo(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValue(), VARIABLE_DATA_VALUE_6);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidFrom(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidTo(), VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		Assert.assertEquals(variableDataDao.getRowCount(), 0);
	}

	@Test
	public void storeBasicVariablesWithPostalCodeData() throws NotValidFormException, NotValidTypeInVariableData,
			FieldTooLongException {
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.POSTAL_CODE);
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);

		globalVariable.addVariableData(VARIABLE_DATA_VALUE_7, VARIABLE_DATA_VALID_FROM, VARIABLE_DATA_VALID_TO);
		globalVariable.addVariableData(VARIABLE_DATA_VALUE_8, VARIABLE_DATA_VALID_TO, VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);
		Assert.assertEquals(variableDataDao.getRowCount(), 2);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		Assert.assertEquals(persistedList.get(0).getVariableData().size(), 2);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValue(), VARIABLE_DATA_VALUE_7);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidFrom(), VARIABLE_DATA_VALID_FROM);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(0).getValidTo(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValue(), VARIABLE_DATA_VALUE_8);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidFrom(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getVariableData().get(1).getValidTo(), VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		Assert.assertEquals(variableDataDao.getRowCount(), 0);
	}

}
