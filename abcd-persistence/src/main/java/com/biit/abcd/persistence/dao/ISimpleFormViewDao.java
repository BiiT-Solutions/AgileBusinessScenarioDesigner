package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface ISimpleFormViewDao {

	int getRowCount() throws UnexpectedDatabaseException;

	List<SimpleFormView> getAll();

	List<SimpleFormView> getSimpleFormViewByLabelAndOrganization(String label, Long organizationId);

}
