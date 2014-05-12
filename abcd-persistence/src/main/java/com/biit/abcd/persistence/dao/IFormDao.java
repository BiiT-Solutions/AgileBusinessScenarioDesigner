package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Form;

public interface IFormDao extends IGenericDao<Form> {

	int getLastVersion(Form form);

	Form getForm(String name);

}
