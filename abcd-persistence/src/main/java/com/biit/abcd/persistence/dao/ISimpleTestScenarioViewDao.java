package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.SimpleTestScenarioView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface ISimpleTestScenarioViewDao {

	/**
	 * Returns a list of all the test scenarios related to the forms that have form organization and form label equals
	 * to the passed form
	 * 
	 * @param formId
	 * @return
	 */
	List<SimpleTestScenarioView> getSimpleTestScenariosByFormId(Long formId);
}
