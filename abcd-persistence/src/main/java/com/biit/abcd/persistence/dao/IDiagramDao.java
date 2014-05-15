package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Diagram;
import com.biit.abcd.persistence.entity.Form;

public interface IDiagramDao extends IGenericDao<Diagram> {

	/**
	 * Gets the diagrams related to a form.
	 * 
	 * @param form
	 * @return
	 */
	Diagram read(Form form);

}
