package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ICategoryDao;
import com.biit.abcd.persistence.entity.Category;

@Repository
public class CategoryDao extends GenericDao<Category> implements ICategoryDao {

	public CategoryDao() {
		super(Category.class);
	}

	@Override
	protected void initializeSets(List<Category> Categories) {
		for (Category category : Categories) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(category.getChildren());
		}
	}
}
