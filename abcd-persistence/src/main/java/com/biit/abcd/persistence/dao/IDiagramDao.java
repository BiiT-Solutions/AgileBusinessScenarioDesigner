package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.persistence.dao.IGenericDao;

public interface IDiagramDao extends IGenericDao<Diagram> {

	/**
	 * Gets the diagrams related to a form.
	 * 
	 * @param form
	 * @return
	 */
	Diagram read(Form form);

}
