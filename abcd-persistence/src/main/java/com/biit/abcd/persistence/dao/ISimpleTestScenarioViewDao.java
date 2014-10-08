package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.SimpleTestScenarioView;

public interface ISimpleTestScenarioViewDao {

	int getRowCount();

	List<SimpleTestScenarioView> getAll();
	
	List<SimpleTestScenarioView> getSimpleTestScenarioByFormId(Long formId);

}
