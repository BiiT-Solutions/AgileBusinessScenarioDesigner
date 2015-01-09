package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface IFormDao extends IBaseFormDao<Form> {

	int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException;

}