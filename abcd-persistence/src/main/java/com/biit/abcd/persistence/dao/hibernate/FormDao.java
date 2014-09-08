package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.BaseForm;
import com.biit.form.persistence.dao.hibernate.TreeObjectDao;

@Repository
public class FormDao extends TreeObjectDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	public int getLastVersion(Form form) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Integer> cq = criteriaBuilder.createQuery(Integer.class);
		Root<Form> root = cq.from(getType());
		cq.select(criteriaBuilder.max(root.<Integer> get("version")));
		cq.where(criteriaBuilder.equal(root.get("name"), form.getName()));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public Form getForm(String name) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<BaseForm> cq = criteriaBuilder.createQuery(BaseForm.class);
		Root<Form> root = cq.from(getType());
		cq.where(criteriaBuilder.equal(root.get("name"), name));
		List<BaseForm> results = getEntityManager().createQuery(cq).getResultList();
		if (!results.isEmpty()) {
			return (Form) results.get(0);
		}
		return null;
	}
}
