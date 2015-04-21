package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramObjectDao;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;


@Repository
public class DiagramObjectDao extends AnnotatedGenericDao<DiagramObject,Long> implements IDiagramObjectDao {

	public DiagramObjectDao() {
		super(DiagramObject.class);
	}

}
