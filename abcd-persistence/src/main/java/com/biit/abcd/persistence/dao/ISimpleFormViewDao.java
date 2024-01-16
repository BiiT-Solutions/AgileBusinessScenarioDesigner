package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

import java.util.List;

public interface ISimpleFormViewDao {

    int getRowCount() throws UnexpectedDatabaseException;

    List<SimpleFormView> getAll();

    List<SimpleFormView> getSimpleFormViewByLabelAndOrganization(String label, Long organizationId);

    SimpleFormView getSimpleFormViewByLabelAndVersionAndOrganization(String label, Integer version, Long organizationId);

    List<SimpleFormView> getSimpleFormViewByOrganization(Long organizationId);

    SimpleFormView get(Long id);

}
