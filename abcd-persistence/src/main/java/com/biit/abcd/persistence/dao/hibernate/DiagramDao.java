package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class DiagramDao extends GenericDao<Diagram> implements IDiagramDao {


	public DiagramDao() {
		super(Diagram.class);
	}

	@Override
	public Diagram read(Form form) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Diagram> cq = criteriaBuilder.createQuery(getType());
		Root<Diagram> root = cq.from(getType());
		cq.where(criteriaBuilder.equal(root.get("form"), form));
		List<Diagram> results = getEntityManager().createQuery(cq).getResultList();
		if (!results.isEmpty()) {
			return (Diagram) results.get(0);
		}
		return null;
	}

}
