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
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class GlobalVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String BASIC_GLOBAL_VARIABLE_NAME = "Variable";
	private static final String BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME = "Variable with data";
	private static final String VARIABLE_DATA_VALUE_1 = "Value1";
	private static final String VARIABLE_DATA_VALUE_2 = "Value2";
	// Mysql database cannot store miliseconds. We round it first.
	private static final Timestamp VARIABLE_DATA_VALID_FROM = new Timestamp((new Date().getTime() / 1000) * 1000);
	private static final Timestamp VARIABLE_DATA_VALID_TO = new Timestamp(VARIABLE_DATA_VALID_FROM.getTime()
			+ (3600 * 1000));
	private static final Timestamp VARIABLE_DATA_VALID_TO_2 = new Timestamp(VARIABLE_DATA_VALID_TO.getTime()
			+ (3600 * 1000));

	@Autowired
	private IGlobalVariables globalVariablesDao;

	@Autowired
	private IVariableDataDao variableDataDao;

	@Test(groups = { "globalVariablesDao" })
	public void storeBasicVariables() throws NotValidFormException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_NAME);
		globalVariable.setFormat(AnswerFormat.TEXT);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_NAME);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
	}

	@Test(groups = { "globalVariablesDao" })
	public void storeBasicVariablesWithData() throws NotValidFormException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setName(BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		globalVariable.setFormat(AnswerFormat.TEXT);

		VariableData variableData1 = new VariableData();
		variableData1.setValue(VARIABLE_DATA_VALUE_1);
		variableData1.setValidFrom(VARIABLE_DATA_VALID_FROM);
		variableData1.setValidTo(VARIABLE_DATA_VALID_TO);
		globalVariable.getData().add(variableData1);

		VariableData variableData2 = new VariableData();
		variableData2.setValue(VARIABLE_DATA_VALUE_2);
		variableData2.setValidFrom(VARIABLE_DATA_VALID_TO);
		variableData2.setValidTo(VARIABLE_DATA_VALID_TO_2);
		globalVariable.getData().add(variableData2);

		globalVariablesDao.makePersistent(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 1);
		Assert.assertEquals(variableDataDao.getRowCount(), 2);

		List<GlobalVariable> persistedList = globalVariablesDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), BASIC_GLOBAL_VARIABLE_WITH_DATA_NAME);
		Assert.assertEquals(persistedList.get(0).getData().size(), 2);
		Assert.assertEquals(persistedList.get(0).getData().get(0).getValue(), VARIABLE_DATA_VALUE_1);
		Assert.assertEquals(persistedList.get(0).getData().get(0).getValidFrom(), VARIABLE_DATA_VALID_FROM);
		Assert.assertEquals(persistedList.get(0).getData().get(0).getValidTo(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getData().get(1).getValue(), VARIABLE_DATA_VALUE_2);
		Assert.assertEquals(persistedList.get(0).getData().get(1).getValidFrom(), VARIABLE_DATA_VALID_TO);
		Assert.assertEquals(persistedList.get(0).getData().get(1).getValidTo(), VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makeTransient(globalVariable);
		Assert.assertEquals(globalVariablesDao.getRowCount(), 0);
		Assert.assertEquals(variableDataDao.getRowCount(), 0);
	}

}
