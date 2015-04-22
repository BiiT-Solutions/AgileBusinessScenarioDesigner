package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.diagram.Diagram;


@Repository
public class DiagramDao extends AnnotatedGenericDao<Diagram, Long>  implements IDiagramDao {

	public DiagramDao() {
		super(Diagram.class);
	}

}
