package com.biit.abcd.persistence.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.biit.abcd.persistence.entity.SimpleFormView;

public interface ISimpleFormViewDao {

	int getRowCount();

	List<SimpleFormView> getAll();
	
	List<SimpleFormView> getSimpleFormViewByLabelAndOrganization(String label, Long organizationId);

	void setSessionFactory(SessionFactory sessionFactory);

	SessionFactory getSessionFactory();

}
