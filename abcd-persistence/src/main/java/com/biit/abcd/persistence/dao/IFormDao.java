package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface IFormDao extends IJpaGenericDao<Form,Long> {

	int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException;

	Form getForm(String label, Integer version, Long organizationId);

	boolean exists(String value, Integer version, Long organizationId, Long id);

	boolean exists(String value, long organizationId);
	
	List<Form> getAll(Long organizationId);

}