package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface IFormDao extends IJpaGenericDao<Form,Long> {

	int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException;

}