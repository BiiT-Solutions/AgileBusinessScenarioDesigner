package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;

@Repository
public class FormDao extends GenericDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		for (Form form : forms) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(form.getChildren());
		}
	}

}
