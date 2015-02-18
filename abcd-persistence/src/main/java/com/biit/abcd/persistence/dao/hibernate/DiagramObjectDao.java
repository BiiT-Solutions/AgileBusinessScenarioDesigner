package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramObjectDao;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class DiagramObjectDao extends GenericDao<DiagramObject> implements IDiagramObjectDao {

	public DiagramObjectDao() {
		super(DiagramObject.class);
	}

	@Override
	protected void initializeSets(List<DiagramObject> arg0) {

	}

}
