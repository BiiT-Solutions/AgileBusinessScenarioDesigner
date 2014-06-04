package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormCustomVariables;

public interface IFormCustomVariablesDao extends IGenericDao<FormCustomVariables> {

	FormCustomVariables getFormCustomVariables(Form form);

}
